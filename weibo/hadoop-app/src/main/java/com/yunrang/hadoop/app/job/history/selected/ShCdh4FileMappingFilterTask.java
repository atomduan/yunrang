package com.yunrang.hadoop.app.job.history.selected;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import com.yunrang.hadoop.app.SingleMapReduceJobClient.JobSubmitContext;
import com.yunrang.hadoop.app.SingleMapReduceJobClient.SMRDefinition;
import com.yunrang.hadoop.app.common.filter.RecordFilter;
import com.yunrang.hadoop.app.common.input.NonSplittableTextInputFormat;
import com.yunrang.hadoop.app.job.history.selected.filter.SelectedWeiboHistoryFilter;
import com.yunrang.hadoop.app.utils.JsonUtil;
import com.yunrang.hadoop.app.utils.PathUtil;
import com.yunrang.hadoop.app.utils.SAWParser;
import com.yunrang.hadoop.app.utils.WeiboUtil;

public class ShCdh4FileMappingFilterTask extends SMRDefinition {
	public static final String INPUT_ROOT_PATH_KEY = "mapreduce.job.input.root.path";
	public static final String OUTPUT_ROOT_PATH_KEY = "mapreduce.job.output.root.path";
	public static final String MOS_KEY = "fanout";
	
	public void doConfiguration(JobSubmitContext jobSubmitContext, Job currJob) throws Exception {
		try {
			//Job queue config
			currJob.getConfiguration().set("mapreduce.job.queuename", "default");
			//Configure Input
			String inputRootPath = "/tmp/juntaoduan/history_selected_filter_0928";
			currJob.setInputFormatClass(NonSplittableTextInputFormat.class);
			NonSplittableTextInputFormat.setInputPaths(currJob, PathUtil.detectRecursively(currJob, inputRootPath));
			//Configure Job Mapper
			currJob.setMapperClass(InnerMapper.class);
			currJob.setMapOutputKeyClass(Text.class);
			currJob.setMapOutputValueClass(Text.class);
			//Configure Job Reducer
			currJob.setNumReduceTasks(SMRDefinition.SING_REDUCE_NUM);//for default output summary reason
			//Configure Output
			String outputRootPath = "/tmp/juntaoduan/history_selected_filter_1022"; //offical
			//OutputFormat configuration
			currJob.setOutputFormatClass(TextOutputFormat.class);
			TextOutputFormat.setOutputPath(currJob, new Path(outputRootPath + "/_logs"));
			currJob.setReducerClass(InnerReducer.class);//for summary reason.
			//MultiOutputsConfig
			currJob.getConfiguration().set(INPUT_ROOT_PATH_KEY, PathUtil.normalizePathStr(inputRootPath));
			currJob.getConfiguration().set(OUTPUT_ROOT_PATH_KEY, PathUtil.normalizePathStr(outputRootPath));
			MultipleOutputs.addNamedOutput(currJob, MOS_KEY, TextOutputFormat.class, Text.class, Text.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	public static class InnerMapper extends Mapper<LongWritable, Text, Text, Text> {
		private SAWParser parser;
		private String inputRootPath;
		private String outputRootPath;
		private MultipleOutputs<Text, Text> mos;
		private AtomicLong count = new AtomicLong(0);
		
		public void setup(Context context) {
			Configuration cfg = context.getConfiguration();
			String inputRootPathStr = StringUtils.trimToNull(cfg.get(INPUT_ROOT_PATH_KEY));
			if (inputRootPathStr != null) {
				this.inputRootPath = inputRootPathStr;
			} else {
				throw new RuntimeException("can not get [mapreduce.job.input.root.path] on mapping phase");
			}
			String outputRootPathStr = StringUtils.trimToNull(cfg.get(OUTPUT_ROOT_PATH_KEY));
			if (outputRootPathStr != null) {
				this.outputRootPath = outputRootPathStr;
			} else {
				throw new RuntimeException("can not get [mapreduce.job.output.root.path] on mapping phase");
			}
			final Context ctx = context;
			this.parser = SAWParser.getParser(new SAWParser.RecordHandler() {
				public void processOneRecord(Map<String, String> attrMap) {
					try {
						processOneRecordReceived(attrMap, ctx);
					} catch (Exception e) {}
				}
			});
			this.mos = new MultipleOutputs<Text, Text>(context);
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
        		this.mos.close();
        		context.write(new Text(this.count.toString()), new Text(this.count.toString()));
        	} catch (Exception e) {}
        }
        
        //Basically it is a file name prefix, not a dir.
        private String outputFilePathCache;
        public void writeOutRecord(Map<String, String> attrMap, Context context) throws Exception {
        	FileSplit fsp = (FileSplit) context.getInputSplit();
        	String inputFilePath = fsp.getPath().toString();
        	if (this.outputFilePathCache == null) {
        		this.outputFilePathCache = inputFilePath.replace(inputRootPath, outputRootPath);
        	}
        	String record = SAWParser.linageRecorde(attrMap);
        	this.writeOutRecord(new Text(record), null, outputFilePathCache);
        }

        public void writeOutRecord(Text key, Text value, String outputFilePath) throws Exception {
        	mos.write(MOS_KEY, key, value, outputFilePath);
        }
 
        private RecordFilter docFilter = new SelectedWeiboHistoryFilter();
        private void processOneRecordReceived(Map<String, String> attrMap, Context context) throws Exception {
        	if (docFilter.accept(JsonUtil.parse(attrMap))) {
        		this.count.incrementAndGet();
        		this.writeOutRecord(attrMap, context);
        	}
		}
    }
	
	//this reducer is defined for default output format, for summary reason, but not for mos. 
	public static class InnerReducer extends Reducer<Text, Text, Text, Text> {
		private AtomicLong count = new AtomicLong(0);
		@Override
		public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
			for (Text t : values) {
				Long keyCount = WeiboUtil.parseLong(t.toString());
				count.addAndGet(keyCount);
			}
		}
		protected void cleanup(Context context) throws IOException, InterruptedException {
        	context.write(new Text(count.toString()), null);
        }
	}
}
