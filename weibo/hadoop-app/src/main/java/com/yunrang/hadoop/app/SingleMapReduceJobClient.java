package com.yunrang.hadoop.app;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SingleMapReduceJobClient extends Configured implements Tool {
	private static Logger LOG = LoggerFactory.getLogger(SingleMapReduceJobClient.class);
	public static final String JOBDEFINE_TAG = "SMRDefinition";
	
	public static final String LIST_CMD = "list";
	public static final String SUBMITBYID_CMD = "submitById";
	public static final String SUBMITBYCLASSNAME_CMD = "submitByName";
	public static final String REDUCER_CMD = "reducerNum";
	public static final String PAY_LOAD = "payLoad";
	public static final String HELP_CMD = "help";
	
	
	private String JOB_PLAN_SCAN_PACKAGE_SCOPE;
	private Class<?> SCAN_FILTER_CLASS;
	private List<Class<?>> JOB_CLASSES_CACHE;

	
	public static final String SUB_PACKAGE_LIMIT = "job";
	public SingleMapReduceJobClient() throws Exception {
		if (SUB_PACKAGE_LIMIT != null && SUB_PACKAGE_LIMIT.length()>0) {
			JOB_PLAN_SCAN_PACKAGE_SCOPE = this.getClass().getPackage().getName()+"."+SUB_PACKAGE_LIMIT;
		} else {
			JOB_PLAN_SCAN_PACKAGE_SCOPE = this.getClass().getPackage().getName();
		}
		Class<?>[] memberClasses = this.getClass().getClasses();
		for (Class<?> clazz : memberClasses) {
			String sn = clazz.getSimpleName();
			if (!sn.isEmpty() && sn.equals(JOBDEFINE_TAG)) {
				SCAN_FILTER_CLASS = clazz;
				break;
			}
		}
		if (SCAN_FILTER_CLASS == null) {
			throw new Exception("initialize SCAN_FILTER_CLASS failed");
		}
		JOB_CLASSES_CACHE = PackScanner.getMatchedBizClassList(JOB_PLAN_SCAN_PACKAGE_SCOPE, SCAN_FILTER_CLASS);
	}
	
	public static abstract class SMRDefinition {
		public static final String DEFAULT_JOB_CACHE_PATH = "/tmp/hadoop-job-distributed-cache";
		public static final String DEFAULT_FIELD_SEP = "#yR@_#";
		public static final String DEFAULT_PAYLOAD_SEP = "@_@";
		public static final Integer DEFAULT_REDUCE_NUM = 100;
		public static final Integer ZERO_REDUCE_NUM = 0;
		public static final Integer SING_REDUCE_NUM = 1;
		public abstract void doConfiguration(JobSubmitContext jobSubmitContext, Job currJob) throws Exception;
		/**
		 * If we have specific requirement rather than default conf to create current job. 
		 * Override this method.
		 */
		protected Configuration getConf(Configuration defaultConfig) {
			return defaultConfig;
		}
	}
	
	public static void main(String[] args) throws Exception {
		ToolRunner.run(new SingleMapReduceJobClient(), args);
	}
	
	@Override
	public int run(String[] args) throws Exception {
		Options opts = new Options();
		opts.addOption(LIST_CMD, false, "List all jobs' id and name.");
		opts.addOption(HELP_CMD, false, "Displays all commands.");
		
		opts.addOption(SUBMITBYID_CMD, true, "Submit job by listed id");
		opts.getOption(SUBMITBYID_CMD).setArgName("Job Id");
		
		opts.addOption(SUBMITBYCLASSNAME_CMD, true, "Submit job by listed name");
		opts.getOption(SUBMITBYCLASSNAME_CMD).setArgName("Job Name");
		
		opts.addOption(REDUCER_CMD, true, "Job's Reducers Number");
		opts.getOption(REDUCER_CMD).setArgName("Reducers Number");
		
		opts.addOption(PAY_LOAD, true, "Pay_Load Info for Task sepcific");
		opts.getOption(PAY_LOAD).setArgName("None Space Sperate String");
		
		CommandLine cliParser = null;
		JobSubmitContext jsoc = new JobSubmitContext();
		try {
			cliParser = new GnuParser().parse(opts, args);
			if (cliParser.hasOption(LIST_CMD)) {
				printCandidateJobClassInfo();
			} else 
			if (cliParser.hasOption(HELP_CMD)) {
				printHelpInfo(opts);
			} else
			if (cliParser.hasOption(SUBMITBYID_CMD)) {
				String jobID = cliParser.getOptionValue(SUBMITBYID_CMD);
				Class<?> jobDefinitionClazz = findJobClassById(jobID);
				if (jobDefinitionClazz != null) {
					jsoc.setJobDefinitionClazz(jobDefinitionClazz);
					configJobSubmitContextFromOption(jsoc, cliParser);
					return submitJob(jsoc);
				} else {
					printCandidateJobClassInfo();
					System.err.println("Job submitting Error!: Can not submit job by id : [" + jobID +"]");
					return 1;
				}
			} else
			if (cliParser.hasOption(SUBMITBYCLASSNAME_CMD)) {
				String jobClassName = cliParser.getOptionValue(SUBMITBYCLASSNAME_CMD);
				Class<?> jobDefinitionClazz = findJobClassByClassName(jobClassName);
				if (jobDefinitionClazz != null) {
					jsoc.setJobDefinitionClazz(jobDefinitionClazz);
					configJobSubmitContextFromOption(jsoc, cliParser);
					return submitJob(jsoc);
				} else {
					printCandidateJobClassInfo();
					System.err.println("Job submitting Error!: Can not submit job by name : [" + jobClassName + "]");
					return 1;
				}
			} else {
				printHelpInfo(opts);
			}
	    } catch (Exception ex) {
	    	System.err.println("Option parsing error...:" + ex.getMessage());
	    	printHelpInfo(opts);
	    	return 1;
	    }
	    return 0;
	}
	
	public static class JobSubmitContext {
		private Integer reducerNumber = -1;
		private Class<?> jobDefinitionClazz = null;
		private String payLoad = "";
		public String getPayLoad() {
			return payLoad.trim();
		}
		public void setPayLoad(String payLoad) {
			this.payLoad = payLoad;
		}
		public Integer getReducerNumber() {
			return reducerNumber;
		}
		public void setReducerNumber(Integer reducerNumber) {
			this.reducerNumber = reducerNumber;
		}
		public Class<?> getJobDefinitionClazz() {
			return jobDefinitionClazz;
		}
		public void setJobDefinitionClazz(Class<?> jobDefinitionClazz) {
			this.jobDefinitionClazz = jobDefinitionClazz;
		}
	}
	
	private void configJobSubmitContextFromOption(JobSubmitContext jobSubmitContext, CommandLine cliParser) throws Exception {
		if (cliParser.hasOption(REDUCER_CMD)) {
			String reducerNumStr = cliParser.getOptionValue(REDUCER_CMD);
			Integer reducerNum = Integer.parseInt(reducerNumStr);
			if (reducerNum >= 0) {
				jobSubmitContext.setReducerNumber(reducerNum);
			} else {
				throw new Exception("the Option input reduce number should not fewer than 0. curr value is [" + reducerNum + "]");
			}
		}
		if (cliParser.hasOption(PAY_LOAD)) {
			String payLoad = cliParser.getOptionValue(PAY_LOAD);
			if (!StringUtils.isEmpty(payLoad)) {
				LOG.info("get payLoad:["+payLoad+"]");
				jobSubmitContext.setPayLoad(payLoad);
			} else {
				throw new Exception("the Option input payload info is empty.");
			}
		}
	}
	
	private void configJobFromOption(JobSubmitContext jobSubmitContext, Job job) {
		//Reducer Number Overwrite...
		if (jobSubmitContext.getReducerNumber() >= 0) {
			LOG.info("NOTE ReducerNumber is overwrited by value from option input, current value is ["+
					jobSubmitContext.getReducerNumber() +"]");
			try {
				if (job.getReducerClass() != null) {
					job.setNumReduceTasks(jobSubmitContext.getReducerNumber());
				}
			} catch (Exception e) {
				LOG.info("NOTE Fail to get reducer class, give up to overwrite reducer number from option input");
			}
		}
	}
	
	private int submitJob(JobSubmitContext jobSubmitContext) {
		try {
			Class<?> jobDefinitionClazz = jobSubmitContext.getJobDefinitionClazz();
			LOG.info("Submitted JobDefinition Class Name is : " + jobDefinitionClazz.getSimpleName());
			SMRDefinition jobDefinition = (SMRDefinition) jobDefinitionClazz.newInstance();
			Configuration defaultConf = getConf();
			Job job = Job.getInstance(jobDefinition.getConf(defaultConf));
			
			Class<SingleMapReduceJobClient> jobJarMainClass = SingleMapReduceJobClient.class;
			job.setJarByClass(jobJarMainClass);
			
			String defaultJobName = "ads-hadoop-tentativce-mapreduce-job";
			job.setJobName(defaultJobName);
			
			LOG.info("Begin to configurate job");
			jobDefinition.doConfiguration(jobSubmitContext, job);
			
			LOG.info("Begin to configurate job from jobSubmitContext");
			configJobFromOption(jobSubmitContext, job);
			
			showJobProfile(job);
			
			return job.waitForCompletion(true) ? 0 : 1;
		} catch (Exception e) {
			e.printStackTrace();
			LOG.warn("JobSubmitException  : " + e.getMessage());
			return 1;
		}
	}
	
	private void showJobProfile(Job job) throws Exception {
		LOG.info("JobName : " + job.getJobName());
		LOG.info("JarByClass : " + job.getJar());
		LOG.info("InputFormatClass : " + job.getInputFormatClass());
		LOG.info("MapperClass : " + job.getMapperClass());
		LOG.info("MapOutputKeyClass : " + job.getMapOutputKeyClass());
		LOG.info("MapOutputValueClass : " + job.getMapOutputValueClass());
		LOG.info("ReducerClass : " + job.getReducerClass());
		LOG.info("NumReduceTasks : " + job.getNumReduceTasks());
		LOG.info("OutputFormatClass : " + job.getOutputFormatClass());
	}
	
	private void printHelpInfo(Options opts) {
		new HelpFormatter().printHelp("mapreduce job submitter", opts);
	}
	
	private void printCandidateJobClassInfo() {
		System.out.println("-------- CANDICATE JOB INFO LIST ---------");
		System.out.println("JOB_ID:		JOB_NAME:");
		if (this.JOB_CLASSES_CACHE.size() > 0) {
			for (int i=0; i<JOB_CLASSES_CACHE.size(); i++) {
				Integer classId = i;
				String className = JOB_CLASSES_CACHE.get(i).getSimpleName();
				System.out.println(classId + "		"+className);
			}
		} else {
			System.out.println("Empty...");
		}
		System.out.println("------------------------------------------");
	}
	
	private Class<?> findJobClassByClassName(String jobName) {
		Class<?> resultClass = null;
		for (Class<?> clazz : this.JOB_CLASSES_CACHE) {
			if (clazz.getSimpleName().equalsIgnoreCase(jobName)) {
				resultClass = clazz;
				break;
			}
		}
		return resultClass;
	}
	
	private Class<?> findJobClassById(String jobId) {
		Class<?> resultClass = null;
		try {
			Integer index = Integer.parseInt(jobId);
			if (index >= 0 && index < this.JOB_CLASSES_CACHE.size()) {
				resultClass = this.JOB_CLASSES_CACHE.get(index);
			}
		} catch (Exception e) {}
		return resultClass;
	}
	
	private static class PackScanner {
		static List<Class<?>> getMatchedBizClassList(String rootPack, Class<?> filter) {
			Set<Class<?>> classes = getClasses(rootPack);
			List<Class<?>> list = new ArrayList<Class<?>>();
			for (Class<?> clz : classes) {
				if (filter.isAssignableFrom(clz)) {
					if (!Modifier.isInterface(clz.getModifiers())) {
						if (!Modifier.isAbstract(clz.getModifiers())){
							list.add(clz);
						}
					}
				}
			} 
			return list;
		}
		static void findAndAddClassesInPackageByFile(String packageName, String packagePath, final boolean recursive, Set<Class<?>> classes) {
			File dir = new File(packagePath);
			if (!dir.exists() || !dir.isDirectory()) {
				return;
			}
			File[] dirfiles = dir.listFiles(new FileFilter() {
				public boolean accept(File file) {
					return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));
				}
			});
			for (File file : dirfiles) {
				if (file.isDirectory()) {
				} else {
					findAndAddClassesInPackageByFile(packageName + "."+ file.getName(), file.getAbsolutePath(), recursive,classes);
					String className = file.getName().substring(0, file.getName().length() - 6);
					try {
						classes.add(Thread.currentThread().getContextClassLoader().loadClass(packageName + '.' + className));
					} catch (ClassNotFoundException e) {
						LOG.error("PackageScaner exception D : ", e);
					}
				}
			}
		}
		static Set<Class<?>> getClasses(String pack) {
			Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
			boolean recursive = true;
			String packageName = pack;
			String packageDirName = packageName.replace('.', '/')+"/";
			Enumeration<URL> dirs;
			try {
				dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
				while (dirs.hasMoreElements()) {
					URL url = dirs.nextElement();
					String protocol = url.getProtocol();
					if ("file".equals(protocol)) {
						String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
						findAndAddClassesInPackageByFile(packageName, filePath, recursive, classes);
					} else if ("jar".equals(protocol)) {
						JarFile jar;
						try {
							jar = ((JarURLConnection) url.openConnection()).getJarFile();
							Enumeration<JarEntry> entries = jar.entries();
							while (entries.hasMoreElements()) {
								JarEntry entry = entries.nextElement();
								String name = entry.getName();
								if (name.charAt(0) == '/') {
									name = name.substring(1);
								}
								if (name.startsWith(packageDirName)) {
									int idx = name.lastIndexOf('/');
									if (idx != -1) {
										packageName = name.substring(0, idx).replace('/', '.');
									}
									if ((idx != -1) || recursive) {
										if (name.endsWith(".class") && !entry.isDirectory()) {
											String className = name.substring(packageName.length() + 1, name.length() - 6);
											try {
												classes.add(Class.forName(packageName + '.'+ className));
											} catch (ClassNotFoundException e) {
												LOG.error("PackageScaner exception A : ", e);
											}
										}
									}
								}
							}
						} catch (IOException e) {
							LOG.error("PackageScaner exception B : ", e);
						}
					}
				}
			} catch (IOException e) {
				LOG.error("PackageScaner exception C: ", e);
			}
			return classes;
		}
	}
}
