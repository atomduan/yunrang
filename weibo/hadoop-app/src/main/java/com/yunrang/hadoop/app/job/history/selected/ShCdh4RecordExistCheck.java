package com.yunrang.hadoop.app.job.history.selected;

import java.io.IOException;
import java.util.HashSet;
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
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import com.yunrang.hadoop.app.SingleMapReduceJobClient.JobSubmitContext;
import com.yunrang.hadoop.app.SingleMapReduceJobClient.SMRDefinition;
import com.yunrang.hadoop.app.common.config.HBaseWeiboColumns;
import com.yunrang.hadoop.app.utils.PathUtil;
import com.yunrang.hadoop.app.utils.SAWParser;

public class ShCdh4RecordExistCheck extends SMRDefinition {
	public void doConfiguration(JobSubmitContext jobSubmitContext, Job currJob) throws Exception {
		try {
			//Job queue config
			currJob.getConfiguration().set("mapreduce.job.queuename", "default");
			//Configure Input
			String defaultInputPaths = "/tmp/juntaoduan/history_selected_filter_dedup_1023";
			currJob.setInputFormatClass(TextInputFormat.class);
			TextInputFormat.setInputPaths(currJob, 
					PathUtil.detectRecursively(currJob, InnerPathFilter.class, defaultInputPaths));
			//Configure Job Mapper
			currJob.setMapperClass(InnerMapper.class);
			currJob.setMapOutputKeyClass(Text.class);
			currJob.setMapOutputValueClass(Text.class);
			//Configure Job Reducer
			currJob.setNumReduceTasks(SMRDefinition.SING_REDUCE_NUM);
			//Configure Output
			currJob.setOutputFormatClass(TextOutputFormat.class);
			String currTimeStamp = System.currentTimeMillis()+"";
			String defaultOutputPath = "/tmp/juntaoduan/history_simple_task/exsit_check";
			TextOutputFormat.setOutputPath(currJob, new Path(defaultOutputPath+ "/" + currTimeStamp));
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	public static class InnerPathFilter implements PathFilter {
		public boolean accept(Path path) {
			String urlStr = path.toUri().getPath().trim();
			if (urlStr.contains("part-")) {
//				return urlStr.contains("2012-12");
				return true;
			}
			return true;
		}
	}
	
	/**
	 * @QIHD:0
	 * @LURL:
	 * @URL:http://t.sina.com.cn/1828064537/BmlvUeKAl
	 * @DUP_CONT:3293578022
	 * @DUP_URL:-70306275
	 */
	public static class InnerMapper extends Mapper<LongWritable, Text, Text, Text> {
		private static String[] samples= new String[]{
			"zaNEK5gxI",
		};

		private Set<String> sampleSet = new HashSet<String>();
		private SAWParser parser;
		
		protected void setup(Context context) {
			final Context ctx = context;
			this.parser = SAWParser.getParser(new SAWParser.RecordHandler() {
				public void processOneRecord(Map<String, String> attrMap) {
					try {
						processOneRecordReceived(attrMap, ctx);
					} catch (Exception e) {}
				}
			});
			for (String s : samples) {
				this.sampleSet.add(s);
			}
		}
		
        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        	try {
	        	String line = StringUtils.trimToNull(value.toString());
	        	this.parser.offerLine(line);
        	} catch (Exception e) {} 
        }
        
        private void processOneRecordReceived(Map<String, String> attrMap, Context context) throws Exception {
        	String URL = attrMap.get(HBaseWeiboColumns.URL.getName());
        	if (URL.contains("zaAVGEtY9")) {
        		context.write(new Text(URL), new Text(SAWParser.linageRecorde(attrMap)));
        	}
		}
    }
	
	public static class InnerReducer extends Reducer<Text, Text, Text, Text> {
		@Override
		public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
			for (Text t : values) {
				context.write(new Text(key.toString()), new Text(t.toString()));
			}
		}
	}
	
    public static void main(String[] args) {
    	//41
    	String s = "http://t.sina.com.cn/1758900490/l4ES2bs4T/asdf";
    	System.out.println(s.subSequence(32, 41));
    }
}
