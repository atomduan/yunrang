package com.yunrang.location.tasklet.mapreduce;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;
import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.OutputFormat;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yunrang.location.common.util.UtilPackageScan;
import com.yunrang.location.tasklet.util.UtilMapReduceInteractive;


@SuppressWarnings("unchecked")
final public class SingleMapReduceJobDelegate extends Configured implements Tool {
	
	private static Logger LOG = LoggerFactory.getLogger(SingleMapReduceJobDelegate.class);
	private static final String JOB_PLAN_SCAN_PACKAGE_SCOPE = SingleMapReduceJobDelegate.class.getPackage().getName();
	
	private Class<? extends Mapper> mapperClass;
	private Class<? extends Reducer> reducerClass;
	private Class<? extends InputFormat> inputFormatClass;
	private Class<? extends OutputFormat> outputFormatClass;
	private Class<?> mapOutputKeyClass;
	private Class<?> mapOutputValueClass;
	
	private PathFilter inputFilePathFilter;
	private String jobInputPath;
	private String jobOutputPath;
	private Integer reduceTaskNum;
	
	public static interface SingleMapReduceJobPlan {
		public void customizeJob(SingleMapReduceJobDelegate delegate) throws Exception;
	}
	
	public int run(String[] args) {
		LOG.info("Process MapReduceJobDelegate......");
		//Warn: There are some tricking reflections in it, implement "Tool" is "have to", or else a null will be returned.
		Configuration configuration = getConf();
		try {
			FileSystem fileSystem = FileSystem.get(configuration);
			if (args.length == 2) {
				String jobPlanClassFormalName = args[0].trim();
				String jobPlanReducerTaskNumStr = args[1].trim();
				LOG.info("jobPlanFormalClassName:"+jobPlanClassFormalName);
				Class<?> clazz = UtilPackageScan.getFirstMatchClass(JOB_PLAN_SCAN_PACKAGE_SCOPE, jobPlanClassFormalName);
				if (clazz!=null && SingleMapReduceJobPlan.class.isAssignableFrom(clazz)) {
					SingleMapReduceJobPlan jobPlan = (SingleMapReduceJobPlan)clazz.newInstance();
					jobPlan.customizeJob(this);
					// Begin to total config....
					LOG.info("delete PlanOutputPath["+jobOutputPath+"] on HDFS if it is exsit");
					fileSystem.delete(new Path(jobOutputPath), true);
					
					LOG.info("create job name:"+UtilPackageScan.simpleClassNameProcess(jobPlanClassFormalName));
					Job job = new Job(configuration, UtilPackageScan.simpleClassNameProcess(jobPlanClassFormalName));
					
					LOG.info("setJarByClass:"+SingleMapReduceJobDelegate.class);
					job.setJarByClass(SingleMapReduceJobDelegate.class);
					
					reduceTaskNum = Integer.parseInt(jobPlanReducerTaskNumStr);
					LOG.info("reduceTaskNum:"+reduceTaskNum);
					job.setNumReduceTasks(reduceTaskNum);
					
					LOG.info("JobInputPath:"+jobInputPath);
					List<String> inputPathes = new ArrayList<String>();
					initialDataInputPathes(new Path(jobInputPath), fileSystem, inputPathes);
					String[] inputPathesArray = inputPathes.toArray(new String[inputPathes.size()]);
					
					LOG.info("InputFormatClass:"+inputFormatClass);
					LOG.info("MapperClass:"+mapperClass);
					for (int i = 0; i < inputPathesArray.length; i++) {
					    MultipleInputs.addInputPath(job, new Path(inputPathesArray[i]), inputFormatClass, mapperClass);
					}
					
					LOG.info("MapOutputKeyClass:"+mapOutputKeyClass);
					job.setMapOutputKeyClass(mapOutputKeyClass);
					LOG.info("MapOutputValueClass:"+mapOutputValueClass);
					job.setMapOutputValueClass(mapOutputValueClass);
					
					//Reducing Phrase Customization
					LOG.info("ReducerClass:"+reducerClass);
					job.setReducerClass(reducerClass);
					LOG.info("OutputFormatClass:"+outputFormatClass);
					job.setOutputFormatClass(outputFormatClass);
					LOG.info("jobOutputPath:"+jobOutputPath);
					FileOutputFormat.setOutputPath(job, new Path(jobOutputPath));
					LOG.info("Job begin["+jobPlan.toString()+"]......");
					return job.waitForCompletion(true) ? 0 : 1;
				}
			} else {
				throw new Exception("We need and only need two args [0]:jobclassName, [1]:reduceTaskNum");
			}
		} catch (ClassNotFoundException e) {
			LOG.info("MapReduceJobDelegate exception, have you set class ["+args[0]+"] " +
					  "under rootpackage ["+JOB_PLAN_SCAN_PACKAGE_SCOPE+"]?" +
					  "OR have you enter the class name["+args[0]+"] without '.class' suffix? " +
					  "please remove the '.class' suffix.", e);
		} catch (Throwable e) {
			LOG.info("MapReduceJobDelegate exception", e);
		}
		return 0;
	}
	
	private void initialDataInputPathes(Path rootFolder, FileSystem fileSystem, List<String> inputPathes) throws IOException {
		FileStatus[] status = fileSystem.listStatus(rootFolder);
		if (status == null || status.length == 0) {
			return;
		}
	    for (int i = 0; i < status.length; i++) {
	        if (!status[i].isDir()) {
	            if (!status[i].getPath().getName().equals("_SUCCESS")) {
	                if (inputFilePathFilter!=null) {
	                    if (!inputFilePathFilter.accept(status[i].getPath())) {
	                        continue;
	                    }
	                }
	                inputPathes.add(status[i].getPath().toString());
	            }
	        } else {
	            Path childFolder = status[i].getPath();
                if (!childFolder.getName().equals("_logs") && 
                    !childFolder.getName().equals("_log")) {
                    initialDataInputPathes(childFolder, fileSystem, inputPathes);
                }
	        }
        }
	}
	
	private static String[] getJobPlanFormalName() {
		return UtilMapReduceInteractive.getHadoopMapreduceArgsInteractively(JOB_PLAN_SCAN_PACKAGE_SCOPE, SingleMapReduceJobDelegate.SingleMapReduceJobPlan.class);
	}
	
	public void setJobInputPath(String jobInputPath) {
		this.jobInputPath = jobInputPath;
	}
	public void setJobOutputPath(String jobOutputPath) {
		this.jobOutputPath = jobOutputPath;
	}
	public void setMapperClass(Class<? extends Mapper> mapperClass) {
		this.mapperClass = mapperClass;
	}
	public void setReducerClass(Class<? extends Reducer> reducerClass) {
		this.reducerClass = reducerClass;
	}
	public void setInputFormatClass(Class<? extends InputFormat> inputFormatClass) {
		this.inputFormatClass = inputFormatClass;
	}
	public void setOutputFormatClass(Class<? extends OutputFormat> outputFormatClass) {
		this.outputFormatClass = outputFormatClass;
	}
	public void setMapOutputKeyClass(Class<?> mapOutputKeyClass) {
		this.mapOutputKeyClass = mapOutputKeyClass;
	}
	public void setMapOutputValueClass(Class<?> mapOutputValueClass) {
		this.mapOutputValueClass = mapOutputValueClass;
	}
	public void setInputFilePathFilter(PathFilter inputFilePathFilter) {
	    this.inputFilePathFilter = inputFilePathFilter;
	}
	
	public static void main(String[] args) throws Exception {
		args = getJobPlanFormalName();
		if (args != null) {
			ToolRunner.run(new SingleMapReduceJobDelegate(), args);
		}
	}
}
