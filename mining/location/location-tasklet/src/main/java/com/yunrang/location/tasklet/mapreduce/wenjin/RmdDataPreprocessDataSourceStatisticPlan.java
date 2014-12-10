package com.yunrang.location.tasklet.mapreduce.wenjin;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import com.yunrang.location.tasklet.mapreduce.SingleMapReduceJobPlanBase;

final public class RmdDataPreprocessDataSourceStatisticPlan extends SingleMapReduceJobPlanBase {
	
	public RmdDataPreprocessDataSourceStatisticPlan() {
		this.jobOutputRootPath = SingleMapReduceJobPlanBase.MORPHEUS_ROOT;
		this.mapperClass = InnerMapper.class;
		this.reducerClass = InnerReducer.class;
		this.jobInputPath = "/chukwa/collector/output/20121026";
	}

	public static class InnerMapper extends Mapper<LongWritable, Text, Text, Text> {
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			String line = value.toString();
			String dataSource = "";
			if (line.contains("actioncontent:在线阅读")) {
				String[] lineTuple = line.split("\\|");
				try {
					for (int i = 0; i < lineTuple.length; i++) {
						String tuple = lineTuple[i];
						String[] keyValue = tuple.split(":");
						if (keyValue != null && keyValue.length == 2) {
							String viewKey = keyValue[0];
							String viewValue = keyValue[1];
							if (viewKey.trim().equalsIgnoreCase("datasource")) {
								dataSource = viewValue.trim();
							}
						}
					}
				} catch (Exception e) {
					return;
				}
			}
			context.write(new Text(dataSource), new Text());
		}
	}

	public static class InnerReducer extends Reducer<Text, Text, Text, LongWritable> {
		public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
			String dataSource = key.toString();
			Integer dataSourceCount = 0;
			while (values.iterator().hasNext()) {
				dataSourceCount++;
				values.iterator().next();
			}
			context.write(new Text(dataSource), new LongWritable(dataSourceCount));
		}
	}
}
