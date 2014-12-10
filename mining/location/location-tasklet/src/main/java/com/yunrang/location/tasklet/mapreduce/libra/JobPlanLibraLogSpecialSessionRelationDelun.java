package com.yunrang.location.tasklet.mapreduce.libra;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import com.yunrang.location.common.util.UtilMurmurHash;
import com.yunrang.location.tasklet.mapreduce.SingleMapReduceJobPlanBase;
import com.yunrang.location.tasklet.mapreduce.libra.filter.LibralogHdfsPathFilterStatisticAdvlog;
import com.yunrang.location.tasklet.mapreduce.libra.parser.LibraLogRecordParser;


public class JobPlanLibraLogSpecialSessionRelationDelun extends SingleMapReduceJobPlanBase {
	
	public JobPlanLibraLogSpecialSessionRelationDelun() {
		this.jobOutputRootPath = SingleMapReduceJobPlanBase.MORPHEUS_ROOT;
		this.mapperClass = InnerMapper.class;
		this.reducerClass = InnerReducer.class;
		this.jobInputPath = "/production/ads/log/libra/";
		this.inputFilePathFilter = new LibralogHdfsPathFilterStatisticAdvlog("2013/08/01", "2013/08/31");
	}
	
	public static class InnerMapper extends Mapper<LongWritable, Text, Text, Text> {
        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        	String line = value.toString();
        	String resultLine = null;
        	try {
        		String sessionId = LibraLogRecordParser.extractSessionId(line);
        		if (isInSessionIdTable(sessionId)) {
        			String gateWayIp = LibraLogRecordParser.extractGatewayIp(line);
        			String appId = LibraLogRecordParser.extractAppId(line);
        			String requestTime = LibraLogRecordParser.extractRequestTime(line);
        			resultLine = sessionId+","+gateWayIp+","+appId+","+requestTime+","+UtilMurmurHash.hash64(line);
        		}
        	} catch (Exception ignore) {}
        	if (resultLine != null) {
        		context.write(new Text(resultLine), new Text());
        	}
        }
        private boolean isInSessionIdTable(String sessionId) {
        	for (String s : sessionIdTable) {
        		if (s.contains(sessionId)) {
        			return true;
        		}
        	}
        	return false;
        }
    }

	public static class InnerReducer extends Reducer<Text, Text, Text, LongWritable> {
        public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        	String[] params = key.toString().split(",");
            context.write(new Text(params[0]+","+params[1]+","+params[2]+","+params[3]), null);
        }
	}
	
	private static String[] sessionIdTable = new String[] {
		"FBCB31D827743E6D3E27C06D3F3BEE75",
		"AF61B8E534A5F16AC773F57FC018E251",
		"C49EBDDE5E07D4D8994DC99D40C0D51D",
		"1DE9F859285CDE7F44F445769E6E853C",
		"0E6157C48ABD42C4BC8E85D19F980657",
		"3F21497B13794F4643A90BAF6BC9C287",
		"8324645CE2EE7E4715B7E3737EB25ACC",
		"8D3225CFECF93FADE990D6D1FE204DA8",
		"061EC226CB3423C295B9AC54333D0832",
		"F36AAC4C193620C0E8411F7DAD673AC4",
		"EF4402D4F8BA83116EF2AC794E910F8C",
		"B22625DBAFD2FBABB96A209B24C41C5A",
		"36F64C02D4317A20D3AB745782AC6F8F",
		"FDB84207110D368C9EEE2DC6714A74A3",
		"7372C996F01ACC642495542D30E16006",
		"0464922E4E18042538417F084B8F2AFD",
		"01D474FD1757BB87A9934981F2D7D9CE",
		"01D474FD1757BB87A9934981F2D7D9CE",
		"01D474FD1757BB87A9934981F2D7D9CE",
		"519E0213D8F5092DDD9A2A2F79C41E2B",
		"6D7B90A7979042E52FBCCE98B8B22EB9",
		"8DB93B2456B1C2912C918830EB9AD6EF",
		"75ECE9E4BEA5F54CEE9C795D9A1C259B",
		"05468420A819F19BD7AF86183EDBBD4A",
		"05468420A819F19BD7AF86183EDBBD4A",
		"CF2AF38364FA54318AA599F44EFD4C5B",
		"75ECE9E4BEA5F54CEE9C795D9A1C259B",
		"05468420A819F19BD7AF86183EDBBD4A",
		"8D8ACB96797BA00344168F01493DA897",
		"5A2ECE1E794D564A712872E92B317AEF",
		"630A40C8D142D849DBBA006E8F62121A",
		"C42D170E0168D34CF9CCD7E175417B51",
		"FD8F655FD4CB2A921BC77D469454996F",
		"FAA26F9C98CD779B802A13C9D3B30342",
		"DAD8590A91C9C06EDEA435B009033A8C",
		"62B4354506696862AD50086783184C43",
		"4DA893A79161E447CE305B0EE6F3703C",
		"C4EAA62789781622E50590580F8374B6",
		"9A27E147E762465F07F853D6D5B00CDA",
		"05D6C71055314653C05E1717E027DB04",
		"116C66A3DDB39B4DB153CF7CFF0BFD26",
		"116C66A3DDB39B4DB153CF7CFF0BFD26",
		"116C66A3DDB39B4DB153CF7CFF0BFD26",
		"116C66A3DDB39B4DB153CF7CFF0BFD26",
		"116C66A3DDB39B4DB153CF7CFF0BFD26",
		"116C66A3DDB39B4DB153CF7CFF0BFD26",
		"116C66A3DDB39B4DB153CF7CFF0BFD26",
		"05D6C71055314653C05E1717E027DB04",
		"116C66A3DDB39B4DB153CF7CFF0BFD26",
		"116C66A3DDB39B4DB153CF7CFF0BFD26",
		"116C66A3DDB39B4DB153CF7CFF0BFD26",
		"116C66A3DDB39B4DB153CF7CFF0BFD26",
		"CA10BD523DF3B36121DFFAED5BD7541D",
		"F0BCC51E421EC6361324F88FCCF7AA78",
		"116C66A3DDB39B4DB153CF7CFF0BFD26",
		"67AD82B94525433F4F5A3FF85A149A2E",
		"ED96BAFCCD34D0BA2B1AD5F624D259A7",
		"43AF97E2505D68DE1D67272D220F820B",
		"50BC3A59FBACD2D63A5AE676F3ABC3C3",
	};
}
