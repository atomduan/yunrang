package com.yunrang.hadoop.app.common.filter;

import org.codehaus.jettison.json.JSONObject;

public interface RecordFilter {
	public boolean accept(JSONObject record);
}
