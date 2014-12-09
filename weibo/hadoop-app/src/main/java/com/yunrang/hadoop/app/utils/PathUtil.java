package com.yunrang.hadoop.app.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;
import org.apache.hadoop.fs.RemoteIterator;
import org.apache.hadoop.mapreduce.JobContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PathUtil {
	private static Logger LOG = LoggerFactory.getLogger(PathUtil.class);
	public static final String[] DEFAULT_EXCLUDE_PATHS = new String[]{"_SUCCESS","_logs"};
	
	public static String normalizePathStr(String pathStr) {
		return StringUtils.trimToNull(pathStr.endsWith("/") ? pathStr.substring(0, pathStr.length()-1) : pathStr);
	}
	
	public static Path[] detectRecursively(JobContext job, String paths) throws Exception {
		String[] pStrs = paths.split(",");
		return detectRecursively(job, pStrs);
	}
	
	public static Path[] detectRecursively(JobContext job, String... dirs) throws Exception {
		return detectRecursively(job, null, dirs);
	}
	
	public static Path[] detectRecursively(JobContext job, Class<? extends PathFilter> filterClazz, String paths) throws Exception {
		String[] pStrs = paths.split(",");
		return detectRecursively(job, filterClazz, pStrs);
	}
	
	public static Path[] detectRecursively(JobContext job, Class<? extends PathFilter> filterClazz, String... dirs) throws Exception {
		PathFilter filter = null;
		if (dirs.length == 0) {
			throw new IOException("No input paths specified in job");
		}
		if (filterClazz != null) {
			filter = filterClazz.newInstance();
		}
		List<Path> result = new ArrayList<Path>();
		for (String pStr : dirs) {
			LOG.info("PathUtils: process one input root path["+pStr+"]");
			Path p = new Path(pStr);
			FileSystem fs = p.getFileSystem(job.getConfiguration());
			addInputPathRecursively(result, fs, p, filter);
		}
		if (result.size() == 0) {
			throw new Exception("There is no qualified input file pathes selected, plz check you input path or filter policy.");
		}
		return result.toArray(new Path[result.size()]);
	}

	static private boolean isExcludedPath(Path path) {
		for (String ex : DEFAULT_EXCLUDE_PATHS) {
			if (path.getName().equals(ex)) {
				return true;
			}
		}
		return false;
	}
	
	static private void addInputPathRecursively(List<Path> result, FileSystem fs, Path path, PathFilter filter) throws Exception {
		RemoteIterator<LocatedFileStatus> iter = fs.listLocatedStatus(path);
		while (iter.hasNext()) {
			LocatedFileStatus stat = iter.next();
			if (stat.isDirectory()) {
				Path childPath = stat.getPath();
				if (isExcludedPath(childPath) == false) {
					addInputPathRecursively(result, fs, childPath, filter);
				}
			} else {
				Path target = stat.getPath();
				if (isExcludedPath(target) == false) {
					if (filter != null) {
						if (filter.accept(target)) {
							result.add(target);
						}
					} else {
						result.add(target);
					}
				}
			}
		}
	}
}
