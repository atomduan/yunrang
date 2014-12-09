package com.yunrang.hadoop.app.job.history.recompute;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.hadoop.mapreduce.lib.output.NullOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.mortbay.log.Log;

import com.yunrang.hadoop.app.SingleMapReduceJobClient.JobSubmitContext;
import com.yunrang.hadoop.app.SingleMapReduceJobClient.SMRDefinition;
import com.yunrang.hadoop.app.common.config.HBaseWeiboColumns;
import com.yunrang.hadoop.app.job.history.recompute.utils.WeiboHbaseAccesser;
import com.yunrang.hadoop.app.utils.PathUtil;
import com.yunrang.hadoop.app.utils.SAWParser;

public class ShCdh4HbaseMergeResult extends SMRDefinition {
	public static final String QI_VALID="mapreduce.job.weibo.search.recompute.qi.valid";
	public static final String OUTPUT_ROOT="mapreduce.job.weibo.search.recompute.output.root";
	private static String outputPath;
	private static String qiValidStr;
	private static String inputPathes;
	
	public static final String FAN_OUT = "fanout";

	public static final String QI_FROM_HBASE = "HbaseQI";
	public static final String TOPIC_FROM_HBASE = "HbaseTOPIC";
	public static final String NONTOPIC_FROM_HBASE = "HbaseNONTOPIC";
	public static final String USERTEXT_FROM_HBASE = "HbaseUSERTEXT";
	
	public void doConfiguration(JobSubmitContext jobSubmitContext, Job currJob) throws Exception {
		try {
			//Job queue config
			currJob.getConfiguration().set("mapreduce.job.queuename", "default");
			parseFromPayLoad(jobSubmitContext);
			currJob.getConfiguration().set(QI_VALID, qiValidStr);
			currJob.getConfiguration().set(OUTPUT_ROOT, outputPath);
			//Configure Input
			String defaultInputPaths = inputPathes;
			currJob.setInputFormatClass(TextInputFormat.class);
			TextInputFormat.setInputPaths(currJob, PathUtil.detectRecursively(currJob, InnerPathFilter.class, defaultInputPaths));
			//Configure Job Mapper
			currJob.setMapperClass(InnerMapper.class);
			currJob.setMapOutputKeyClass(Text.class);
			currJob.setMapOutputValueClass(Text.class);
			//Configure Job Reducer
			currJob.setReducerClass(InnerReducer.class);
			currJob.setNumReduceTasks(SMRDefinition.DEFAULT_REDUCE_NUM);
			currJob.setOutputFormatClass(NullOutputFormat.class); //close default output
			//Set output path for job auditing info output
			currJob.getConfiguration().set("mapred.output.dir", outputPath);
			Log.info("ShCdh4HbaseMergeResult: config outputPath ["+outputPath+"]");
			//Configure Output
			MultipleOutputs.addNamedOutput(currJob, FAN_OUT, TextOutputFormat.class, Text.class, Text.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	private void parseFromPayLoad(JobSubmitContext jobSubmitContext) {
		String payLoad = StringUtils.trimToNull(jobSubmitContext.getPayLoad());
		String[] params = payLoad.split(DEFAULT_PAYLOAD_SEP);
		inputPathes = StringUtils.trimToNull(params[0]).trim();
		qiValidStr = StringUtils.trimToNull(params[1]).trim();
		outputPath = StringUtils.trimToNull(params[2]).trim();
		this.validQiValidStr(qiValidStr);
	}
	
	/*
	 * for mal format, an exception will throw.
	 */
	private void validQiValidStr(String qiValidStr) {
		String[] params = qiValidStr.split(",");
		for (String p : params) {
			Integer.parseInt(p);
		}
	}
	
	public static class InnerPathFilter implements PathFilter {
		public boolean accept(Path path) {
			String urlStr = path.toUri().getPath().trim();
			if (urlStr.contains("part-")) {
				return true;
			}
			return false;
		}
	}
	public static class InnerMapper extends Mapper<LongWritable, Text, Text, Text> {
		private SAWParser parser;
		public void setup(Context context) {
			final Context ctx = context;
			this.parser = SAWParser.getParser(new SAWParser.RecordHandler() {
				public void processOneRecord(Map<String, String> attrMap) {
					try {
						String midStr = attrMap.get(HBaseWeiboColumns.ID.getName());
			        	if (StringUtils.trimToNull(midStr) != null) {
			        		ctx.write(new Text(midStr), new Text(SAWParser.linageRecorde(attrMap)));
			        	}
					} catch (Exception e) {}
				}
			});
		}
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        	try {
	        	String line = StringUtils.trimToNull(value.toString());
	        	this.parser.offerLine(line);
        	} catch (Exception e) {} 
        }
		public void cleanup(Context context) {
        	try {
        		this.parser.flush();
        	} catch (Exception e) {}
        }
    }
	
	public static class InnerReducer extends Reducer<Text, Text, Text, Text> {
		protected List<Integer> qiValidList = new ArrayList<Integer>();
		protected Set<String> dict = new HashSet<String>();
		protected MultipleOutputs<Text, Text> mos;
		protected String outputRoot;
		protected WeiboHbaseAccesser hbaseAccessor;
		
		protected String syncBackBaseName;
		protected String mergedAttrBaseName;
		protected String mergedIndexBaseName;
		protected String hbaseDebugInfoBaseName;
		
		public void setup(Context context) {
			String qiValidStr = context.getConfiguration().get(QI_VALID);
			qiValidStr = StringUtils.trimToNull(qiValidStr);
			if (qiValidStr == null) {
				throw new RuntimeException("can not get qiValidStr in reducer, abandon this task");
			}
			String[] params = qiValidStr.split(",");
			for (String p : params) {
				p = StringUtils.trimToNull(p);
				if (p != null) {
					qiValidList.add(Integer.parseInt(p));
				}
			}
			outputRoot = context.getConfiguration().get(OUTPUT_ROOT);
			if (StringUtils.trimToNull(outputRoot) == null) {
				throw new RuntimeException("the runtime output root is empty.....");
			}
			
			syncBackBaseName = outputRoot+"/"+"syncBack"+"/part";
			mergedAttrBaseName = outputRoot+"/"+"mergedAttr"+"/part";
			mergedIndexBaseName = outputRoot+"/"+"mergedIndex"+"/part";
			
			mos = new MultipleOutputs<Text, Text>(context);
			try {
				hbaseAccessor = new WeiboHbaseAccesser();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		
		@Override
		public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
			Map<String, String> record = new HashMap<String, String>();
			try {
				//get merged record map from values.
				for (Text v : values) {
					String recordStr = v.toString();
					Map<String, String> rm = SAWParser.delinageRecorde(recordStr);
					for (String k : rm.keySet()) {
						if (dict.contains(k) == false) {
							dict.add(k);
							record.put(k, rm.get(k));
						}
					}
				}
			} finally {
				dict.clear();
			}
			try {
				processHbaseRecordMerge(context, record);
			} catch (Exception e) {}
		}
		
		public void cleanup(Context context) {
        	try {
        		this.mos.close();
        	} catch (Exception e) {}
        }
		
		private void processHbaseRecordMerge(Context context, Map<String, String> record) throws Exception {
			if (this.hasNesscaryFieldsToUpdate(record, qiValidList)) {
				Map<String, String> mergeResult= this.genMergedkRecord(context, record);
				if (mergeResult.size() > 0) {
					String midKeyStr = HBaseWeiboColumns.ID.getName();
					String dateKeyStr = HBaseWeiboColumns.TIME.getName();
					String midStr = record.get(midKeyStr);
					String date = record.get(dateKeyStr);
					//Get fan out result
					Map<String, String> syncBackRecord = getSyncBackRecord(mergeResult);
					Map<String, String> mergedAttrRecord = getMergedAttrRecord(mergeResult);
					Map<String, String> mergedIndexRecord = getMergedIndexRecord(mergeResult, midStr, date);
					if (syncBackRecord.size() > 0) {
						//Add header
						mapPutIfAbasent(syncBackRecord, midKeyStr, midStr);
						mapPutIfAbasent(syncBackRecord, dateKeyStr, date);
						String key = SAWParser.linageRecorde(syncBackRecord);
						mos.write(FAN_OUT, key, null, syncBackBaseName);
					}
					if (mergedAttrRecord.size() > 0) {
						//Add header
						mapPutIfAbasent(mergedAttrRecord, midKeyStr, midStr);
						mapPutIfAbasent(mergedAttrRecord, dateKeyStr, date);
						String key = SAWParser.linageRecorde(mergedAttrRecord);
						mos.write(FAN_OUT, key, null, mergedAttrBaseName);
					}
					if (mergedIndexRecord.size() > 0) {	
						//Add header
						mapPutIfAbasent(mergedIndexRecord, midKeyStr, midStr);
						mapPutIfAbasent(mergedIndexRecord, dateKeyStr, date);
						String key = SAWParser.linageRecorde(mergedIndexRecord);
						mos.write(FAN_OUT, key, null, mergedIndexBaseName);
					}
				}
			}
		}

		private Map<String, String> genMergedkRecord(Context context, Map<String, String> record) throws Exception {
			Map<String, String> result = new HashMap<String, String>();
			try {
				String hQIMerged = null;
				String rQI = record.get(HBaseWeiboColumns.QI.getName());
				rQI = StringUtils.trimToNull(rQI);
				if (rQI != null) {
					String hQI = StringUtils.trimToNull(record.get(QI_FROM_HBASE));
					if (hQI != null) {
						hQIMerged = this.processQIMerge(rQI, hQI, qiValidList);
					}
				}
				if (hQIMerged != null) {
					result.put(HBaseWeiboColumns.QI.getName(), hQIMerged);
				}
			} catch(Exception e) {}
			
			try {
				String hbaseTopic = record.get(TOPIC_FROM_HBASE);
				String recordTopic = record.get(HBaseWeiboColumns.TOPIC_WORDS.getName());
				String mergeTopic = this.processSEPFiledMerge(hbaseTopic, recordTopic, " ");
				if (mergeTopic != null) {
					result.put(HBaseWeiboColumns.TOPIC_WORDS.getName(), mergeTopic);
				}
			} catch(Exception e) {}
			
			try {
				String hbaseNonTopic = record.get(NONTOPIC_FROM_HBASE);
				String recordNonTopic = record.get(HBaseWeiboColumns.NON_TOPIC_WORDS.getName());
				String mergeNonTopic = this.processSEPFiledMerge(hbaseNonTopic, recordNonTopic, " ");
				if (mergeNonTopic != null) {
					result.put(HBaseWeiboColumns.NON_TOPIC_WORDS.getName(), mergeNonTopic);
				}
			} catch(Exception e) {}
			
			try {
				String hbaseUserText = record.get(USERTEXT_FROM_HBASE);
				String recordUserText = record.get(HBaseWeiboColumns.USERTEXT.getName());
				String mergeUserText = this.processSEPFiledMerge(hbaseUserText, recordUserText, ",");
				if (mergeUserText != null) {
					result.put(HBaseWeiboColumns.USERTEXT.getName(), mergeUserText);
				}
			} catch(Exception e) {}
			
			//Filter out helping fields...
			for (String k : record.keySet()) {
				if (k.equals(HBaseWeiboColumns.ID.getName())) {
					continue;
				}
				if (k.equals(HBaseWeiboColumns.TIME.getName())) {
					continue;
				}
				if (k.equals(HBaseWeiboColumns.QI.getName())) {
					continue;
				}
				if (k.equals(HBaseWeiboColumns.TOPIC_WORDS.getName())) {
					continue;
				}
				if (k.equals(HBaseWeiboColumns.NON_TOPIC_WORDS.getName())) {
					continue;
				}
				if (k.equals(HBaseWeiboColumns.USERTEXT.getName())) {
					continue;
				}
				if (k.equals(QI_FROM_HBASE)) {
					continue;
				}
				if (k.equals(TOPIC_FROM_HBASE)) {
					continue;
				}
				if (k.equals(NONTOPIC_FROM_HBASE)) {
					continue;
				}
				if (k.equals(USERTEXT_FROM_HBASE)) {
					continue;
				}
				String value = StringUtils.trimToNull(record.get(k));
				if (value != null) {
					if (HBaseWeiboColumns.isValidFieldsName(k)) {
						result.put(k, value);
					}
				}
			}
			return result;
		}
		
		private static ThreadLocal<Set<String>> htpSetTL = new ThreadLocal<Set<String>>(){
			protected Set<String> initialValue() {
				return new HashSet<String>();
			}
		};
		private static ThreadLocal<Set<String>> rtpSetTL = new ThreadLocal<Set<String>>(){
			protected Set<String> initialValue() {
				return new HashSet<String>();
			}
		};
		private String processSEPFiledMerge(String fromHbase, String fromRecord, String dli) {
			htpSetTL.get().clear();
			rtpSetTL.get().clear();
			try {
				String ht = StringUtils.trimToNull(fromHbase);
				String rt = StringUtils.trimToNull(fromRecord);
				if (ht == null && rt == null) {
					return null;
				}
				if (ht == null && rt != null) {
					return rt;
				}
				if (ht != null && rt == null) {
					return null;
				}
				if (ht != null && rt != null) {
					
					String[] htp = ht.split(dli);
					Set<String> htpSet = htpSetTL.get();
					for (String w : htp) {
						w = StringUtils.trimToEmpty(w);
						htpSet.add(w);
					}
					
					String[] rtp = rt.split(dli);
					Set<String> rtpSet = rtpSetTL.get();
					for (String w : rtp) {
						w = StringUtils.trimToEmpty(w);
						rtpSet.add(w);
					}
					
					boolean hasDiff = false;
					if (htpSet.size() == rtpSet.size()) {
						for (String rw : rtp) {
							rw = StringUtils.trimToEmpty(rw);
							if (htpSet.contains(rw) == false) {
								hasDiff = true;
								break;
							}
						}
					} else {
						hasDiff = true;
					}
					
					if (hasDiff) {
						return rt;
					}
				}
				return null;
			} finally {
				htpSetTL.get().clear();
				rtpSetTL.get().clear();
			}
		}
		
		private String processQIMerge(String rQI, String hQI, List<Integer> qiValidList) {
			Integer rqi = Integer.parseInt(rQI);
			Integer hqi = Integer.parseInt(hQI);
			boolean needOverWrite = false;
			for (Integer pos : qiValidList) {
				Integer p = 1 << pos;
				if ((p & rqi) != (p & hqi)) {
					needOverWrite = true;
					hqi = (hqi & (~p)) | (rqi & p);
				}
			}
			if (needOverWrite == true) {
				return hqi+"";
			} else {
				return null;
			}
		}
		
		private boolean hasNesscaryFieldsToUpdate(Map<String, String> record, List<Integer> qiValidList) {
			for (String key : record.keySet()) {
				if (key != null && !key.equals(HBaseWeiboColumns.ID.getName()) 
						&& !key.equals(HBaseWeiboColumns.TIME.getName())) {
					return true;
				}
			}
			return false;
		}
		
		private void mapPutIfAbasent(Map<String, String> record, String key, String value) {
			if (record.containsKey(key) == false) {
				record.put(key, value);
			}
		}
		
		public static final Set<String> INDEX_SET = new HashSet<String>();
		static {
			INDEX_SET.add(HBaseWeiboColumns.NON_TOPIC_WORDS.getName());
			INDEX_SET.add(HBaseWeiboColumns.TOPIC_WORDS.getName());
			INDEX_SET.add(HBaseWeiboColumns.USERTEXT.getName());
		}

		private Map<String, String> getMergedAttrRecord(Map<String, String> mergeResult) {
			Map<String, String> result = new HashMap<String, String>();
			if (mergeResult.containsKey(HBaseWeiboColumns.QI.getName())) {
				String qiStr = mergeResult.get(HBaseWeiboColumns.QI.getName());
				result.put(HBaseWeiboColumns.QI.getName(), qiStr);
			}
			return result;
		}
		
		private Map<String, String> getMergedIndexRecord(Map<String, String> mergeResult, String mid, String date) throws Exception {
			boolean isIndexContain = false;
			for (String k : INDEX_SET) {
				if (mergeResult.containsKey(k)) {
					isIndexContain = true;
					break;
				}
			}
			if (isIndexContain) {
				Map<String, String> result = this.hbaseAccessor.get(mid, date);
				for (String k : mergeResult.keySet()) {
					result.put(k, mergeResult.get(k));
				}
				return result;
			} else {
				return new HashMap<String, String>();
			}
		}
		
		private Map<String, String> getSyncBackRecord(Map<String, String> mergeResult) {
			Map<String, String> result = new HashMap<String, String>();
			for (String k : mergeResult.keySet()) {
				if (HBaseWeiboColumns.isValidFieldsName(k)) {
					String value = StringUtils.trimToNull(mergeResult.get(k));
					if (value != null) {
						result.put(k, value);
					}
				}
			}
			return result;
		}
	}
}
