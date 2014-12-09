package com.yunrang.hadoop.app.common.input.combine;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.mapreduce.TableSplit;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.InputSplit;

/**
 * A sub-collection of input regions. 
 * 
 * 
 * @see TableSplit
 * @see CombineRegionInputFormat 
 * 
 * @author jianyesun
 *
 */
public class CombineRegionSplit extends InputSplit implements Writable {

	private TableSplit[] tablesplits;

	public CombineRegionSplit() {
	}

	public CombineRegionSplit(TableSplit[] tablesplits) {
		this.tablesplits = tablesplits;
	}

	/** Returns the number of Paths in the split */
	public int getNumSplit() {
		return tablesplits.length;
	}

	public InputSplit getInputSplit(int index) {
		return tablesplits[index];
	}

	public TableSplit[] getInputSplit() {
		return tablesplits;
	}

	/**
	 * Returns a Scan object from the stored string representation.
	 *
	 * @return Returns a Scan object based on the stored scanner.
	 * @throws IOException
	 */
	public Scan getScan(int index) throws IOException {
		return tablesplits[index].getScan();
	}

	/**
	 * Returns the table name.
	 *
	 * @return The table name.
	 */
	public byte [] getTableName(int index) {
		return tablesplits[index].getTableName();
	}

	/**
	 * Returns the start row.
	 *
	 * @return The start row.
	 */
	public byte [] getStartRow(int index) {
		return tablesplits[index].getStartRow();
	}

	/**
	 * Returns the end row.
	 *
	 * @return The end row.
	 */
	public byte [] getEndRow(int index) {
		return tablesplits[index].getEndRow();
	}

	/**
	 * Returns the region location.
	 *
	 * @return The region's location.
	 */
	public String getRegionLocation(int index) {
		return tablesplits[index].getRegionLocation();
	}

	/**
	 * Returns the region's location as an array.
	 *
	 * @return The array containing the region location.
	 * @see org.apache.hadoop.mapreduce.InputSplit#getLocations()
	 */
	public String[] getLocations() {
		int num = tablesplits.length;
		String[] locs = new String[num];
		for(int i = 0; i < num ; i++) {
			locs[i] = tablesplits[i].getLocations()[0];
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
		return tablesplits.length;
	}

	public void readFields(DataInput in) throws IOException {

		int numsplit = in.readInt();
		tablesplits = new TableSplit[numsplit];
		for(int i= 0; i < numsplit; i++) {
			TableSplit ts = new TableSplit();
			ts.readFields(in);
			tablesplits[i] = ts;
		}
	}

	public void write(DataOutput out) throws IOException {
		int numsplit = getNumSplit();
		out.writeInt(numsplit);
		for(TableSplit ts : tablesplits) {
			ts.write(out);
		}
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("split-num = " + tablesplits.length + "\n");
		for(TableSplit ts : tablesplits) {
			sb.append(ts.toString() + "\n");
		}
		return sb.toString();
	}

}

