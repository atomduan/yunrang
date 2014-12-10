package com.yunrang.location.tasklet.mapreduce.libra;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import com.yunrang.location.common.bean.ProfileCellStation;
import com.yunrang.location.tasklet.mapreduce.SingleMapReduceJobPlanBase;
import com.yunrang.location.tasklet.mapreduce.libra.filter.LibralogHdfsPathFilterStatisticAdvlog;
import com.yunrang.location.tasklet.mapreduce.libra.parser.LibraLogRecordParser;

public class JobPlanLibraLogIpWithCellStation extends SingleMapReduceJobPlanBase {
	
	public JobPlanLibraLogIpWithCellStation() {
		this.jobOutputRootPath = SingleMapReduceJobPlanBase.MORPHEUS_ROOT;
		this.mapperClass = InnerMapper.class;
		this.reducerClass = InnerReducer.class;
		this.jobInputPath = "/production/ads/log/libra/";
		this.inputFilePathFilter = new LibralogHdfsPathFilterStatisticAdvlog("2013/08/12", "2013/08/31");
	}

	public static class InnerMapper extends Mapper<LongWritable, Text, Text, Text> {
        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        	String line = value.toString();
        	String resultLine = null;
        	try {
        		ProfileCellStation profileCellStation = LibraLogRecordParser.parseProfileCellStation(line);
        		if (!profileCellStation.isEmpty()) {
        			String gateWayIp = LibraLogRecordParser.extractGatewayIp(line);
            		resultLine = profileCellStation.getOpt()+"#"+
            							profileCellStation.getMnc()+"#"+
            							profileCellStation.getMcc()+"#"+
            							profileCellStation.getLac()+"#"+
            							profileCellStation.getCid()+"#"+gateWayIp;
            		
        		}
        	} catch (Exception ignore) {}	
        	if (resultLine != null) {
        		context.write(new Text(resultLine), new Text());
        	}
        }
    }

	public static class InnerReducer extends Reducer<Text, Text, Text, LongWritable> {
        public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            context.write(key, null);
        }
	}
}
