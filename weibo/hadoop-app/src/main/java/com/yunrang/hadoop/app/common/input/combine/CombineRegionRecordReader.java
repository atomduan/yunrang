package com.yunrang.hadoop.app.common.input.combine;

import java.io.IOException;
import java.lang.reflect.Constructor;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.log4j.Logger;


/**
 * A generic RecordReader that can hand out different recordReaders
 * for each region in a {@link CombineRegionSplit}.
 * A CombineRegionSplit can combine region from multiple regions.
 * @see CombineRegionSplit
 * @param <K>
 * @param <V>
 */
@SuppressWarnings("rawtypes")
public class CombineRegionRecordReader<K, V> extends RecordReader<K, V> {
	private static final Logger LOGGER = Logger.getLogger(CombineRegionRecordReader.class);
	static final Class [] constructorSignature = new Class[]{CombineRegionSplit.class, TaskAttemptContext.class, Integer.class};

	protected CombineRegionSplit split;
	protected Class<? extends RecordReader<K,V>> rrClass;
	protected Constructor<? extends RecordReader<K,V>> rrConstructor;
	protected FileSystem fs;
	protected TaskAttemptContext context;

	protected int idx;
	protected long progress;
	protected RecordReader<K, V> curReader;
	protected long count;

	public void initialize(InputSplit split, TaskAttemptContext context) throws IOException, InterruptedException {
		this.split = (CombineRegionSplit)split;
		this.context = context;
		if (null != this.curReader) {
			this.curReader.initialize(split, context);
		}
	}

	public boolean nextKeyValue() throws IOException, InterruptedException {
		while ((curReader == null) || !curReader.nextKeyValue()) {
			if (!initNextRecordReader()) {
				return false;
			}
		}
		count++;
		return true;
	}

	public K getCurrentKey() throws IOException, InterruptedException {
		return curReader.getCurrentKey();
	}

	public V getCurrentValue() throws IOException, InterruptedException {
		return curReader.getCurrentValue();
	}

	public void close() throws IOException {
		if (curReader != null) {
			curReader.close();
			curReader = null;
		}
	}

	/**
	 * return progress based on the amount of data processed so far.
	 */
	 public float getProgress() throws IOException, InterruptedException {
		long subprogress = 0;    // bytes processed in current split
		if (null != curReader) {
			// idx is always one past the current subsplit's true index.
			subprogress = (long)(curReader.getProgress() * split.getLength(idx - 1));
		}
		return Math.min(1.0f,  (progress + subprogress)/(float)(split.getLength()));
	 }

	 /**
	  * A generic RecordReader that can hand out different recordReaders
	  * for each chunk in the CombineRegionSplit.
	  */
	 public CombineRegionRecordReader(CombineRegionSplit split, TaskAttemptContext context, Class<? extends RecordReader<K,V>> rrClass) throws IOException {
		 this.split = split;
		 this.context = context;
		 this.rrClass = rrClass;
		 this.idx = 0;
		 this.curReader = null;
		 this.progress = 0;
		 try {
			 rrConstructor = rrClass.getDeclaredConstructor(constructorSignature);
			 rrConstructor.setAccessible(true);
		 } catch (Exception e) {
			 throw new RuntimeException(rrClass.getName() + " does not have valid constructor", e);
		 }
		 initNextRecordReader();
	 }

	 /**
	  * Get the record reader for the next chunk in this CombineRegionSplit.
	  */
	 protected boolean initNextRecordReader() throws IOException {
		 if (curReader != null) {
			 curReader.close();
			 curReader = null;
			 if (idx > 0) {
				 progress += split.getLength(idx-1);    // done processing so far
			 }
		 }
		 // count log
		 if (count != 0) {
			 LOGGER.info("current-count : " + count);
			 count = 0;
		 }
		 // if all chunks have been processed, nothing more to do.
		 if (idx == split.getNumSplit()) {
			 return false;
		 }
		 // get a record reader for the idx-th chunk
		 try {
			 curReader =  rrConstructor.newInstance(new Object[]{split, context, Integer.valueOf(idx)});
			 if (idx > 0) {
				 // initialize() for the first RecordReader will be called by MapTask;
				 // we're responsible for initializing subsequent RecordReaders.
				 curReader.initialize(split, context);
			 }
		 } catch (Exception e) {
			 throw new RuntimeException (e);
		 }
		 // split-reader log
		 LOGGER.info("init reader [" + split.getInputSplit(idx).toString() + "]");
		 idx++; 
		 return true;
	 }
}
