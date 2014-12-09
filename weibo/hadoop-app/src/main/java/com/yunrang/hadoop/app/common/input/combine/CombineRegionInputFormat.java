package com.yunrang.hadoop.app.common.input.combine;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableRecordReader;
import org.apache.hadoop.hbase.mapreduce.TableSplit;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.log4j.Logger;


public class CombineRegionInputFormat extends RangeTableInputFormat {
	private static final Logger LOGGER = Logger.getLogger(CombineRegionInputFormat.class);
	public static final String CRIF_MAP_NUM_CONF = "mapred.map.tasks";

	@Override
	public RecordReader<ImmutableBytesWritable, Result> createRecordReader(InputSplit split, TaskAttemptContext context) {
		CombineRegionRecordReader<ImmutableBytesWritable, Result> reader = null;
		try {
			reader = new CombineRegionRecordReader<ImmutableBytesWritable, Result>((CombineRegionSplit)split, context, XTableRecordReader.class);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return reader;
	}

	public static class XTableRecordReader extends TableRecordReader {
		public XTableRecordReader(CombineRegionSplit split, TaskAttemptContext context, Integer idx) throws Exception {
			Scan sc = new Scan(split.getScan(idx));
			sc.setStartRow(split.getStartRow(idx));
			sc.setStopRow(split.getEndRow(idx));
			setScan(sc);
			Configuration conf = context.getConfiguration();
			setHTable(new HTable(conf, split.getTableName(idx)));
			try {
				initialize(split.getInputSplit(idx), context);
			} catch (InterruptedException e) {
				throw new InterruptedIOException(e.getMessage());
			}
		}	
	}	

	@Override
	public List<InputSplit> getSplits(JobContext context) throws IOException {
		List<InputSplit> _splits = super.getSplits(context);
		String mapnum = context.getConfiguration().get(CRIF_MAP_NUM_CONF);
		int imapnum = 0;
		try {
			imapnum = Integer.parseInt(mapnum);
		} catch (Exception e) {
			LOGGER.info("the value of mapred.map.tasks is invalid (" + mapnum + "). ");
		}
		if (imapnum < 1) {
			imapnum = _splits.size();
		}
		// combine
		String policy = context.getConfiguration().get("hbase.mapreduce.combine.policy");
		if (policy != null && policy.equals("region")) {
			_splits = combineSplits(_splits, imapnum);
		} else {
			_splits = combineSplitsByRS(_splits, imapnum);
		}
		// print the information of splits
		printSplits(_splits);
		return _splits;
	}

	protected List<InputSplit> combineSplits(List<InputSplit> _splits, int mapnum) {
		List<InputSplit> splits = new ArrayList<InputSplit>();
		int region_num = _splits.size();
		int grouplen = 1;
		while(true) {
			if ((region_num / grouplen) <= mapnum) {
				break;
			}
			grouplen = grouplen * 2;
		}
		int i = 0;
		List<InputSplit> temp = new ArrayList<InputSplit>();
		CombineRegionSplit csplit = null;
		for(InputSplit split : _splits) {
			temp.add(split);			
			if (i < (grouplen-1)) {
				i++;
			} else {
				csplit = new CombineRegionSplit(temp.toArray(new TableSplit[grouplen]));
				temp.clear();
				splits.add(csplit);
				i = 0;				
			}
		}
		return splits;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected List<InputSplit> combineSplitsByRS(List<InputSplit> _splits, int mapnum) {
		// correct the num of map
		int region_total = _splits.size();
		if (mapnum > region_total) {
			mapnum = region_total;	
		}
		List<InputSplit> splits = new ArrayList<InputSplit>();
		// group by rs
		Map<String, List<TableSplit>> map = new HashMap<String, List<TableSplit>>();
		for(InputSplit split : _splits) {
			TableSplit tsplit = (TableSplit)split;
			String key = tsplit.getRegionLocation();
			if (map.get(key) == null) {
				map.put(key, new ArrayList<TableSplit>());
			}
			map.get(key).add(tsplit);
		}
		// get the min region of rs
		int min = region_total;
		for(List<TableSplit> list : map.values()) {
			if (list.size() < min) {
				min = list.size();	
			}
		}
		int rsnum = map.size();
		// correct the num of map
		if (mapnum < rsnum) {
			mapnum = rsnum;	
		}
		int groupnum = 1;
		while(true) {
			if ((rsnum * groupnum) > mapnum) {
				groupnum--;
				break;
			}
			groupnum++;
		}
		// correct the groupnum
		if (groupnum > min) {
			groupnum = min;	
		}
		CombineRegionSplit csplit = null;		
		for(List<TableSplit> list : map.values()) {
			List[] groups = new List[groupnum];
			for(int i = 0; i < groups.length; i++) {
				groups[i] = new ArrayList<TableSplit>();
			}
			for(int j = 0; j < list.size(); j++) {
				int y = j % groupnum;
				groups[y].add(list.get(j));
			}
			for(int k = 0; k < groups.length; k++) {
				List<TableSplit> group = (ArrayList<TableSplit>)groups[k];
				int len = group.size();
				csplit = new CombineRegionSplit(group.toArray(new TableSplit[len]));
				splits.add(csplit);
			}
		}
		return splits;
	}

	protected void printSplits(List<InputSplit> splits) {
		int viewNumber = 10;
		StringBuffer buffer = new StringBuffer();
		int count = 0;
		for(InputSplit split : splits) {
			if (split instanceof TableSplit) {
				break;	
			}
			CombineRegionSplit tsplit = (CombineRegionSplit)split;
			TableSplit[] subs = tsplit.getInputSplit();
			for(TableSplit sub : subs) {
				if (count++ > viewNumber) continue;
				buffer.append("\t " + new String(sub.getTableName()) 
				+ " on " + sub.getRegionLocation() 
				+ " : " + new String(sub.getStartRow()) + " - " + new String(sub.getEndRow()) + "\n");
			}
		}
		if (count >= viewNumber) {
			buffer.append("\t Other more splits not listed..........\n");
		}
		buffer.append("Total # of splits : " + splits.size());
		LOGGER.info("Total input splits to process : " + "\n" + buffer.toString());
	}
}
