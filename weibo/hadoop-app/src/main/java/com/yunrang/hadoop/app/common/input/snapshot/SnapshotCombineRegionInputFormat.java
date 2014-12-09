package com.yunrang.hadoop.app.common.input.snapshot;


import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.log4j.Logger;


public class SnapshotCombineRegionInputFormat extends SnapshotRangeTableInputFormat {
	private static final Logger LOGGER = Logger.getLogger(SnapshotCombineRegionInputFormat.class);
	public static final String CRIF_MAP_NUM_CONF = "mapred.map.tasks";

	@Override
	public RecordReader<ImmutableBytesWritable, Result> createRecordReader(InputSplit split, TaskAttemptContext context) {
		SnapshotCombineRegionRecordReader<ImmutableBytesWritable, Result> reader = null;
		try {
			reader = new SnapshotCombineRegionRecordReader<ImmutableBytesWritable, Result>((SnapshotCombineRegionSplit)split, context, XSnapshotRegionRecordReader.class);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return reader;
	}

	public static class XSnapshotRegionRecordReader extends SnapshotRegionRecordReader {
		public XSnapshotRegionRecordReader(SnapshotCombineRegionSplit split, TaskAttemptContext context, Integer idx) throws Exception {
			Scan sc = new Scan(split.getScan(idx));
			sc.setStartRow(split.getStartRow(idx));
			sc.setStopRow(split.getEndRow(idx));
			setScan(sc);
			try {
				initialize(split.getInputSplit(idx), context);
			} catch (InterruptedException e) {
				throw new InterruptedIOException(e.getMessage());
			}
		}
	}	

	@Override
	public List<InputSplit> getSplits(JobContext context) throws IOException, InterruptedException {
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
		SnapshotCombineRegionSplit csplit = null;
		for(InputSplit split : _splits) {
			temp.add(split);			
			if (i < (grouplen-1)) {
				i++;
			} else {
				csplit = new SnapshotCombineRegionSplit(temp.toArray(new SnapshotRegionSplit[grouplen]));
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
		Map<String, List<SnapshotRegionSplit>> map = new HashMap<String, List<SnapshotRegionSplit>>();
		for(InputSplit split : _splits) {
			SnapshotRegionSplit tsplit = (SnapshotRegionSplit)split;
			String key = tsplit.getRegionName();
			if (map.get(key) == null) {
				map.put(key, new ArrayList<SnapshotRegionSplit>());
			}
			map.get(key).add(tsplit);
		}
		// get the min region of rs
		int min = region_total;
		for(List<SnapshotRegionSplit> list : map.values()) {
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
		SnapshotCombineRegionSplit csplit = null;		
		for(List<SnapshotRegionSplit> list : map.values()) {
			List[] groups = new List[groupnum];
			for(int i = 0; i < groups.length; i++) {
				groups[i] = new ArrayList<SnapshotRegionSplit>();
			}
			for(int j = 0; j < list.size(); j++) {
				int y = j % groupnum;
				groups[y].add(list.get(j));
			}
			for(int k = 0; k < groups.length; k++) {
				List<SnapshotRegionSplit> group = (ArrayList<SnapshotRegionSplit>)groups[k];
				int len = group.size();
				csplit = new SnapshotCombineRegionSplit(group.toArray(new SnapshotRegionSplit[len]));
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
			if (split instanceof SnapshotRegionSplit) {
				break;	
			}
			SnapshotCombineRegionSplit tsplit = (SnapshotCombineRegionSplit)split;
			SnapshotRegionSplit[] subs = tsplit.getInputSplit();
			for(SnapshotRegionSplit sub : subs) {
				if (count++ > viewNumber) continue;
				buffer.append("\t " + new String(sub.getRegionName()) 
				+ " on " + sub.getRegionName() 
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
