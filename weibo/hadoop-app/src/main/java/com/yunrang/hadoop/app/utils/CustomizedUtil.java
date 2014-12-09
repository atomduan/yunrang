package com.yunrang.hadoop.app.utils;

/**
 * Copyright 2008 The Apache Software Foundation
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.mapreduce.TableInputFormat;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Base64;
import org.apache.hadoop.mapreduce.Job;

import com.yunrang.hadoop.app.common.input.snapshot.SnapshotInputFormat;

/**
 * Utility for {@link TableMapper} and {@link TableReducer}
 */
@SuppressWarnings({"deprecation", "resource" })
public class CustomizedUtil {
	public static Log LOG = LogFactory.getLog(CustomizedUtil.class);

	@SuppressWarnings("rawtypes")
	public static void initSnapshotMapperJob(String snapshotName,
			Path tableRootDir, Scan scan, Class<? extends TableMapper> mapper,
			Class<?> outputKeyClass, Class<?> outputValueClass, Job job,
			boolean addDependencyJars) throws IOException {
		SnapshotInputFormat.setInput(job.getConfiguration(), snapshotName, tableRootDir);
		Configuration conf = job.getConfiguration();
		job.setInputFormatClass(SnapshotInputFormat.class);
		if (outputValueClass != null) {
			job.setMapOutputValueClass(outputValueClass);
		}
		if (outputKeyClass != null) {
			job.setMapOutputKeyClass(outputKeyClass);
		}
		job.setMapperClass(mapper);
		conf.set(TableInputFormat.SCAN, convertScanToString(scan));
		if (addDependencyJars) {
			TableMapReduceUtil.addDependencyJars(job);
		}
	}
	
	/**
	 * Writes the given scan into a Base64 encoded string.
	 * 
	 * @param scan
	 *            The scan to write out.
	 * @return The scan saved in a Base64 encoded string.
	 * @throws IOException
	 *             When writing the scan fails.
	 */
	public static String convertScanToString(Scan scan) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(out);
		scan.write(dos);
		return Base64.encodeBytes(out.toByteArray());
	}

	/**
	 * Converts the given Base64 string back into a Scan instance.
	 * 
	 * @param base64
	 *            The scan details.
	 * @return The newly created Scan instance.
	 * @throws IOException
	 *             When reading the scan instance fails.
	 */
	public static Scan convertStringToScan(String base64) throws IOException {
		ByteArrayInputStream bis = new ByteArrayInputStream(Base64.decode(base64));
		DataInputStream dis = new DataInputStream(bis);
		Scan scan = new Scan();
		scan.readFields(dis);
		return scan;
	}

	/**
	 * Ensures that the given number of reduce tasks for the given job
	 * configuration does not exceed the number of regions for the given table.
	 * 
	 * @param table
	 *            The table to get the region count for.
	 * @param job
	 *            The current job to adjust.
	 * @throws IOException
	 *             When retrieving the table details fails.
	 */
	public static void limitNumReduceTasks(String table, Job job) throws IOException {
		HTable outputTable = new HTable(job.getConfiguration(), table);
		int regions = outputTable.getRegionsInfo().size();
		if (job.getNumReduceTasks() > regions)
			job.setNumReduceTasks(regions);
	}

	/**
	 * Sets the number of reduce tasks for the given job configuration to the
	 * number of regions the given table has.
	 * 
	 * @param table
	 *            The table to get the region count for.
	 * @param job
	 *            The current job to adjust.
	 * @throws IOException
	 *             When retrieving the table details fails.
	 */
	
	public static void setNumReduceTasks(String table, Job job) throws IOException {
		HTable outputTable = new HTable(job.getConfiguration(), table);
		int regions = outputTable.getRegionsInfo().size();
		job.setNumReduceTasks(regions);
	}

	/**
	 * Sets the number of rows to return and cache with each scanner iteration.
	 * Higher caching values will enable faster mapreduce jobs at the expense of
	 * requiring more heap to contain the cached rows.
	 * 
	 * @param job
	 *            The current job to adjust.
	 * @param batchSize
	 *            The number of rows to return in batch with each scanner
	 *            iteration.
	 */
	public static void setScannerCaching(Job job, int batchSize) {
		job.getConfiguration().setInt("hbase.client.scanner.caching", batchSize);
	}

	/**
	 * Returns a classpath string built from the content of the "tmpjars" value
	 * in {@code conf}. Also exposed to shell scripts via `bin/hbase mapredcp`.
	 */
	public static String buildDependencyClasspath(Configuration conf) {
		if (conf == null) {
			throw new IllegalArgumentException("Must provide a configuration object.");
		}
		Set<String> paths = new HashSet<String>(conf.getStringCollection("tmpjars"));
		if (paths.size() == 0) {
			throw new IllegalArgumentException("Configuration contains no tmpjars.");
		}
		StringBuilder sb = new StringBuilder();
		for (String s : paths) {
			// entries can take the form 'file:/path/to/file.jar'.
			int idx = s.indexOf(":");
			if (idx != -1)
				s = s.substring(idx + 1);
			if (sb.length() > 0)
				sb.append(File.pathSeparator);
			sb.append(s);
		}
		return sb.toString();
	}

	/**
	 * Add entries to <code>packagedClasses</code> corresponding to class files
	 * contained in <code>jar</code>.
	 * 
	 * @param jar
	 *            The jar who's content to list.
	 * @param packagedClasses
	 *            map[class -> jar]
	 */
	public static void updateMap(String jar,Map<String, String> packagedClasses) throws IOException {
		if (null == jar || jar.isEmpty()) {
			return;
		}
		ZipFile zip = null;
		try {
			zip = new ZipFile(jar);
			for (Enumeration<? extends ZipEntry> iter = zip.entries(); iter.hasMoreElements();) {
				ZipEntry entry = iter.nextElement();
				if (entry.getName().endsWith("class")) {
					packagedClasses.put(entry.getName(), jar);
				}
			}
		} finally {
			if (null != zip)
				zip.close();
		}
	}

	/**
	 * Find a jar that contains a class of the same name, if any. It will return
	 * a jar file, even if that is not the first thing on the class path that
	 * has a class with the same name. Looks first on the classpath and then in
	 * the <code>packagedClasses</code> map.
	 * 
	 * @param my_class the class to find.
	 * @return a jar file that contains the class, or null.
	 * @throws IOException
	 */
	public static String findContainingJar(Class<?> my_class, Map<String, String> packagedClasses) throws IOException {
		ClassLoader loader = my_class.getClassLoader();
		String class_file = my_class.getName().replaceAll("\\.", "/") + ".class";
		// first search the classpath
		for (Enumeration<URL> itr = loader.getResources(class_file); itr.hasMoreElements();) {
			URL url = itr.nextElement();
			if ("jar".equals(url.getProtocol())) {
				String toReturn = url.getPath();
				if (toReturn.startsWith("file:")) {
					toReturn = toReturn.substring("file:".length());
				}
				// URLDecoder is a misnamed class, since it actually decodes
				// x-www-form-urlencoded MIME type rather than actual
				// URL encoding (which the file path has). Therefore it would
				// decode +s to ' 's which is incorrect (spaces are actually
				// either unencoded or encoded as "%20"). Replace +s first, so
				// that they are kept sacred during the decoding process.
				toReturn = toReturn.replaceAll("\\+", "%2B");
				toReturn = URLDecoder.decode(toReturn, "UTF-8");
				return toReturn.replaceAll("!.*$", "");
			}
		}
		// now look in any jars we've packaged using JarFinder. Returns null
		// when
		// no jar is found.
		return packagedClasses.get(class_file);
	}
}