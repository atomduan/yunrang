package com.yunrang.hadoop.app.job.history.recompute.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;

import com.yunrang.hadoop.app.common.config.HBaseWeiboColumns;
import com.yunrang.hadoop.app.utils.CommonMethod;
import com.yunrang.hadoop.app.utils.HashUtils;

public class WeiboHbaseAccesser {
	private static final String TABLE_NAME="weibo";
	private Configuration conf;
	private HTable table;
	public WeiboHbaseAccesser() throws Exception {
		this.conf = HBaseConfiguration.create();
		this.table = new HTable(this.conf, TABLE_NAME);
	}
	public Map<String, String> get(String mid, String date) throws Exception {
		Map<String, String> result = new HashMap<String, String>();
		String rowKey = null;
		if (CommonMethod.isNormal(mid)) {
			rowKey = HashUtils.getRowKey(mid);
		} else {
			rowKey = HashUtils.getRowKeyForUnnormal(mid, date);
		}
		Get g = new Get(rowKey.getBytes());
		g.addFamily("cf".getBytes());
		Result r = this.table.get(g);
		Map<byte[], byte[]> rMap = r.getFamilyMap("cf".getBytes());
		for (byte[] k : rMap.keySet()){
			String key = new String(k);
			String value = new String(rMap.get(k));
			result.put(HBaseWeiboColumns.getName(key), value);
		}
		return result;
	}
}
