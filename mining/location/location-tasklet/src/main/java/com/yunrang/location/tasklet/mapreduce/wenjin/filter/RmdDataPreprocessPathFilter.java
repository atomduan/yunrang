package com.yunrang.location.tasklet.mapreduce.wenjin.filter;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;


public class RmdDataPreprocessPathFilter implements PathFilter {
    String indicator_may = "output/201305";
    String indicator_july = "output/201306";
    public boolean accept(Path InputFilePath) {
        String fileUrl = InputFilePath.toUri().getPath();
        if (fileUrl.contains(indicator_may) || fileUrl.contains(indicator_july)) {
            if (fileUrl.endsWith(".done")) {
                return true;
            }
        }
        return false;
    }
}
