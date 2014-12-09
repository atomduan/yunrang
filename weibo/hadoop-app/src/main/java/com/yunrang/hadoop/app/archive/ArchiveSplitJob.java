package com.yunrang.hadoop.app.archive;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.fs.Path;
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

public class ArchiveSplitJob extends SMRDefinition {
	public void doConfiguration(JobSubmitContext jobSubmitContext, Job currJob) throws Exception {
		//Job queue config
		currJob.getConfiguration().set("mapreduce.job.queuename", "default");
		//Parse payLoads
		//Configure Input
		String defaultInputPaths = "/tmp/raw_archvie_process/history_at_user_single";
		TextInputFormat.setInputPaths(currJob, PathUtil.detectRecursively(currJob, defaultInputPaths));
		currJob.setInputFormatClass(TextInputFormat.class);		
		//Configure Job Mapper
		currJob.setMapperClass(InnerMapper.class);
		currJob.setMapOutputKeyClass(Text.class);
		currJob.setMapOutputValueClass(Text.class);
		//Configure Job Reducer
		Integer reducerNumer = 1;
		currJob.setNumReduceTasks(reducerNumer);
		currJob.setReducerClass(InnerReducer.class);
		//Configure Output
		currJob.setOutputFormatClass(TextOutputFormat.class);
		String currTimeStamp = System.currentTimeMillis()+"";
		String defaultOutputPath = "/tmp/raw_archvie_process/history_at_user_split";
		TextOutputFormat.setOutputPath(currJob, new Path(defaultOutputPath+ "/" + currTimeStamp));
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
        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        	try {
	        	String line = StringUtils.trimToNull(value.toString());
	        	context.write(new Text(line), new Text());
        	} catch (Exception e) {
        		//do nothing...
        	} 
        }
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
