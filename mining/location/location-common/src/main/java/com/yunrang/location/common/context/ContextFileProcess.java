package com.yunrang.location.common.context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yunrang.location.common.context.thread.ThreadPool;


public class ContextFileProcess {
    static Logger logger = LoggerFactory.getLogger(ContextFileProcess.class);
    private LinkedBlockingQueue<String> blockingQueue;
    private ThreadPool threadPool;
    private Integer workerNum = 90;
    
    public ContextFileProcess() {
    	this.blockingQueue = new LinkedBlockingQueue<String>(1000000);
    }
    
    public void doSingleThreadProcess(final String filepath, final LineProcessor lineProcesser) {
    	this.doSingleThreadProcess(filepath, "UTF-8", lineProcesser);
    }
    
    public void doSingleThreadProcess(final String filepath, String charsetName, final LineProcessor lineProcesser) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filepath)), charsetName));
            Long lineCount = 0L;
            try {
                lineProcesser.startup();
                while (true) {
                    String onProcessedLine = null;
                    if (lineCount%10000L==0) logger.info(lineCount+" lines has been processed......");
                    lineCount++;
                    try {
                        onProcessedLine = reader.readLine();
                        if (onProcessedLine != null) {
                            lineProcesser.processLine(onProcessedLine);
                        } else {
                            break;
                        }
                    } catch (Exception e) {
                        logger.error("lineCount:["+lineCount+"]; onQueuedLine:["+onProcessedLine+"]", e);
                    }
                }
                lineProcesser.cleanup();
                logger.info("total process routine finished, lineCount:["+lineCount+"]; ");
            } finally {
                try {
                    if (reader!=null) {
                        reader.close();
                    }
                } catch (Exception e) {
                    logger.error("fail to close reader", e);
                }
            }
        } catch (Exception e) {
            logger.error("single thread process error....", e);
        }
    }
    
    private final AtomicBoolean readFileComplete = new AtomicBoolean();
    public void doMutiThreadProcess(final String filepath, final LineProcessor lineProcesser) {
    	this.doMutiThreadProcess(filepath, "UTF-8", lineProcesser);
    }
    
    public void doMutiThreadProcess(final String filepath, final String charsetName, final LineProcessor lineProcesser) {
    	threadPool.dispatch(new Runnable() {
            @Override
            public void run() {
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filepath)), charsetName));
                    Long lineCount = 0L;
                    String onQueuedLine = "";
                    try {
                        readFileComplete.set(false);
                        while (true) {
                            if (lineCount%10000L==0) logger.info(lineCount+" lines has been processed......");
                            lineCount++;
                            try {
                                onQueuedLine = reader.readLine();
                                if (onQueuedLine != null) {
                                    blockingQueue.put(onQueuedLine);
                                } else {
                                    break;
                                }
                            } catch (Exception e) {
                                logger.error("lineCount:["+lineCount+"]; onQueuedLine:["+onQueuedLine+"]", e);
                            }
                        }
                        readFileComplete.set(true);
                    } finally {
                        try {
                            if (reader!=null) {
                                reader.close();
                            }
                        } catch (Exception e) {
                            logger.error("fail to close reader", e);
                        }
                    }
                } catch (Exception e) {
                    logger.error("import data to queue error", e);
                }
            }
        });
        
        for (int i=0; i<workerNum; i++) {
        	threadPool.dispatch(new Runnable() {
                Integer emptyCount = 0;
                public void run() {
                    while (true) {
                        String onProcessedLine = null;
                        try {
                            onProcessedLine = blockingQueue.poll(100, TimeUnit.MILLISECONDS);
                        } catch (Exception e) {
                            // ignore......
                        }
                        // update emptyCount
                        if (onProcessedLine != null) {
                            emptyCount = 0;
                            try {
                                lineProcesser.processLine(onProcessedLine);
                            } catch (Exception e) {
                                logger.error("process error; line :["+onProcessedLine+"]", e);
                            }
                        } else {
                            emptyCount++;
                        }
                        // end up condition.
                        if (emptyCount > 10 && readFileComplete.get()==true) {
                            logger.info("There is no more record line need to be processed, worker exit......");
                            break;
                        }
                    }
                }
            });
        }
    }
    
    public void setThreadPool(ThreadPool threadPool) {
    	this.threadPool = threadPool;
    }
    
    public static abstract class LineProcessor {
        public void startup() throws Exception{}
        abstract public void processLine(String line) throws Exception;
        public void cleanup() throws Exception{}
    }
}
