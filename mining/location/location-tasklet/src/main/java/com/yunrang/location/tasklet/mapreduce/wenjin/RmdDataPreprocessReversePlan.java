package com.yunrang.location.tasklet.mapreduce.wenjin;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import com.yunrang.location.tasklet.mapreduce.SingleMapReduceJobPlanBase;

final public class RmdDataPreprocessReversePlan extends SingleMapReduceJobPlanBase {
	
	public RmdDataPreprocessReversePlan() {
		this.jobOutputRootPath = SingleMapReduceJobPlanBase.MORPHEUS_ROOT;
		this.mapperClass = InnerMapper.class;
		this.reducerClass = InnerReducer.class;
		this.jobInputPath = "/chukwa/collector/output";
	}
	
	public static class InnerMapper extends Mapper<LongWritable, Text, Text, Text> {
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			String[] values = value.toString().split(",");
			String userID = values[0];
			String itemID = values[1];
			String score = values[2];
			context.write(new Text(itemID+","+userID+","+score), value);
		}
	}

	public static class InnerReducer extends Reducer<Text, Text, Text, LongWritable> {
		public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
			context.write(key, null);
		}
	}
}