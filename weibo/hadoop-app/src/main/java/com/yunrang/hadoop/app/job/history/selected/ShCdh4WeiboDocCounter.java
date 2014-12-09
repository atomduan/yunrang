package com.yunrang.hadoop.app.job.history.selected;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

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
import com.yunrang.hadoop.app.utils.WeiboUtil;

public class ShCdh4WeiboDocCounter extends SMRDefinition {
	private static String filterIndecator = "";
	private static String inputPath = "";

	public void doConfiguration(JobSubmitContext jobSubmitContext, Job currJob) throws Exception {
		try {
			//Job queue config
			currJob.getConfiguration().set("mapreduce.job.queuename", "default");
			parseFromPayLoad(jobSubmitContext);
			//Configure Input
			String defaultInputPaths = inputPath;
			currJob.setInputFormatClass(TextInputFormat.class);
			TextInputFormat.setInputPaths(currJob, PathUtil.detectRecursively(currJob, InnerPathFilter.class, defaultInputPaths));
			//Configure Job Mapper
			currJob.setMapperClass(InnerMapper.class);
			currJob.setMapOutputKeyClass(Text.class);
			currJob.setMapOutputValueClass(Text.class);
			//Configure Job Reducer
			currJob.setReducerClass(InnerReducer.class);
			currJob.setNumReduceTasks(SMRDefinition.SING_REDUCE_NUM);
			
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
	private void parseFromPayLoad(JobSubmitContext jobSubmitContext) {
		String payLoad = StringUtils.trimToNull(jobSubmitContext.getPayLoad());
		String[] params = payLoad.split(DEFAULT_PAYLOAD_SEP);
		filterIndecator = StringUtils.trimToNull(params[0]);
		String px = StringUtils.trimToNull(params[1]).trim();
		inputPath = px.endsWith("/") ? px.substring(0, px.length()-1) : px;
	}
	
	public static class InnerPathFilter implements PathFilter {
		public boolean accept(Path path) {
			String urlStr = path.toUri().getPath().trim();
			if (urlStr.contains("part-")) {
				if (StringUtils.trimToNull(filterIndecator) != null) {
					return urlStr.contains(filterIndecator);
				}
			}
			return true;
		}
	}
	
	public static class InnerMapper extends Mapper<LongWritable, Text, Text, Text> {
		private AtomicLong count = new AtomicLong(0);
        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        	try {
	        	String line = StringUtils.trimToNull(value.toString());
	        	if (line.startsWith("@ID:")) {
	        		count.getAndIncrement();
	        	}
        	} catch (Exception e) {
        	} 
        }
        protected void cleanup(Context context) throws IOException, InterruptedException {
        	context.write(new Text(count.toString()), new Text(count.toString()));
        }
    }
	
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
