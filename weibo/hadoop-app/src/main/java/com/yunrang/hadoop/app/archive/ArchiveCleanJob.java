package com.yunrang.hadoop.app.archive;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import com.yunrang.hadoop.app.SingleMapReduceJobClient.JobSubmitContext;
import com.yunrang.hadoop.app.SingleMapReduceJobClient.SMRDefinition;
import com.yunrang.hadoop.app.utils.PathUtil;

public class ArchiveCleanJob extends SMRDefinition {
	private static String filterIndecator = "";
	private static final String RECORD_SEP = "\t";
	private static final String COMMON_FILTE_OUT = "(\"|:|：| |"+RECORD_SEP+")";
	
	public void doConfiguration(JobSubmitContext jobSubmitContext, Job currJob) throws Exception {
		//Job queue config
		currJob.getConfiguration().set("mapreduce.job.queuename", "default");
		//Parse payLoads
		filterIndecator = jobSubmitContext.getPayLoad().trim();
		//Configure Input
		String defaultInputPaths = "/production/raw_archvie/history_at_user";
		TextInputFormat.setInputPaths(currJob, 
				PathUtil.detectRecursively(currJob, InnerPathFilter.class, defaultInputPaths));
		TextInputFormat.setInputPathFilter(currJob, InnerPathFilter.class);
		currJob.setInputFormatClass(TextInputFormat.class);		
		//Configure Job Mapper
		currJob.setMapperClass(InnerMapper.class);
		currJob.setMapOutputKeyClass(Text.class);
		currJob.setMapOutputValueClass(Text.class);
		//Configure Job Reducer
		currJob.setNumReduceTasks(SMRDefinition.DEFAULT_REDUCE_NUM);
		currJob.setReducerClass(InnerReducer.class);
		//Configure Output
		currJob.setOutputFormatClass(TextOutputFormat.class);
		String currTimeStamp = System.currentTimeMillis()+"";
		String defaultOutputPath = "/tmp/raw_archvie_process/history_at_user_merge";
		TextOutputFormat.setOutputPath(currJob, new Path(defaultOutputPath+ "/" + currTimeStamp));
	}
	
	public static class InnerPathFilter implements PathFilter {
		public boolean accept(Path path) {
			String urlStr = path.toUri().getPath().trim();
			if (urlStr.contains("processed")) {
				return urlStr.contains(filterIndecator);
			}
			return true;
		}
	}
	
	/**
	 * recorde sample
	 * ......
	 * @
	 * @ACTION:M
	 * @ID:"3677666182147195"
	 * @UID:1652271005
	 * @USERTEXT:@xiaomi;@XIAOMI;
	 * @UNRNUM:2
	 * ......
	 */
	public static class InnerMapper extends Mapper<LongWritable, Text, Text, Text> {
		Map<String, String> buff = new HashMap<String, String>();
        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        	try {
	        	String line = StringUtils.trimToNull(value.toString());
	        	if (line.equals("@")) {
	        		if (buff.size() > 0) {
	        			String rLine = listToString(buff);
	        			if (!StringUtils.isEmpty(rLine)) {
	        				context.write(new Text(rLine), new Text());
	        			}
	        		} 
	        		buff.clear();
	        	} else {
	        		processLine(line, buff);
	    		}
        	} catch (Exception e) {
        		buff.clear();
        	} 
        }
    }

	static void processLine(String line, Map<String, String> buff) {
		line = line.replaceAll(COMMON_FILTE_OUT, "");
		if (line.startsWith("@ID")) {
			buff.put("ID", line.replace("@ID", ""));
		}
		if (line.startsWith("@UID")) {
			buff.put("UID", line.replace("@UID", ""));
		}
		if (line.startsWith("@USERTEXT")) {
			buff.put("USERTEXT", line.replace("@USERTEXT", ""));
		}
		if (line.startsWith("@UNRNUM")) {
			buff.put("UNRNUM", line.replace("@UNRNUM", ""));
		}
	}
	
	static String listToString(Map<String, String> buff) {
		String result = "";
		String id = buff.get("ID");
		String uid = buff.get("UID");
		String unrnum = buff.get("UNRNUM");
		String userText = buff.get("USERTEXT");
		if (!StringUtils.isEmpty(id) && !StringUtils.isEmpty(uid) && !StringUtils.isEmpty(unrnum) && !StringUtils.isEmpty(userText)) {
			StringBuffer sb = new StringBuffer();
			sb.append(id).append(RECORD_SEP).append(uid).append(RECORD_SEP).append(unrnum).append(RECORD_SEP).append(userText);
			return sb.toString();
		}
		return result;
	}
	
	/**
	 * sample:
	 * 3677666182147195	1652271005	2	@xiaomi;@XIAOMI;
	 */
	public static class InnerReducer extends Reducer<Text, Text, Text, LongWritable> {
        public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        	String keyStr = key.toString();
        	if (!StringUtils.isEmpty(keyStr)) {
        		context.write(new Text(keyStr), null);
        	}
        }
	}
}