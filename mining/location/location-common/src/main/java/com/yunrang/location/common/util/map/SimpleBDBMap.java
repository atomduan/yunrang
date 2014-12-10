package com.yunrang.location.common.util.map;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;
import com.sleepycat.je.Transaction;

public class SimpleBDBMap implements SimpleMap {
	private static final String SEPERATOR = "#yrtmpbdb#";
	private static String defaultTmpPath = "/tmp/tmp_bdbmap_bdb";
	private String dataFilePath;
	private String dbName = "Tmpdata"+"_"+this.hashCode();
	
	private Environment dbEnvirment = null;
	private DatabaseConfig dbConfig = null;
	private Database dataBase = null;
	private File tmpDataFile = null;
	
	public SimpleBDBMap(String dataTmpPath) throws Exception {
		this.dataFilePath = dataTmpPath + "_" + this.hashCode();
		openDatabase();
	}
	
	public SimpleBDBMap() throws Exception {
		this(defaultTmpPath);
	}

	private void openDatabase() throws Exception {
		EnvironmentConfig envConfig = new EnvironmentConfig();
		envConfig.setAllowCreate(true);
		envConfig.setTransactional(true);
		envConfig.setReadOnly(false);
		initializeDBEnvirment(envConfig);
	}
	
	private void initializeDBEnvirment(EnvironmentConfig envConfig) throws Exception {
		tmpDataFile = new File(dataFilePath);
		if (tmpDataFile.exists()) {
			deleteFile(tmpDataFile);
		}
		tmpDataFile.mkdirs();
		dbEnvirment = new Environment(tmpDataFile, envConfig);
		dbConfig = new DatabaseConfig();
		dbConfig.setAllowCreate(true);
		dbConfig.setTransactional(false);
		dbConfig.setReadOnly(false);
		if (dataBase == null) {
			dataBase = dbEnvirment.openDatabase(null, dbName+"_"+this.hashCode(), dbConfig);
		}
	}
	
	public List<String> put(String key, List<String> list) throws Exception {
		Transaction txn = null;
		String value = this.list2String(list);
		DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
		DatabaseEntry theData = new DatabaseEntry(value.getBytes("UTF-8"));
		dataBase.put(txn, theKey, theData);
		return list;
	}


	public List<String> get(Object key) {
		try {
			Transaction txn = null;
			DatabaseEntry theKey = new DatabaseEntry(key.toString().getBytes("UTF-8"));
			DatabaseEntry theData = new DatabaseEntry();
			OperationStatus res = dataBase.get(txn, theKey, theData, LockMode.DEFAULT);
			if (res == OperationStatus.SUCCESS) {
				byte[] retData = theData.getData();
				String foundData = new String(retData, "UTF-8");
				return string2list(foundData);
			}
		} catch (Exception e) {}
		return null;
	}
	
	private String list2String(List<String> list) {
		String result="";
		for (int i=0; i<list.size(); i++) {
			if (i==0) {
				result = list.get(i);
			} else {
				result = result + SEPERATOR + list.get(i);
			}
		}
		return result;
	}
	
	private List<String> string2list(String str) {
		String[] arr = str.split(SEPERATOR);
		List<String> list = new ArrayList<String>();
		for (String s : arr) {
			list.add(s);
		}
		return list;
	}
	
	private void closeDatabase() throws Exception {
		if (dataBase != null) {
			dataBase.close();
		}
		if (dbEnvirment != null) {
			dbEnvirment.cleanLog();
			dbEnvirment.close();
		}
	}
	
	private void deleteFile(File dir) {
		File[] files = dir.listFiles();
		if (files != null) {
			for (File f : files) {
				if (f.isFile()) {
					f.delete();
				} else {
					deleteFile(f);
				}
			}
		}
		dir.delete();
		return;
	}
	
	public void clear() {
		try {
			closeDatabase();
			deleteFile(tmpDataFile);
		} catch (Exception e) {}
	}
}
