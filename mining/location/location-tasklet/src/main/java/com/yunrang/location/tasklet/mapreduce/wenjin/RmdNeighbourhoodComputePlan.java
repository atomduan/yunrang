package com.yunrang.location.tasklet.mapreduce.wenjin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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


public class RmdNeighbourhoodComputePlan extends SingleMapReduceJobPlanBase {
	static Logger LOG = LoggerFactory.getLogger(RmdNeighbourhoodComputePlan.class);

	public RmdNeighbourhoodComputePlan() {
		this.jobOutputRootPath = SingleMapReduceJobPlanBase.MORPHEUS_ROOT;
		this.mapperClass = InnerMapper.class;
		this.reducerClass = InnerReducer.class;
		this.jobInputPath = "/chukwa/collector/output";
	}
	
	static class InnerMapper extends Mapper<LongWritable, Text, Text, Text> {
		
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			String[] values = value.toString().split(",");
			String userID = values[0];
			context.write(new Text(userID), value);
		}
	}
	
	static class InnerReducer extends Reducer<Text, Text, Text, LongWritable> {
		
		
		static final String MARGINAL_RECOMMAND_MAP_FILE = "/tmp/juntaoduan/marginal-recommand/part-r-00000";
		static final String MARGINAL_RECOMMAND_REVERSE_MAP_FILE = "/tmp/juntaoduan/marginal-recommand-reverse/part-r-00000";	
		static final String SEPERATOR = ",";
		
		static final Integer NEIGHBOR_COUNT = 10;
		
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
		
		protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
			try {
				String hostID = key.toString().trim();
				String neighourIDs = computeNeighbors(hostID);
				contextWrite(context, new Text(hostID+","+neighourIDs), null);
			} catch (Exception e) {
				throw new IOException(e);
			} 
		}
		
		protected void cleanup(Context context) {
			try {
				try {
					marginalRecommandMap.clear();
				} finally {
					marginalRecommandReverseMap.clear();
				}
			} catch (Exception e) {
				LOG.error("fatal error on cleanup", e);
			}
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
		
		String computeNeighbors(String hostID) throws Exception {
			LinkedList<String[]> neighbours = new LinkedList<String[]>();
			Set<String> eveluatedCandidates = new HashSet<String>();
			//[0]:userId [1]:itemId [2]:score
			List<String> hostPerfers = marginalRecommandMap.get(hostID);
			if (hostPerfers != null && hostPerfers.size()<100) {
				for (String prefer : hostPerfers) {
					String itemID = prefer.split(SEPERATOR)[1];
					//[0]:itemId [1]:userId [2]:score
					List<String> itemRelates = marginalRecommandReverseMap.get(itemID);
					if (itemRelates != null && itemRelates.size()<500) {
						for (String itemRelate :  itemRelates) {
							String candidateID = itemRelate.split(SEPERATOR)[1];
							// candidate should not be host itself
							if (!candidateID.equals(hostID) && !eveluatedCandidates.contains(candidateID)) {
								List<String> candidatePrefers = marginalRecommandMap.get(candidateID);
								if (candidatePrefers != null) {
									evaluateNeighbourRelation(hostPerfers, candidatePrefers, neighbours);
									eveluatedCandidates.add(candidateID);
								}
							}
						}
					}
				}
			}
			String neighorStr = "";
			for (int i=0 ; i<neighbours.size(); i++) {
				String[] neighbour = neighbours.get(i);
				if (i == 0) {
					neighorStr = "["+neighbour[0]+","+neighbour[1]+"]";
				} else {
					neighorStr += ",["+neighbour[0]+","+neighbour[1]+"]";
				}
			}
			return "{"+neighorStr+"}";
		}
		
		void evaluateNeighbourRelation(List<String> hostPerfers, List<String> candidatePrefers, LinkedList<String[]> neibours) {
			Double dist = computeNeighborDistance(hostPerfers, candidatePrefers);
			String candidateID = candidatePrefers.get(0).split(SEPERATOR)[0];
			if (dist != null) {
				for (int i=0; i<neibours.size(); i++) {
					Double comparDist = Double.parseDouble(neibours.get(i)[1]);
					if (dist <= comparDist) {
						if (neibours.size()>NEIGHBOR_COUNT) {
							neibours.pollLast();
						}
						neibours.add(i, new String[]{candidateID, dist.toString()});
						return;
					} 
				}
				if (neibours.size()<NEIGHBOR_COUNT) {
					neibours.add(new String[]{candidateID, dist.toString()});
					return;
				}
			}
		}
		
		Double computeNeighborDistance(List<String> hostPrefers, List<String> candidatePrefers) {
			Integer identCount = 0;
			Integer totalEffect = 0;
			for(String hostPStr : hostPrefers) {
				String[] hostPref = hostPStr.split(SEPERATOR);
				String hostItemId = hostPref[1];
				Integer hostItemScore = Integer.parseInt(hostPref[2]); 
				for (String candidatePStr : candidatePrefers) {
					String[] candidatePref = candidatePStr.split(SEPERATOR);
					String candidateItemId = candidatePref[1];
					Integer candidateItemScore = Integer.parseInt(candidatePref[2]);
					if (hostItemId.equals(candidateItemId)) {
						identCount += 1;
						totalEffect += (hostItemScore - candidateItemScore);
					}
				}
			}
			Double hostCoverRate = (identCount*1.0) / (hostPrefers.size()*1.0);
			// Note : [hostCoverRate>=0.3 && candidateCoverRate>=0.3]--> bad case, both have few book to share.
			// 0.0 --> to many 10 score and only one item
			// big.0 --> only one big be shared
			if (hostCoverRate>=0.3) {
				Double distance = (totalEffect*1.0) / (identCount*1.0);
				return Math.abs(distance);
			} else {
				return null;
			}
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
