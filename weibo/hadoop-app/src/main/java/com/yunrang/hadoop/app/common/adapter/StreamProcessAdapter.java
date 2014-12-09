package com.yunrang.hadoop.app.common.adapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileContext;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.Options.Rename;
import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper.Context;

@SuppressWarnings("rawtypes")
public class StreamProcessAdapter {
	static final Log LOG = LogFactory.getLog(StreamProcessAdapter.class);
	
	private final FsPermission cachePerms = new FsPermission((short) 0755);
	private final Configuration conf;
	
	private Context mprContext;
	private FileContext files;
	private String jobName;
	final private Path srcFilePath;
	final private Path destDirPath;
	
	private OutputStreamWriter outputToServiceProcessWriter;
	private InputStreamReader inputFromServiceProcessReader;
	
	private Process serviceProcess; 
	
	private boolean isOutputStart = false;
	private Long lastOutputTime = System.currentTimeMillis();
	
	public static final Integer DEFAULT_CLEANUP_WAITSECOND = 20;
	private Integer cleanupWaitSecond;
	
	private String jobCachePath = "/var/yr/localcache/hadoop-job-local-cache/null";
	public StreamProcessAdapter(Context context, Path srcFilePath, Integer cleanupWaitSecond) throws Exception {
		this.mprContext = context;
		this.files = FileContext.getLocalFSFileContext();
		this.conf = mprContext.getConfiguration();
		this.jobName = mprContext.getJobName();
		this.srcFilePath = srcFilePath;
		this.jobCachePath = "/var/yr/localcache/hadoop-job-local-cache/"+this.jobName;
		this.destDirPath = new Path(this.jobCachePath + "/resource");
		if (cleanupWaitSecond > 0) {
			this.cleanupWaitSecond = cleanupWaitSecond;			
		} else {
			this.cleanupWaitSecond = DEFAULT_CLEANUP_WAITSECOND;
		}
	}
	
	public StreamProcessAdapter(Context context, Path srcFilePath) throws Exception {
		this(context, srcFilePath, DEFAULT_CLEANUP_WAITSECOND);
	}
	
	public void start() throws Exception {
		File jobCacheDir = new File(this.jobCachePath);
		jobCacheDir.mkdirs();
		
		File initStartLock = new File(this.jobCachePath+"/initStartLock_"+this.mprContext.getJobID()+".lk");
		File initCompleteFlag = new File(this.jobCachePath+"/initComplete_"+this.mprContext.getJobID()+".lk");
		
		for (File f : jobCacheDir.listFiles()) {
			if (f.getName().endsWith(".lk")) {
				if ( ! f.getName().contains(this.mprContext.getJobID().toString())) {
					f.delete();
				}
			}
		}
		
		Thread.sleep(new Random().nextInt(10000));
		
		while (true) {
			if (initCompleteFlag.exists()) {
				break;
			} else {
				if (initStartLock.createNewFile()) {
					try {
						this.runLocalization();
						initCompleteFlag.createNewFile();
						break;
					} catch (Exception e) {
						initStartLock.delete();
					}
				}
			}
			Thread.sleep(1000);
		}
		this.runLocalService();
	}

	void setOutputStart() {
		this.isOutputStart = true;
		this.lastOutputTime = System.currentTimeMillis();
	}
	
	void initOutputPipe(final InputStreamReader ifspr) {
		new Thread() {
			@SuppressWarnings("unchecked")
			public void run() {
				if (ifspr != null) {
					BufferedReader br = new BufferedReader(ifspr);
					String line = null;
					try {
						while ((line = br.readLine()) != null) {
							try {
								setOutputStart();
								mprContext.write(new Text(line), new Text(""));
							} catch (Exception e) {}
						}
					} catch (Exception e) {
					} finally {
						try {
							ifspr.close();
						} catch (IOException e) {}
					}
				}
			}
		}.start();
	}
	
	public void runLocalService() throws Exception {
		String serviceShellCmd = this.destDirPath+"/service.sh";
		this.serviceProcess = Runtime.getRuntime().exec(serviceShellCmd);
		this.outputToServiceProcessWriter = new OutputStreamWriter(serviceProcess.getOutputStream());
		this.inputFromServiceProcessReader = new InputStreamReader(serviceProcess.getInputStream());
		this.initOutputPipe(this.inputFromServiceProcessReader);
	}
	
	public void schedule(Text key, Text value) throws Exception {
		if (this.outputToServiceProcessWriter != null) {
			this.outputToServiceProcessWriter.write(value.toString()+"\n");
		}
	}

	public void cleanup() throws Exception {
		if (this.outputToServiceProcessWriter != null) {
			try {
				this.outputToServiceProcessWriter.flush();
			} finally {
				while (true) {
					if (this.isOutputStart) {
						if (System.currentTimeMillis() - this.lastOutputTime > cleanupWaitSecond * 1000) {
							break;
						}
					}
					this.outputToServiceProcessWriter.flush();
					Thread.sleep(1000);
				}
				this.outputToServiceProcessWriter.close();
			}
		}
	}
	
	void runLocalization() throws Exception {
		localizeFiles();
	}

	void localizeFiles() throws Exception {
		refreshDir(destDirPath, cachePerms);
		final Path dst_work = new Path(destDirPath + "_tmp");
		refreshDir(dst_work, cachePerms);
		try {
			Path dTmp = files.makeQualified(copy(srcFilePath, dst_work));
			unpack(new File(dTmp.toUri()), new File(dst_work.toString()));
			files.rename(dst_work, destDirPath, Rename.OVERWRITE);
		} finally {
			files.delete(dst_work, true);
		}
		files.makeQualified(new Path(destDirPath, srcFilePath.getName()));
	}
	
	void unpack(File localrsrc, File dst) throws Exception {
		FileUtil.unTar(localrsrc, dst);
		if (localrsrc.isFile()) {
			files.delete(new Path(localrsrc.toString()), false);
		}
	}
	
	public Path copy(Path sCopy, Path dstdir) throws Exception {
		FileSystem sourceFs = sCopy.getFileSystem(conf);
		Path dCopy = new Path(dstdir, "tmp_" + sCopy.getName());
		sourceFs.copyToLocalFile(sCopy, dCopy);
		return dCopy;
	}
	
	void refreshDir(Path path, FsPermission perm) throws Exception {
		try {
			files.delete(path, true);
		} catch(Exception e) {}
		files.mkdir(path, perm, false);
		if (!perm.equals(files.getUMask().applyUMask(perm))) {
			files.setPermission(path, perm);
		}
	}
}