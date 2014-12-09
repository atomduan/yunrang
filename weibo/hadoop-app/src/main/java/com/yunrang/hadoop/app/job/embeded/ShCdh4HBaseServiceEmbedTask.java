package com.yunrang.hadoop.app.job.embeded;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
import org.codehaus.jettison.json.JSONObject;

import com.yunrang.hadoop.app.SingleMapReduceJobClient.JobSubmitContext;
import com.yunrang.hadoop.app.SingleMapReduceJobClient.SMRDefinition;
import com.yunrang.hadoop.app.common.adapter.StreamProcessAdapter;
import com.yunrang.hadoop.app.common.config.HBaseWeiboColumns;
import com.yunrang.hadoop.app.common.filter.RecordFilter;
import com.yunrang.hadoop.app.common.input.combine.CombineRegionInputFormat;
import com.yunrang.hadoop.app.utils.JsonUtil;
import com.yunrang.hadoop.app.utils.WeiboUtil;


@SuppressWarnings("unchecked")
public class ShCdh4HBaseServiceEmbedTask extends SMRDefinition {
	private static final String COLUMN_FAMILY = "cf";
	private static final String RESOURCE_HDFS_PATH_COLUMN= "mapreduce.job.resource.hdfs.path";
	private static final String CLEANUP_WAIT_SECOND = "mapreduce.job.resource.cleanup.wait.second";
	private String startTime;
	private String endTime;
	private String resourceHdfsPath;
	private String jobName;
	private String projectPrefix;
	private String hsql;
	private Integer cleanupWaitSecond;
	
	protected Configuration getConf(Configuration defaultConfig) {
		return HBaseConfiguration.create();
	}

	public void doConfiguration(JobSubmitContext jobSubmitContext, Job currJob) throws Exception {
		try {
			parseFromPayLoad(jobSubmitContext);
			String hsql = this.hsql;
			// Job config params setting
			currJob.setJobName(this.jobName);
			currJob.getConfiguration().set("mapreduce.job.queuename", "default");
			currJob.getConfiguration().set(CombineRegionInputFormat.HSQL_CONFIG, hsql);
			currJob.getConfiguration().setInt(CombineRegionInputFormat.CRIF_MAP_NUM_CONF, 400);
			currJob.getConfiguration().set(RESOURCE_HDFS_PATH_COLUMN, this.resourceHdfsPath);
			currJob.getConfiguration().setInt(CLEANUP_WAIT_SECOND, this.cleanupWaitSecond);
			// Configure Input
			currJob.setInputFormatClass(CombineRegionInputFormat.class);
			// Configure Job Mapper
			currJob.setMapperClass(InnerMapper.class);
			currJob.setMapOutputKeyClass(Text.class);
			currJob.setMapOutputValueClass(Text.class);
			// Configure Job Reducer
			currJob.setNumReduceTasks(SMRDefinition.ZERO_REDUCE_NUM);
			// Configure Output
			currJob.setSpeculativeExecution(false);
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
		this.hsql = "select " + COLUMN_FAMILY + ":*" + " from weibo where " + "datetime>=" + startTime +" and datetime<=" + endTime;
		
		String px = StringUtils.trimToNull(params[2]).trim();
		this.projectPrefix = px.endsWith("/") ? px.substring(0, px.length()-1) : px;
		
		String sx = StringUtils.trimToNull(params[3]).trim();
		this.resourceHdfsPath = sx.endsWith("/") ? sx.substring(0, sx.length()-1) : sx;
		
		this.jobName = StringUtils.trimToNull(params[4]).trim();
		
		this.cleanupWaitSecond = Integer.parseInt(StringUtils.trimToNull(params[5]).trim());
	}
	
	public static class InnerMapper extends TableMapper<Text, Text> {
		private RecordFilter selectedFilter = new RecordFilter() {
			@Override
			public boolean accept(JSONObject record) {
				return true;
			}
		};
		private Path srcFilePath;
		private StreamProcessAdapter mappingStageStreamProcessService;
		
		protected void setup(Context context) {
			this.srcFilePath = new Path(context.getConfiguration().get(RESOURCE_HDFS_PATH_COLUMN));
			try {
				Integer cleanupWaitSecond = context.getConfiguration().
					getInt(CLEANUP_WAIT_SECOND, StreamProcessAdapter.DEFAULT_CLEANUP_WAITSECOND);
				this.mappingStageStreamProcessService = 
					new StreamProcessAdapter(context, srcFilePath, cleanupWaitSecond);
				this.mappingStageStreamProcessService.start();
			} catch (Exception e) {}
		}
		
		
		private List<Text> buffer = new ArrayList<Text>();
		@Override
		protected void map(ImmutableBytesWritable key, Result value, Context context) throws IOException, InterruptedException {
			try {
				JSONObject record = JsonUtil.parse(key, value, COLUMN_FAMILY);
				if (selectedFilter.accept(record)) {
					try {
						Text outputKey = new Text(WeiboUtil.getStringField(record, HBaseWeiboColumns.ID));
						buffer.add(new Text("@"));
						Iterator<String> keys = record.keys();
						while (keys.hasNext()) {
							String colCode = keys.next();
							String colValue = record.getString(colCode);
							String colName = HBaseWeiboColumns.getName(colCode);
							if (StringUtils.trimToNull(colName) != null) {
								buffer.add(new Text("@"+colName+":"+colValue));
							}
						}
						for (Text outputValue : this.buffer) {
							this.mappingStageStreamProcessService.schedule(outputKey, outputValue);
						}
					} catch (Exception e) {
					} finally {
						this.buffer.clear();
					}
				}
			} catch (Exception e) {}
		}
		
		@Override
		public void cleanup(Context context) {
			try {
				mappingStageStreamProcessService.cleanup();
			} catch (Exception e) {}
		}
	}
}
