package com.yunrang.location.tasklet.mapreduce.wenjin;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import com.yunrang.location.common.util.UtilIpConverter;
import com.yunrang.location.tasklet.mapreduce.SingleMapReduceJobPlanBase;


final public class RmdDataPreprocessPlan extends SingleMapReduceJobPlanBase {
	
	public RmdDataPreprocessPlan() {
		this.jobOutputRootPath = SingleMapReduceJobPlanBase.MORPHEUS_ROOT;
		this.mapperClass = InnerMapper.class;
		this.reducerClass = InnerReducer.class;
		this.jobInputPath = "/chukwa/collector/output/20121026/030000";
	}
	
	public static class InnerMapper extends Mapper<LongWritable, Text, Text, Text> {
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			String line = value.toString();
			if (line == null || line.length() <= 0 || line.indexOf("resview:ResView(viewtype:RESVIEW") == -1) {
				return;
			}
			String ipStr = "";
			String resId = "";
			String actTp = "";
			String actCt = "";
			String userId= "";
			String[] lineTuple = line.split("\\|");
			try {
				for (int i = 0; i < lineTuple.length; i++) {
					String tuple = lineTuple[i];
					String[] keyValue = tuple.split(":");
					if (keyValue != null && keyValue.length == 2) {
						String viewKey = keyValue[0];
						String viewValue = keyValue[1];
						if (viewKey.trim().equalsIgnoreCase("ip")) {
							ipStr = UtilIpConverter.toLongValue(viewValue.trim()).toString();
						} else if (viewKey.trim().equalsIgnoreCase("userid")) {
						    userId = viewValue.trim();
						} else if (viewKey.trim().equalsIgnoreCase("resid")) {
							resId = viewValue.trim();
						} else if (viewKey.trim().equalsIgnoreCase("useractivetype")) {
							actTp = viewValue.trim();
						} else if (viewKey.trim().equalsIgnoreCase("actioncontent")){
							actCt = viewValue.trim();
						}
					}
				}
			} catch (Exception e) {
				return;
			}
			if (ipStr.length()==0 || resId.length()==0 || actTp.length()==0 || actCt.length()==0) {
				return;
			}
			//black list.....
			if (ipStr.equals("3722486962")) {
				return;
			}
			//20130606: only concern the not "null" records...
			if (!userId.equalsIgnoreCase("null")) {
			    String keyString = userId+","+resId;
	            String valueString = actTp+","+actCt;
	            context.write(new Text(keyString), new Text(valueString));
			}
		}
	}

	public static class InnerReducer extends Reducer<Text, Text, Text, LongWritable> {
		public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
			Iterator<Text> iterator = values.iterator();
			Map<String, Map<String, Long>> actionProfile = new HashMap<String, Map<String, Long>>();
			while (iterator.hasNext()) {
				String[] actTuple = iterator.next().toString().split(",");
				String actionType = actTuple[0];
				String actionContent = actTuple[1];
				if (actionProfile.containsKey(actionType)){
					Map<String, Long> contMap = actionProfile.get(actionType);
					if (contMap.containsKey(actionContent)) {
						Long cont = contMap.get(actionContent);
						cont += 1;
						contMap.put(actionContent, cont);
					} else {
						contMap.put(actionContent, 1L);
					}
				} else {
					Map<String, Long> contMap = new HashMap<String, Long>();
					contMap.put(actionContent, 1L);
					actionProfile.put(actionType, contMap);
				}
			}
			//##################### LEVEL ONE ##############
			// Search and see this books detail
			Long level_1_raw = 0L;
			if (actionProfile.containsKey("detail")) {
				for (String key_ : actionProfile.get("detail").keySet()) {
					level_1_raw += actionProfile.get("detail").get(key_);
				}
			}
			if (actionProfile.containsKey("extend")) {
				for (String key_ : actionProfile.get("extend").keySet()) {
					level_1_raw += actionProfile.get("extend").get(key_);
				}
			}
			if (actionProfile.containsKey("recommand")) {
				for (String key_ : actionProfile.get("recommand").keySet()) {
					level_1_raw += actionProfile.get("recommand").get(key_);
				}
			}
			//##################### LEVEL TWO ##############
			// Read or download This book
			Long level_2_raw = 0L;
			if (actionProfile.containsKey("docget")) {
				for (String key_ : actionProfile.get("docget").keySet()) {
					level_2_raw += actionProfile.get("docget").get(key_);
				}
			}
			//##################### LEVEL THREE ##############
			Long level_3_raw = 0L;
			if (actionProfile.containsKey("share")) {
				for (String key_ : actionProfile.get("share").keySet()) {
					level_3_raw += actionProfile.get("share").get(key_);
				}
			}
			//##################### LEVEL FOUR ##############
			//Check out this real book from library
			Long level_4_raw = 0L;
			if (actionProfile.containsKey("library")) {
				for (String key_ : actionProfile.get("library").keySet()) {
					level_4_raw += actionProfile.get("library").get(key_);
				}
			}
			//#################### COMPUTE LEVEL MARGINAL SCORE ##############
			Long amp_1 = 40L;
			Long amp_2 = 80L;
			Long amp_3 = 160L;
			Long amp_4 = 320L;
			Long level_1_marginal = marginalFunc(amp_1, level_1_raw);
			Long level_2_marginal = marginalFunc(amp_2, level_2_raw);
			Long level_3_marginal = marginalFunc(amp_3, level_3_raw);
			Long level_4_marginal = marginalFunc(amp_4, level_4_raw);
			Long marginal_score = level_1_marginal+level_2_marginal+level_3_marginal+level_4_marginal+1L;
			
			String reduceKey = key.toString();
			reduceKey += "," + marginal_score.toString();
			context.write(new Text(reduceKey), null);
		}
		
		private Long marginalFunc(Long amplitude, Long raw_score) {
			Double logistic = 1.0 / (1.0 + Math.exp(-1.0 * raw_score));
			Double marginalScore = amplitude * (logistic - 0.5);
			return marginalScore.longValue();
		}
	}
}