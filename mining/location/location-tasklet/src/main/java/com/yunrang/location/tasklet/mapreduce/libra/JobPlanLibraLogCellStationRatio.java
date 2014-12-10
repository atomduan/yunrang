package com.yunrang.location.tasklet.mapreduce.libra;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import com.yunrang.location.common.bean.ProfileCellStation;
import com.yunrang.location.tasklet.mapreduce.SingleMapReduceJobPlanBase;
import com.yunrang.location.tasklet.mapreduce.libra.filter.LibralogHdfsPathFilterStatisticAdvlog;
import com.yunrang.location.tasklet.mapreduce.libra.parser.LibraLogRecordParser;

public class JobPlanLibraLogCellStationRatio extends SingleMapReduceJobPlanBase {
	
	public JobPlanLibraLogCellStationRatio() {
		this.jobOutputRootPath = SingleMapReduceJobPlanBase.MORPHEUS_ROOT;
		this.mapperClass = InnerMapper.class;
		this.reducerClass = InnerReducer.class;
		this.jobInputPath = "/production/ads/log/libra/";
		this.inputFilePathFilter = new LibralogHdfsPathFilterStatisticAdvlog("2013/08/12", "2013/08/31");
	}
	
	public static class InnerMapper extends Mapper<LongWritable, Text, Text, Text> {
		private AtomicLong totalCount = new AtomicLong(0);
	    private AtomicLong availableCount = new AtomicLong(0);
        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            totalCount.incrementAndGet();
            try {
        		String line = value.toString();
        		ProfileCellStation profileCellStation = LibraLogRecordParser.parseProfileCellStation(line);
        		if (!profileCellStation.isEmpty()) {
        			availableCount.incrementAndGet();
        		}
        	} catch (Exception ignore) {}
        }
        public void cleanup(Context context) {
            try {
                String keyStr = hashCode()+";"+totalCount.toString()+";"+availableCount.toString();
                context.write(new Text(keyStr), new Text(""));
            } catch (Exception ignore) {
                //......
            }
        }
	}

	public static class InnerReducer extends Reducer<Text, Text, Text, LongWritable> {
        AtomicLong totalCount = new AtomicLong(0);
        AtomicLong totalAvailableCount = new AtomicLong(0);
        public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            String[] result = key.toString().split(";");
            Long count = Long.parseLong(result[1]);
            Long availableCount = Long.parseLong(result[2]);
            totalCount.addAndGet(count);
            totalAvailableCount.addAndGet(availableCount);
        }
        public void cleanup(Context context) {
            try {
                context.write(new Text(totalCount+";"+totalAvailableCount), null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
	}
}
