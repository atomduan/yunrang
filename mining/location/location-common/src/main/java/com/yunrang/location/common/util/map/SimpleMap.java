package com.yunrang.location.common.util.map;

import java.util.List;

public interface SimpleMap {
	List<String> put(String key, List<String> value) throws Exception;
	List<String> get(Object key) throws Exception;
	void clear() throws Exception;
}
