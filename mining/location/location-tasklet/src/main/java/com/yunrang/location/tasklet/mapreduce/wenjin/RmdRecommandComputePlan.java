package com.yunrang.location.tasklet.mapreduce.wenjin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yunrang.location.common.util.map.SimpleHashMap;
import com.yunrang.location.common.util.map.SimpleMap;
import com.yunrang.location.tasklet.mapreduce.SingleMapReduceJobPlanBase;


public class RmdRecommandComputePlan extends SingleMapReduceJobPlanBase {
	static Logger LOG = LoggerFactory.getLogger(RmdRecommandComputePlan.class);
	
	public RmdRecommandComputePlan() {
		this.jobOutputRootPath = SingleMapReduceJobPlanBase.MORPHEUS_ROOT;
		this.mapperClass = InnerMapper.class;
		this.reducerClass = InnerReducer.class;
		this.jobInputPath = "/chukwa/collector/output";
	}
	
	public static class InnerMapper extends Mapper<LongWritable, Text, Text, Text> {
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			context.write(value, value);
		}
	}

	public static class InnerReducer extends Reducer<Text, Text, Text, LongWritable> {
		static final String MARGINAL_RECOMMAND_MAP_FILE = "/tmp/juntaoduan/marginal-recommand/part-r-00000";
		static final String MARGINAL_RECOMMAND_REVERSE_MAP_FILE = "/tmp/juntaoduan/marginal-recommand-reverse/part-r-00000";	
		static final String SEPERATOR = ",";
		static SimpleMap marginalRecommandMap;
		static SimpleMap marginalRecommandReverseMap;
		
		protected void setup(Context context) {
			try {
				BufferedReader getMapFileReader = null;
				BufferedReader getReverseMapFileReader = null;
				try {
					getMapFileReader = getMapFileReader(context);
					getReverseMapFileReader = getReverseMapFileReader(context);
					initializeUtilMap(getMapFileReader, getReverseMapFileReader);
				} finally {
					try {
						getMapFileReader.close();
					} finally {
						getReverseMapFileReader.close();
					}
				}
			} catch (Exception e) {
				LOG.error("fatal error on setup", e);
			}
		}
		
		public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
			context.write(key, null);
		}
		
		void contextWrite(Context context, Text key, LongWritable value) throws Exception {
			context.write(key, value);
		}
		
		BufferedReader getMapFileReader(Context context) throws Exception {
			Configuration config = context.getConfiguration();
			Path path = new Path(MARGINAL_RECOMMAND_MAP_FILE);
			FileSystem fs = FileSystem.get(path.toUri(), config);
			return new BufferedReader(new InputStreamReader(fs.open(path)));
		}
		
		BufferedReader getReverseMapFileReader(Context context) throws Exception {
			Configuration config = context.getConfiguration();
			Path path = new Path(MARGINAL_RECOMMAND_REVERSE_MAP_FILE);
			FileSystem fs = FileSystem.get(path.toUri(), config);
			return new BufferedReader(new InputStreamReader(fs.open(path)));
		}
		
		SimpleMap getRecommandUtilMap() throws Exception {
			return new SimpleHashMap();
		}
		
		void initializeUtilMap(BufferedReader mapFileReader, BufferedReader reverseMapFileReader) throws Exception {
			marginalRecommandMap = getRecommandUtilMap();
			marginalRecommandReverseMap = getRecommandUtilMap();
			initMarginalMap(marginalRecommandMap, mapFileReader);
			initMarginalMap(marginalRecommandReverseMap, reverseMapFileReader);
		}
		
		void initMarginalMap(SimpleMap map, BufferedReader reader) throws Exception {
			String lastKey = null;
			List<String> pkg = null;
			Long count = 0L;
			while (true) {
				String line = reader.readLine();
				if (line == null) {
					//post process the final one, Don't forget it , is tricky~!
					if (lastKey != null && pkg != null) {
						map.put(lastKey, pkg);
					}
					break;
				}
				String[] values = line.split(SEPERATOR);
				if (!values[0].equals(lastKey)) {
					//process the last one
					if (lastKey != null && pkg != null) {
						map.put(lastKey, pkg);
					}
					pkg = new ArrayList<String>();
				}
				pkg.add(line);
				lastKey = values[0];
				count++;
			}
		}
	}
}
