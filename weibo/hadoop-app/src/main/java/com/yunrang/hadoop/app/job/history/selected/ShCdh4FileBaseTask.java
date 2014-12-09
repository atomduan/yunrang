package com.yunrang.hadoop.app.job.history.selected;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import com.yunrang.hadoop.app.SingleMapReduceJobClient.JobSubmitContext;
import com.yunrang.hadoop.app.SingleMapReduceJobClient.SMRDefinition;
import com.yunrang.hadoop.app.common.input.NonSplittableTextInputFormat;
import com.yunrang.hadoop.app.utils.PathUtil;

public class ShCdh4FileBaseTask extends SMRDefinition {
	
	public void doConfiguration(JobSubmitContext jobSubmitContext, Job currJob) throws Exception {
		try {
			//Job queue config
			currJob.getConfiguration().set("mapreduce.job.queuename", "default");
			//Configure Input
			String defaultInputPaths = "/tmp/juntaoduan/history_data_shard9_part-02369.processed";
			currJob.setInputFormatClass(NonSplittableTextInputFormat.class);
			NonSplittableTextInputFormat.setInputPaths(currJob, PathUtil.detectRecursively(currJob, defaultInputPaths));
			//Configure Job Mapper
			currJob.setMapperClass(InnerMapper.class);
			currJob.setMapOutputKeyClass(Text.class);
			currJob.setMapOutputValueClass(Text.class);
			//Configure Job Reducer
			currJob.setNumReduceTasks(SMRDefinition.ZERO_REDUCE_NUM);
			//Configure Output
			currJob.setOutputFormatClass(TextOutputFormat.class);
			String currTimeStamp = System.currentTimeMillis()+"";
			String defaultOutputPath = "/tmp/juntaoduan/history_simple_task";
			TextOutputFormat.setOutputPath(currJob, new Path(defaultOutputPath+ "/" + currTimeStamp));
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	public static class InnerMapper extends Mapper<LongWritable, Text, Text, Text> {
        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        	try {
	        	String line = StringUtils.trimToNull(value.toString());
	        	if (line.startsWith("@CONT_SIGN")) {
	        		context.write(new Text(line), null);
	        		context.write(new Text("@TIME:2012-06-11 19:15:51"), null);
	        	} else {
	        		context.write(new Text(line), null);
	        	}
        	} catch (Exception e) {
        	} 
        }
    }
}
