package com.yunrang.hadoop.app.job.history.selected;

import java.io.IOException;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.yunrang.hadoop.app.SingleMapReduceJobClient.JobSubmitContext;
import com.yunrang.hadoop.app.SingleMapReduceJobClient.SMRDefinition;
import com.yunrang.hadoop.app.common.config.HBaseWeiboColumns;
import com.yunrang.hadoop.app.common.input.combine.CombineRegionInputFormat;
import com.yunrang.hadoop.app.job.history.selected.filter.SelectedWeiboFilter;
import com.yunrang.hadoop.app.utils.JsonUtil;
import com.yunrang.hadoop.app.utils.WeiboUtil;

@SuppressWarnings("unchecked")
public class ShCdh4HBaseSamplingTask extends SMRDefinition {
	private static final String COLUMN_FAMILY = "cf";
	private String startTime;
	private String endTime;
	private String projectPrefix;
	private String hsql;
	
	protected Configuration getConf(Configuration defaultConfig) {
		return HBaseConfiguration.create();
	}

	public void doConfiguration(JobSubmitContext jobSubmitContext, Job currJob) throws Exception {
		try {
			parseFromPayLoad(jobSubmitContext);
			String hsql = this.hsql;
			// Job config params setting
			currJob.getConfiguration().set("mapreduce.job.queuename", "default");
			currJob.getConfiguration().set(CombineRegionInputFormat.HSQL_CONFIG, hsql);
			currJob.getConfiguration().setInt(CombineRegionInputFormat.CRIF_MAP_NUM_CONF, 400);
			// Configure Input
			currJob.setInputFormatClass(CombineRegionInputFormat.class);
			// Configure Job Mapper
			currJob.setMapperClass(InnerMapper.class);
			currJob.setMapOutputKeyClass(Text.class);
			currJob.setMapOutputValueClass(Text.class);
			// Configure Job Combiner
			currJob.setCombinerClass(InnerCombiner.class);
			// Configure Job Reducer
			currJob.setNumReduceTasks(SMRDefinition.DEFAULT_REDUCE_NUM);
			// Configure Output
			currJob.setSpeculativeExecution(false);
			currJob.setReducerClass(InnerReducer.class);
			currJob.setOutputFormatClass(TextOutputFormat.class);
			String defaultOutputPath = this.projectPrefix;
			TextOutputFormat.setOutputPath(currJob, new Path(defaultOutputPath));
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	private void parseFromPayLoad(JobSubmitContext jobSubmitContext) {
		String payLoad = StringUtils.trimToNull(jobSubmitContext.getPayLoad());
		String[] params = payLoad.split(DEFAULT_PAYLOAD_SEP);
		this.startTime = StringUtils.trimToNull(params[0]).trim();
		this.endTime = StringUtils.trimToNull(params[1]).trim();
		String px = StringUtils.trimToNull(params[2]).trim();
		this.projectPrefix = px.endsWith("/") ? px.substring(0, px.length()-1) : px;
		this.hsql = "select " + COLUMN_FAMILY + ":*" + " from weibo where " + "datetime>=" + startTime +" and datetime<=" + endTime;
	}
	
	public static class InnerMapper extends TableMapper<Text, Text> {
		private SelectedWeiboFilter selectedFilter = new SelectedWeiboFilter();
		
		@Override
		protected void map(ImmutableBytesWritable key, Result value, Context context) throws IOException, InterruptedException {
			try {
				JSONObject record = JsonUtil.parse(key, value, COLUMN_FAMILY);
				if (selectedFilter.accept(record)) {
					String outputKey = null;
					Iterator<String> keys = record.keys();
					StringBuilder sb = new StringBuilder();
					String contSign = null;
					String idStr = null;
					sb.append("@");
					while (keys.hasNext()) {
						String colCode = keys.next();
						String colValue = record.getString(colCode);
						String colName = HBaseWeiboColumns.getName(colCode);
						if (StringUtils.trimToNull(colName) != null) {
							if (HBaseWeiboColumns.CONT_SIGN.codeEquals(colCode)) {
								contSign = StringUtils.trimToNull(colValue);
							}
							if (HBaseWeiboColumns.ID.codeEquals(colCode)) {
								idStr = StringUtils.trimToNull(colValue);
							}
							sb.append(DEFAULT_FIELD_SEP).append("@").append(colName).append(":").append(colValue);
						}
					}
					if (idStr == null) {
						return;
					}
					Integer fwnum = WeiboUtil.getIntField(record, HBaseWeiboColumns.FWNUM);
					Integer validFwn = WeiboUtil.getIntField(record, HBaseWeiboColumns.VALIDFWNM);
					validFwn  = (fwnum * validFwn) / 1000;
					if (validFwn < 10) {
						outputKey = contSign != null ? contSign : idStr;
					} else {
						outputKey = idStr;
					}
					context.write(new Text(outputKey), new Text(sb.toString()));
				}
			} catch (JSONException e) {throw new RuntimeException(e);}
		}
	}
	
	public static class InnerCombiner extends Reducer<Text, Text, Text, Text> {
		@Override
		public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
			for (Text t : values) {
				String tv = t.toString();
				context.write(new Text(key.toString()), new Text(tv));
				break;
			}
		}
	}
	
	public static class InnerReducer extends Reducer<Text, Text, Text, Text> {
		@Override
		public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
			for (Text t : values) {
				String tv = t.toString();
				String[] vs = tv.split(DEFAULT_FIELD_SEP);
				for (String s : vs) {
					context.write(new Text(s), null);
				}
				break;
			}
		}
	}
}