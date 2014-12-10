package com.yunrang.location.tasklet.mapreduce.wenjin;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import com.yunrang.location.common.util.UtilMurmurHash;
import com.yunrang.location.tasklet.mapreduce.SingleMapReduceJobPlanBase;


public class RmdUserIdToLongHashMapPlan extends SingleMapReduceJobPlanBase {
    
	public RmdUserIdToLongHashMapPlan() {
		this.jobOutputRootPath = SingleMapReduceJobPlanBase.MORPHEUS_ROOT;
		this.mapperClass = InnerMapper.class;
		this.reducerClass = InnerReducer.class;
		this.jobInputPath = "/chukwa/collector/output";
	}
    
    public static class InnerMapper extends Mapper<LongWritable, Text, Text, Text> {
        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            String[] params = line.trim().split(",");
            String userId = params[0];
            long userHashId = UtilMurmurHash.hash64(userId);
            context.write(new Text(userId+","+userHashId), new Text(""));
        }
    }

    public static class InnerReducer extends Reducer<Text, Text, Text, LongWritable> {
        public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            String reduceKey = key.toString();
            context.write(new Text(reduceKey), null);
        }
    }
}
