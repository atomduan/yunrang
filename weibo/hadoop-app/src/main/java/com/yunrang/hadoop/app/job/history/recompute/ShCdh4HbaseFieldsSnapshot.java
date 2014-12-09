package com.yunrang.hadoop.app.job.history.recompute;

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
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.yunrang.hadoop.app.SingleMapReduceJobClient.JobSubmitContext;
import com.yunrang.hadoop.app.SingleMapReduceJobClient.SMRDefinition;
import com.yunrang.hadoop.app.common.config.HBaseWeiboColumns;
import com.yunrang.hadoop.app.common.filter.RecordFilter;
import com.yunrang.hadoop.app.common.input.combine.CombineRegionInputFormat;
import com.yunrang.hadoop.app.utils.JsonUtil;


@SuppressWarnings("unchecked")
public class ShCdh4HbaseFieldsSnapshot extends SMRDefinition {
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
			currJob.getConfiguration().setInt(CombineRegionInputFormat.CRIF_MAP_NUM_CONF, 800);
			// Configure Input
			currJob.setInputFormatClass(CombineRegionInputFormat.class);
			// Configure Job Mapper
			currJob.setMapperClass(InnerMapper.class);
			currJob.setMapOutputKeyClass(Text.class);
			currJob.setMapOutputValueClass(Text.class);
			// Configure Output
			currJob.setNumReduceTasks(SMRDefinition.ZERO_REDUCE_NUM);
			currJob.setSpeculativeExecution(false);
			currJob.setOutputFormatClass(TextOutputFormat.class);
			String defaultOutputPath = this.projectPrefix;
			TextOutputFormat.setOutputPath(currJob, new Path(defaultOutputPath));
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	String qiIndex = HBaseWeiboColumns.QI.getCode();
	String midIndex = HBaseWeiboColumns.ID.getCode();
	String timeIndex = HBaseWeiboColumns.TIME.getCode();
	String topicIndex = HBaseWeiboColumns.TOPIC_WORDS.getCode();
	String nontopicIndex = HBaseWeiboColumns.NON_TOPIC_WORDS.getCode();
	String usertextIndex = HBaseWeiboColumns.USERTEXT.getCode();
	
	private void parseFromPayLoad(JobSubmitContext jobSubmitContext) {
		String payLoad = StringUtils.trimToNull(jobSubmitContext.getPayLoad());
		String[] params = payLoad.split(DEFAULT_PAYLOAD_SEP);
		this.startTime = StringUtils.trimToNull(params[0]).trim();
		this.endTime = StringUtils.trimToNull(params[1]).trim();
		String px = StringUtils.trimToNull(params[2]).trim();
		this.projectPrefix = px.endsWith("/") ? px.substring(0, px.length()-1) : px;
		this.hsql = "select " 
				+ COLUMN_FAMILY + ":" + this.midIndex + ","
				+ COLUMN_FAMILY + ":" + this.timeIndex + ","
				+ COLUMN_FAMILY + ":" + this.qiIndex + ","
				+ COLUMN_FAMILY + ":" + this.topicIndex + ","
				+ COLUMN_FAMILY + ":" + this.nontopicIndex + ","
				+ COLUMN_FAMILY + ":" + this.usertextIndex + " "
				+ "from weibo where " + "datetime>=" + startTime +" and datetime<=" + endTime;
	}
	
	public static class InnerMapper extends TableMapper<Text, Text> {
		private RecordFilter filter = new RecordFilter(){
			public boolean accept(JSONObject record) {
				return true;
			}
		};
		@Override
		protected void map(ImmutableBytesWritable key, Result value, Context context) throws IOException, InterruptedException {
			try {
				JSONObject record = JsonUtil.parse(key, value, COLUMN_FAMILY);
				if (filter.accept(record)) {
					Iterator<String> keys = record.keys();
					StringBuilder sb = new StringBuilder();
					sb.append("@");
					while (keys.hasNext()) {
						String colCode = keys.next();
						String colValue = record.getString(colCode);
						String colName = HBaseWeiboColumns.getName(colCode);
						if (StringUtils.trimToNull(colName) != null) {
							if (colName.equals(HBaseWeiboColumns.QI.getName())) {
								colName = ShCdh4HbaseMergeResult.QI_FROM_HBASE;
							}
							if (colName.equals(HBaseWeiboColumns.TOPIC_WORDS.getName())) {
								colName = ShCdh4HbaseMergeResult.TOPIC_FROM_HBASE;
							}
							if (colName.equals(HBaseWeiboColumns.NON_TOPIC_WORDS.getName())) {
								colName = ShCdh4HbaseMergeResult.NONTOPIC_FROM_HBASE;
							}
							if (colName.equals(HBaseWeiboColumns.USERTEXT.getName())) {
								colName = ShCdh4HbaseMergeResult.USERTEXT_FROM_HBASE;
							}
							sb.append("\n").append("@").append(colName).append(":");
							if (StringUtils.trimToNull(colValue) != null) {
								sb.append(colValue);
							}
						}
					}
					context.write(new Text(sb.toString()), null);
				}
			} catch (JSONException e) {throw new RuntimeException(e);}
		}
	}
}
