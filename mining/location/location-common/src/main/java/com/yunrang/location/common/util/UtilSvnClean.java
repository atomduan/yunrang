package com.yunrang.location.common.util;

import java.io.File;

public class UtilSvnClean {
    
    public static void clean(String rootdir) {
        doClean(new File(rootdir));
    }
    
    private static void doClean(File dir) {
        if (dir.getName().contains(".svn")) {
            System.out.println("delete "+dir.getPath());
            deleteDir(dir);
        } else {
            File[] fs = dir.listFiles();
            if (fs != null) {
                for (File f : fs) doClean(f);
            }
        }
    }
    
    private static void deleteDir(File dir) {
        if (dir.isFile()) {
            dir.delete();
        } else {
            File[] fs = dir.listFiles();
            if (fs == null || (fs !=null && fs.length==0)) {
                dir.delete();
            } else {
                for (File f : fs) deleteDir(f);
            }
            dir.delete();
        }
    }
}
