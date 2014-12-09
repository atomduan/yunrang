package com.yunrang.hadoop.app.common.input.snapshot;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.InputSplit;

import com.yunrang.hadoop.app.common.input.snapshot.SnapshotInputFormat.SnapshotRegionSplit;

/**
 * A sub-collection of input regions. 
 */
public class SnapshotCombineRegionSplit extends InputSplit implements Writable {

	private SnapshotRegionSplit[] snapshotRegionSplits;

	public SnapshotCombineRegionSplit() {
	}

	public SnapshotCombineRegionSplit(SnapshotRegionSplit[] tablesplits) {
		this.snapshotRegionSplits = tablesplits;
	}

	/** Returns the number of Paths in the split */
	public int getNumSplit() {
		return snapshotRegionSplits.length;
	}

	public InputSplit getInputSplit(int index) {
		return snapshotRegionSplits[index];
	}

	public SnapshotRegionSplit[] getInputSplit() {
		return snapshotRegionSplits;
	}

	/**
	 * Returns a Scan object from the stored string representation.
	 *
	 * @return Returns a Scan object based on the stored scanner.
	 * @throws IOException
	 */
	public Scan getScan(int index) throws IOException {
		return snapshotRegionSplits[index].getScan();
	}

	/**
	 * Returns the table name.
	 *
	 * @return The table name.
	 */
	public byte [] getTableName(int index) {
		return snapshotRegionSplits[index].getRegionName().getBytes();
	}

	/**
	 * Returns the start row.
	 *
	 * @return The start row.
	 */
	public byte [] getStartRow(int index) {
		return snapshotRegionSplits[index].getStartRow();
	}

	/**
	 * Returns the end row.
	 *
	 * @return The end row.
	 */
	public byte [] getEndRow(int index) {
		return snapshotRegionSplits[index].getEndRow();
	}

	/**
	 * Returns the region location.
	 *
	 * @return The region's location.
	 */
	public String getRegionLocation(int index) {
		return snapshotRegionSplits[index].getRegionName();
	}

	/**
	 * Returns the region's location as an array.
	 *
	 * @return The array containing the region location.
	 * @see org.apache.hadoop.mapreduce.InputSplit#getLocations()
	 */
	public String[] getLocations() {
		int num = snapshotRegionSplits.length;
		String[] locs = new String[num];
		for(int i = 0; i < num ; i++) {
			try {
				locs[i] = snapshotRegionSplits[i].getLocations()[0];
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return locs;
	}

	/**
	 * Returns the length of the split.
	 *
	 * @return The length of the split.
	 * @see org.apache.hadoop.mapreduce.InputSplit#getLength()
	 */
	public long getLength(int index) {
		// Not clear how to obtain this... seems to be used only for sorting splits
		//return tablesplits[index].getLength();
		// 1 unit per split
		return 1;
	}

	/**
	 * Returns the length of the split.
	 *
	 * @return The length of the split.
	 * @see org.apache.hadoop.mapreduce.InputSplit#getLength()
	 */
	public long getLength() {
		// Not clear how to obtain this... seems to be used only for sorting splits
		// 1 unit per split, return the num of split
		return snapshotRegionSplits.length;
	}

	public void readFields(DataInput in) throws IOException {

		int numsplit = in.readInt();
		snapshotRegionSplits = new SnapshotRegionSplit[numsplit];
		for(int i= 0; i < numsplit; i++) {
			SnapshotRegionSplit ts = new SnapshotRegionSplit();
			ts.readFields(in);
			snapshotRegionSplits[i] = ts;
		}
	}

	public void write(DataOutput out) throws IOException {
		int numsplit = getNumSplit();
		out.writeInt(numsplit);
		for(SnapshotRegionSplit ts : snapshotRegionSplits) {
			ts.write(out);
		}
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("split-num = " + snapshotRegionSplits.length + "\n");
		for(SnapshotRegionSplit ts : snapshotRegionSplits) {
			sb.append(ts.toString() + "\n");
		}
		return sb.toString();
	}

}

