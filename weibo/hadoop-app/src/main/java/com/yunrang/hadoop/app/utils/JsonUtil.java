package com.yunrang.hadoop.app.utils;

import java.util.Map;
import java.util.NavigableMap;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.util.Bytes;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.yunrang.hadoop.app.common.config.HBaseWeiboColumns;

public class JsonUtil {
	static public JSONObject parse(ImmutableBytesWritable key, Result value, String columnFamily) throws JSONException {
		byte[] rowkey = new byte[key.getLength()];
		Bytes.putBytes(rowkey, 0, key.get(), 0, key.getLength());

		NavigableMap<byte[], byte[]> map = value.getFamilyMap(columnFamily.getBytes());
		byte[][] columns = map.keySet().toArray(new byte[map.size()][]);

		JSONObject json = new JSONObject();
		for (byte[] col : columns) {
			byte[] colvalue = value.getValue(columnFamily.getBytes(), col);
			if (null == colvalue) {
				return null;
			}
			json.put(new String(col), new String(colvalue));
		}
		json.put("rk", new String(value.getRow()));
		return json;
	}
	
	static public JSONObject parse(Map<String, String> attrMap) throws JSONException {
		JSONObject json = new JSONObject();
		for (String key : attrMap.keySet()) {
			String value = attrMap.get(key);
			String codeKey = HBaseWeiboColumns.getCode(key);
			json.put(codeKey, value);
		}
		return json;
	}
}
