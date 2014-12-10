package com.yunrang.location.tasklet.mapreduce;

import org.apache.hadoop.fs.PathFilter;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.OutputFormat;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;


@SuppressWarnings("unchecked")
abstract public class SingleMapReduceJobPlanBase implements SingleMapReduceJobDelegate.SingleMapReduceJobPlan {
	public static final String MORPHEUS_ROOT = "/tmp/morpheus";
	
	protected Class<? extends InputFormat> inputFormatClass = TextInputFormat.class;
	protected Class<? extends OutputFormat> outputFormatClass = TextOutputFormat.class;;
	protected Class<?> mapOutputKeyClass = Text.class;
	protected Class<?> mapOutputValueClass = Text.class;
	
	protected Class<? extends Mapper> mapperClass;
	protected Class<? extends Reducer> reducerClass;
	protected PathFilter inputFilePathFilter;
	protected String jobInputPath;
	protected String jobOutputRootPath;
	 
	
	public void customizeJob(SingleMapReduceJobDelegate delegate) throws Exception {
		delegate.setInputFormatClass(inputFormatClass);
		delegate.setOutputFormatClass(outputFormatClass);
		delegate.setMapOutputKeyClass(mapOutputKeyClass);
		delegate.setMapOutputValueClass(mapOutputValueClass);
		delegate.setMapperClass(mapperClass);
        delegate.setReducerClass(reducerClass);
        delegate.setJobOutputPath(jobOutputRootPath+"/"+this.getClass().getSimpleName());
		delegate.setInputFilePathFilter(inputFilePathFilter);
		delegate.setJobInputPath(jobInputPath);
    }
}
