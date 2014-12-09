package com.yunrang.hadoop.app.common.input.snapshot;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HConstants;
import org.apache.hadoop.hbase.HRegionInfo;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.IsolationLevel;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.errorhandling.ForeignExceptionDispatcher;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableInputFormat;
import org.apache.hadoop.hbase.monitoring.MonitoredTask;
import org.apache.hadoop.hbase.monitoring.TaskMonitor;
import org.apache.hadoop.hbase.protobuf.generated.HBaseProtos.SnapshotDescription;
import org.apache.hadoop.hbase.regionserver.HRegion;
import org.apache.hadoop.hbase.regionserver.RegionScanner;
import org.apache.hadoop.hbase.snapshot.RestoreSnapshotHelper;
import org.apache.hadoop.hbase.snapshot.SnapshotDescriptionUtils;
import org.apache.hadoop.hbase.snapshot.SnapshotReferenceUtil;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.util.FSTableDescriptors;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

import com.yunrang.hadoop.app.utils.CustomizedUtil;

/**
 * SnapshotInputFormat allows a MapReduce job to run over a table snapshot. The
 * job bypasses HBase servers, and directly accesses the underlying files
 * (hfile, recovered edits, hlogs, etc) directly to provide maximum performance.
 * The snapshot is not required to be restored or cloned. This also allows to
 * run the mapreduce job from an online or offline hbase cluster. The snapshot
 * files can be exported by using the ExportSnapshot tool, to a pure-hdfs
 * cluster, and this InputFormat can be used to run the mapreduce job directly
 * over the snapshot files.
 * <p>
 * Usage is similar to TableInputFormat, and
 * TableMapReduceUtil.initSnapshotMapperJob(String, Scan, Class, Class, Class,
 * Job, boolean)} can be used to configure the job.
 * 
 * <pre>
 * {
 * 	&#064;code
 * 	Job job = new Job(conf);
 * 	Scan scan = new Scan();
 * 	TableMapReduceUtil.initSnapshotMapperJob(snapshotName, scan,
 * 			MyTableMapper.class, MyMapKeyOutput.class,
 * 			MyMapOutputValueWritable.class, job, true);
 * }
 * </pre>
 * <p>
 * Internally, this input format restores the snapshot into the working
 * directory. Similar to TableInputFormat an InputSplit is created per region.
 * The region is opened for reading from each RecordReader. An internal
 * RegionScanner is used to execute the Scan obtained from the user.
 * <p>
 * The user has to have sufficient access rights in the file system to access
 * the snapshot files, and referenced files.
 */
public class SnapshotInputFormat extends InputFormat<ImmutableBytesWritable, Result> {
	// TODO: Snapshots files are owned in fs by the hbase user. There is no
	// easy way to delegate access.

	private static final String SNAPSHOT_NAME_KEY = "hbase.mr.snapshot.input.name";
	private static final String TABLE_DIR_KEY = "hbase.mr.snapshot.input.table.dir";

	/**
	 * Snapshot region split.
	 */
	public static final class SnapshotRegionSplit extends InputSplit implements Writable {
		private String regionName;
		private String[] locations;

		/**
		 * Constructor for serialization.
		 */
		public SnapshotRegionSplit() {}

		/**
		 * Constructor.
		 * @param regionName Region name
		 * @param locationList List of nodes with the region's HDFS blocks, in descending order of weight
		 */
		public SnapshotRegionSplit(final String regionName, final List<String> locationList) {
			this.regionName = regionName;
			// only use the top node
			List<String> list = locationList.size() > 1 ? locationList.subList(0, 1) : locationList;
			this.locations = list.toArray(new String[list.size()]);
		}

		@Override
		public long getLength() throws IOException, InterruptedException {
			return locations.length;
		}

		@Override
		public String[] getLocations() throws IOException, InterruptedException {
			return locations;
		}

		@Override
		public void readFields(DataInput in) throws IOException {
			regionName = Text.readString(in);
			int locLength = in.readInt();
			locations = new String[locLength];
			for (int i = 0; i < locLength; i++) {
				locations[i] = Text.readString(in);
			}
		}

		@Override
		public void write(DataOutput out) throws IOException {
			Text.writeString(out, regionName);
			out.writeInt(locations.length);
			for (String l : locations) {
				Text.writeString(out, l);
			}
		}

		private byte[] startRow;
		public void setStartRow(byte[] startRow) {
			this.startRow = startRow;
		}
		public byte[] getStartRow() {
			return this.startRow;
		}

		private byte[] endRow;
		public void setEndRow(byte[] endRow) {
			this.endRow = endRow;
		}
		public byte[] getEndRow() {
			return this.endRow;
		}
		
		private Scan scan;
		public Scan getScan() {
			return scan;
		}
		public void setScan(Scan scan) {
			this.scan = scan;
		}

		public String getRegionName() {
			return regionName;
		}
		public void setRegionName(String regionName) {
			this.regionName = regionName;
		}
	}

	/**
	 * Snapshot region record reader.
	 */
	public static class SnapshotRegionRecordReader extends RecordReader<ImmutableBytesWritable, Result> {
		private SnapshotRegionSplit split;
		private HRegion region;
		private Scan scan;
		private RegionScanner scanner;
		private List<KeyValue> values;
		private Result result = null;
		private ImmutableBytesWritable row = null;
		private boolean more;

		public void setScan(Scan scan) {
			this.scan = scan;
		}
		
		@Override
		public void initialize(final InputSplit aSplit, final TaskAttemptContext context) 
				throws IOException, InterruptedException {
			Configuration conf = context.getConfiguration();
			this.split = (SnapshotRegionSplit) aSplit;

			Path rootDir = new Path(conf.get(HConstants.HBASE_DIR));
			FileSystem fs = rootDir.getFileSystem(conf);

			String snapshotName = getSnapshotName(conf);
			Path snapshotDir = SnapshotDescriptionUtils.getCompletedSnapshotDir(snapshotName, rootDir);

			// load region descriptor
			String regionName = this.split.regionName;
			Path regionDir = new Path(snapshotDir, regionName);
			HRegionInfo hri = HRegion.loadDotRegionInfoFileContent(fs, regionDir);

			// region is immutable, this should be fine, otherwise we have to set the
			// thread read point...
			scan.setIsolationLevel(IsolationLevel.READ_UNCOMMITTED);

			// load table descriptor
			HTableDescriptor htd = FSTableDescriptors.getTableDescriptor(fs, snapshotDir);
			Path tableDir = new Path(conf.get(TABLE_DIR_KEY));

			// open region from the snapshot directory
			this.region = openRegion(tableDir, fs, conf, hri, htd);

			// create region scanner
			this.scanner = region.getScanner(scan);
			values = new ArrayList<KeyValue>();
			this.more = true;

			region.startRegionOperation();
		}

		private HRegion openRegion(final Path tableDir, final FileSystem fs, final Configuration conf, 
				final HRegionInfo hri, final HTableDescriptor htd) throws IOException {
			HRegion r = HRegion.newHRegion(tableDir, null, fs, conf, hri, htd, null);
			r.initialize(null);
			return r;
		}

		@Override
		public boolean nextKeyValue() throws IOException, InterruptedException {
			values.clear();
			// RegionScanner.next() has a different contract than
			// RecordReader.nextKeyValue(). Scanner
			// indicates no value read by returning empty results. Returns boolean
			// indicates if more
			// rows exist AFTER this one
			if (!more) {
				return false;
			}
			more = scanner.nextRaw(values, scan.getBatch(), null);
			if (values == null || values.isEmpty()) {
				// we are done
				return false;
			}

			this.result = new Result(values);
			if (this.row == null) {
				this.row = new ImmutableBytesWritable();
			}
			this.row.set(result.getRow());

			return true;
		}

		@Override
		public ImmutableBytesWritable getCurrentKey() throws IOException, InterruptedException {
			return row;
		}

		@Override
		public Result getCurrentValue() throws IOException, InterruptedException {
			return result;
		}

		@Override
		public float getProgress() throws IOException, InterruptedException {
			return 0;
		}

		@Override
		public void close() throws IOException {
			try {
				if (this.scanner != null) {
					this.scanner.close();
				}
			} finally {
				if (region != null) {
					region.closeRegionOperation();
					region.close(true);
				}
			}
		}
	}

	@Override
	public RecordReader<ImmutableBytesWritable, Result> createRecordReader(final InputSplit split, 
			final TaskAttemptContext context) throws IOException {
		return new SnapshotRegionRecordReader();
	}

	@Override
	public List<InputSplit> getSplits(final JobContext job) throws IOException, InterruptedException {
		Configuration conf = job.getConfiguration();
		String snapshotName = getSnapshotName(job.getConfiguration());

		Path rootDir = new Path(conf.get(HConstants.HBASE_DIR));
		FileSystem fs = rootDir.getFileSystem(conf);

		Path snapshotDir = SnapshotDescriptionUtils.getCompletedSnapshotDir(snapshotName, rootDir);

		Set<String> snapshotRegionNames = SnapshotReferenceUtil.getSnapshotRegionNames(fs, snapshotDir);
		if (snapshotRegionNames == null) {
			throw new IllegalArgumentException("Snapshot is empty");
		}

		// load table descriptor
		HTableDescriptor htd = FSTableDescriptors.getTableDescriptor(fs, snapshotDir);

		Scan scan = CustomizedUtil.convertStringToScan(conf.get(TableInputFormat.SCAN));

		List<InputSplit> splits = new ArrayList<InputSplit>(snapshotRegionNames.size());
		for (String regionName : snapshotRegionNames) {
			// load region descriptor
			Path regionDir = new Path(snapshotDir, regionName);
			HRegionInfo hri = HRegion.loadDotRegionInfoFileContent(fs, regionDir);

			if (keyRangesOverlap(scan.getStartRow(), scan.getStopRow(),hri.getStartKey(), hri.getEndKey())) {
				List<String> hosts = HRegion.computeHDFSBlocksDistribution(conf, htd,hri.getEncodedName()).getTopHosts();
				splits.add(new SnapshotRegionSplit(regionName, hosts));
			}
		}
		return splits;
	}

	private boolean keyRangesOverlap(final byte[] start1, final byte[] end1, final byte[] start2, final byte[] end2) {
		return (end2.length == 0 || start1.length == 0 || Bytes.compareTo(start1,end2) < 0)
				&& (end1.length == 0 || start2.length == 0 || Bytes.compareTo(start2,end1) < 0);
	}

	/**
	 * Set job input.
	 * @param job The job
	 * @param snapshotName The snapshot name
	 * @param tableRootDir The directory where the temp table will be created
	 * @throws IOException on error
	 */
	public static void setInput(final Configuration jobconf, final String snapshotName, final Path tableRootDir) throws IOException {
		Configuration conf = jobconf;
		conf.set(SNAPSHOT_NAME_KEY, snapshotName);

		Path rootDir = new Path(conf.get(HConstants.HBASE_DIR));
		FileSystem fs = rootDir.getFileSystem(conf);

		Path snapshotDir = SnapshotDescriptionUtils.getCompletedSnapshotDir(snapshotName, rootDir);
		SnapshotDescription snapshotDesc = SnapshotDescriptionUtils.readSnapshotInfo(fs, snapshotDir);

		// load table descriptor
		HTableDescriptor htd = FSTableDescriptors.getTableDescriptor(fs, snapshotDir);

		Path tableDir = new Path(tableRootDir, htd.getNameAsString());
		conf.set(TABLE_DIR_KEY, tableDir.toString());

		MonitoredTask status = TaskMonitor.get().createStatus("Restoring	snapshot '" + snapshotName + "' to directory " + tableDir);
		ForeignExceptionDispatcher monitor = new ForeignExceptionDispatcher();

		RestoreSnapshotHelper helper = new RestoreSnapshotHelper(conf, fs,snapshotDesc, snapshotDir, htd, tableDir, monitor, status);
		helper.restoreHdfsRegions();
	}

	private static String getSnapshotName(final Configuration conf) {
		String snapshotName = conf.get(SNAPSHOT_NAME_KEY);
		if (snapshotName == null) {
			throw new IllegalArgumentException("Snapshot name must be provided");
		}
		return snapshotName;
	}

	private Configuration configuration;
	public void setConf(Configuration configuration) {
		this.configuration = configuration;
	}
	public Configuration getConf() {
		return this.configuration;
	}
}