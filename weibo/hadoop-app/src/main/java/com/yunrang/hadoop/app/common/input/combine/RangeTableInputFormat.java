package com.yunrang.hadoop.app.common.input.combine;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configurable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.FirstKeyOnlyFilter;
import org.apache.hadoop.hbase.filter.KeyOnlyFilter;
import org.apache.hadoop.hbase.mapreduce.TableInputFormat;
import org.apache.hadoop.hbase.mapreduce.TableSplit;
import org.apache.hadoop.hbase.util.Base64;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.log4j.Logger;

import com.yunrang.hadoop.app.utils.DateUtil;


public class RangeTableInputFormat extends TableInputFormat implements Configurable {
	private static final Logger LOGGER = Logger.getLogger(RangeTableInputFormat.class);
	public static final String HSQL_CONFIG = "hbase.mapreduce.sql";
	private String[] rangeDate;
	private String hsql;
	public String columns;
	public String tableName;
	public String family;
	public boolean counter = false;

	@Override
	public List<InputSplit> getSplits(JobContext context) throws IOException {
		List<InputSplit> _splits = super.getSplits(context);
		// add scan
		_splits = addScanForSplit(_splits);
		if (rangeDate != null) {
			_splits = reSplit(_splits);
		}
		// print the information of splits
		printSplits(_splits);
		return _splits;
	}
	
	/**
	 * add scan
	 */
	protected List<InputSplit> addScanForSplit(List<InputSplit> _splits) throws IOException {
		List<InputSplit> splits = new ArrayList<InputSplit>();
		for(InputSplit split : _splits) {
			TableSplit tsplit = (TableSplit)split;
			TableSplit newsplit = new TableSplit(
					tsplit.getTableName(), 
					this.getScan(),
					tsplit.getStartRow(), 
					tsplit.getEndRow(), 
					tsplit.getRegionLocation());

			splits.add(newsplit);
		}
		return splits;
	}
	
	protected List<InputSplit> reSplit(List<InputSplit> _splits) throws IOException {
		List<InputSplit> splits = new ArrayList<InputSplit>(_splits.size());
		for(InputSplit split : _splits) {
			TableSplit tsplit = (TableSplit)split;
			String startRow = new String(tsplit.getStartRow());
			String endRow = new String(tsplit.getEndRow());

			if (startRow.length() == 0) {
				startRow = "000";				
			} else {
				startRow = startRow.substring(0, 3);
			}

			String[] rows = null;
			if (rangeDate.length == 1) {
				rows = getStartEndRow(startRow, rangeDate[0]);	
			} else if (rangeDate.length == 2) {
				rows = getStartEndRow(startRow, rangeDate[0], rangeDate[1]);
			}
			startRow = rows[0];
			endRow = rows[1];

			TableSplit newsplit = new TableSplit(tsplit.getTableName(), tsplit.getScan(),
					startRow.getBytes(), 
					endRow.getBytes(), 
					tsplit.getRegionLocation());

			splits.add(newsplit);
		}
		return splits;
	}
	
	private String[] getStartEndRow(String seg, String startDate, String endDate) throws IOException {
		String[] start = getStartEndRow(seg, startDate);
		String[] end = getStartEndRow(seg, endDate);
		return new String[] {start[0], end[1]};
	}
	
	private String[] getStartEndRow(String seg, String date) throws IOException {
		String[] rows = new String[2];

		String stime = date;
		int informat = DateUtil.findIntFormat(date);

		rows[0] = getHex80(seg, stime, informat);		
		String etime = null;
		try {
			// add one day
			etime = DateUtil.addDate(date, informat, 1);
		} catch (Exception e) {
			throw new IOException(e);
		}
		rows[1] = getHex80(seg, etime, informat);
		return rows;
	}

	private String getHex80(String seg, String time, int informat) {
        long b = getMidTimeLong(time);
		String hex64 = bytesToHexString(Bytes.toBytes(b)).toLowerCase();
		String hex80 = seg + "0" + hex64;
		return hex80;
	}
	
	public static long getMidTimeLong(String time) {
		long ltime = DateUtil.getDateTimeLong(time);
		// exchange time from millisecond to second
		ltime = ltime / 1000;
		long b = ltime - 515483463;
		b = b << 22;
		return b;
	}
	
	public static final String bytesToHexString(byte[] bArray) {
		StringBuffer sb = new StringBuffer(bArray.length);
		String temp;
		for (int i = 0; i < bArray.length; i++) {
			temp = Integer.toHexString(0xFF & bArray[i]);
			if (temp.length() < 2) {
				sb.append(0);
			}
			sb.append(temp.toUpperCase());
		}
        return sb.toString();
	}

	/** The configuration. */
	private Configuration conf = null;

	@Override
	public void setConf(Configuration configuration) {
		this.conf = configuration;
		hsql = conf.get(HSQL_CONFIG);
		if (hsql != null) {
			if (!parseSql()) {
				LOGGER.info("the hsql [" + hsql + "] is error.");
			}
			if (tableName != null) {
				conf.set(INPUT_TABLE, tableName);
			}
			if (family != null) {
				conf.set(SCAN_COLUMN_FAMILY, family);
			}
			if (columns != null) {
				conf.set(SCAN_COLUMNS, columns);
			}
			// count
			if (counter) {
				Scan scan = new Scan();
				scan.setCaching(conf.getInt(SCAN_CACHEDROWS, 500));
				scan.setCacheBlocks(conf.getBoolean(SCAN_CACHEBLOCKS, false));
				FilterList fl = new FilterList();
				fl.addFilter(new FirstKeyOnlyFilter());
				fl.addFilter(new KeyOnlyFilter());
				scan.setFilter(fl);
				try {
					String scanString = convertScanToString(scan);
					conf.set(SCAN, scanString);
				} catch (IOException e) {
					LOGGER.error(e);
				}			
			}
		}
		LOGGER.info("#### hbase sql #### " + hsql);
		super.setConf(configuration);
	}
	
	public static String convertScanToString(Scan scan) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(out);
		scan.write(dos);
		return Base64.encodeBytes(out.toByteArray());
	}

	public static Scan convertStringToScan(String base64) throws IOException {
		ByteArrayInputStream bis = new ByteArrayInputStream(Base64.decode(base64));
		DataInputStream dis = new DataInputStream(bis);
		Scan scan = new Scan();
		scan.readFields(dis);
		return scan;
	}
	
	private String getDatetime(String dateString) {
		String symbols = null;
		if (dateString.contains("<=")) {
			symbols = "<=";
		} else if (dateString.contains(">=")) {
			symbols = ">=";
		} else if (dateString.contains("=")) {
			symbols = "=";
		}
		String temp = dateString.substring(dateString.indexOf(symbols)+symbols.length());
		temp = temp.replace("'", "").trim();
		return temp;
	}
	
	private boolean parseSql() {
		hsql = hsql.trim();
		String[] keywords = hsql.split(" ");
		if (keywords.length < 4)
			return false;
		if (!keywords[0].equals("select") || !keywords[2].equals("from")) {
			return false;
		}		

		String qualifier = keywords[1];
		if (qualifier.equals("count")) {
			counter = true;
		} else if (qualifier.contains("*")) {
			family = qualifier.substring(0, qualifier.indexOf(":"));
		} else {
			columns = qualifier.replace(",", " ");
		}				

		tableName = keywords[3];
		
		if (keywords.length == 4)
			return true;
		// time
		if (keywords.length < 6)
			return false;
		if (!keywords[4].equals("where"))
			return false;
		
		int whereindex = hsql.indexOf("where") + "where".length();
		String dateString = hsql.substring(whereindex, hsql.length());
		String[] pair = dateString.split("and");
		if (pair.length > 2)
			return false;
		
		rangeDate = new String[pair.length];
		for(int i = 0; i < pair.length; i++) {
			if (!pair[i].trim().startsWith("datetime"))
				return false;
			rangeDate[i] = getDatetime(pair[i]);			
		}
		
		return true;
	}

	@Override
	public Configuration getConf() {
		return conf;
	}

	protected void printSplits(List<InputSplit> splits) {
		int viewNumber = 10;
		StringBuffer buffer = new StringBuffer();
		int count = 0;
		for(InputSplit split : splits) {
			TableSplit tsplit = (TableSplit)split;
			if (count++ > viewNumber) continue;
			buffer.append("\t " + new String(tsplit.getTableName()) 
			+ " on " + tsplit.getRegionLocation() 
			+ " : " + new String(tsplit.getStartRow()) + " - " + new String(tsplit.getEndRow()) + "\n");
		}
		if (count >= viewNumber) {
			buffer.append("Other # < " + (count-viewNumber) + " > more splits not listed..........");
		}
		buffer.append("Total # of splits : " + splits.size());
		LOGGER.info("Total input splits to process : " + "\n" + buffer.toString());
	}
}
