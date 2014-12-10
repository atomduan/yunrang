package com.yunrang.location.tasklet.mapreduce.wenjin.filter;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;

public class RmdDataPreprocessMonthFilter implements PathFilter {
	private String indicator_aug = "201308";
	private String indicator_jly = "201307";
	private String indicator_jun = "201306";
	private String indicator_may = "201305";
	@Override
	public boolean accept(Path InputFilePath) {
		String fileUrl = InputFilePath.toUri().getPath();
        if (fileUrl.contains(indicator_aug) 
        		|| fileUrl.contains(indicator_jly)
        			|| fileUrl.contains(indicator_jun)
        				|| fileUrl.contains(indicator_may)) {
        	return true;
        }
        return false;
	}
}
