package com.yunrang.location.common.context.thread;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A pool of threads.
 * Avoids the expense of thread creation by pooling threads after their run
 * methods exit for reuse.
 * If the maximum pool size is reached, jobs wait for a free thread. By default
 * there is no maximum pool size. Idle threads timeout and terminate until the
 * minimum number of threads are running.
 */
public class QueuedThreadPool extends AbstractLifeCycle implements Serializable, ThreadPool {
	static Logger logger = LoggerFactory.getLogger(QueuedThreadPool.class);
    private static final long serialVersionUID = 6454842040951848852L;
    private static int threadPoolId;

    private final Object threadsLock = new Object();
    private String threadPoolName;
    private Set<Thread> threads;
    private List<Thread> idleThreadList;
    
    private final Object jobsArraylock = new Object();
    private Runnable[] jobsArray;
    private int nextJob;
    private int nextJobSlot;
    private int queued;
    private int maxQueued;

    private boolean isDaemon;
    private int threadId;
    private long lastShrink;
    private int maxIdleTimeMs = 60000;
    private int maxThreads = 25;
    private int minThreads = 2;
    private boolean warned = false;
    private int lowThreads = 0;
    private int threadPriority = Thread.NORM_PRIORITY;
    private int spawnOrShrinkAt = 0;
    private int maxStopTimeMs;
    private final Object joinLock = new Object();
    private AtomicInteger businessJobs = new AtomicInteger(0);

    public QueuedThreadPool() {
        threadPoolName = "qtp" + threadPoolId++;
    }

    public boolean dispatch(Runnable jobToRun) {
    	if (jobToRun != null) {
    		if (isRunning()) {
        		return doDispatch(jobToRun);
        	} else {
        		if (!this.isStopping() && !this.isStopped()) {
        			try {
    					start();
    					return doDispatch(jobToRun);
    				} catch (Exception e) {
    					logger.error("thread pool init fail~!", e);
    				}
        		}
        	}
    	} 
        return false;
    }
    
    private boolean doDispatch(Runnable jobToRun) {
        PoolThread thread = null;
        boolean spawn = false;
        synchronized (jobsArraylock) {
            // Look for an idle thread
            int idle = idleThreadList.size();
            if (idle > 0)
                thread = (PoolThread) idleThreadList.remove(idle - 1);
            else {
                // queue the job
                queued++;
                if (queued > maxQueued)
                    maxQueued = queued;
                jobsArray[nextJobSlot++] = jobToRun;
                if (nextJobSlot == jobsArray.length)
                    nextJobSlot = 0;
                if (nextJobSlot == nextJob) {
                    // Grow the job queue
                    Runnable[] jobs = new Runnable[jobsArray.length + maxThreads];
                    int split = jobsArray.length - nextJob;
                    if (split > 0)
                        System.arraycopy(jobsArray, nextJob, jobs, 0, split);
                    if (nextJob != 0)
                        System.arraycopy(jobsArray, 0, jobs, split, nextJobSlot);

                    jobsArray = jobs;
                    nextJob = 0;
                    nextJobSlot = queued;
                }
                spawn = queued > spawnOrShrinkAt;
            }
        }
        if (thread != null) {
            thread.dispatch(jobToRun);
        } else if (spawn) {
            newThread();
        }
        return true;
    }

    public void join() throws InterruptedException {
        synchronized (joinLock) {
            while (isRunning())
                joinLock.wait();
        }
        // TODO remove this semi busy loop!
        while (isStopping())
            Thread.sleep(100);
    }
    
    protected void doStart() throws Exception {
        if (maxThreads < minThreads || minThreads <= 0)
            throw new IllegalArgumentException("!0<minThreads<maxThreads");
        threads = new HashSet<Thread>();
        idleThreadList = new ArrayList<Thread>();
        jobsArray = new Runnable[maxThreads];
        for (int i = 0; i < minThreads; i++) {
            newThread();
        }
    }

    protected void doStop() throws Exception {
        super.doStop();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            synchronized (threadsLock) {
                Iterator<Thread> iter = threads.iterator();
                while (iter.hasNext())
                    iter.next().interrupt();
            }
            Thread.yield();
            if (threads.size() == 0 || (maxStopTimeMs > 0 && maxStopTimeMs < (System.currentTimeMillis() - start)))
                break;
            try {
                Thread.sleep(i * 100);
            } catch (InterruptedException e) {
            }
        }
        // TODO perhaps force stops
        if (threads.size() > 0){
            //......
        }
        synchronized (joinLock) {
            joinLock.notifyAll();
        }
    }

    protected void newThread() {
        synchronized (threadsLock) {
            if (threads.size() < maxThreads) {
                PoolThread thread = new PoolThread();
                threads.add(thread);
                thread.setName(threadPoolName + "-" + threadId++);
                thread.start();
            } else if (!warned) {
                warned = true;
            }
        }
    }

    protected void stopJob(Thread thread, Object job) {
        thread.interrupt();
    }

    private class PoolThread extends Thread {
        Runnable jobToRun = null;

        private PoolThread() {
            setDaemon(isDaemon);
            setPriority(threadPriority);
        }

        public void run() {
            boolean idle = false;
            Runnable job = null;
            try {
                while (isRunning()) {
                    // Run any job that we have.
                    if (job != null) {
                        final Runnable todo = job;
                        job = null;
                        idle = false;
                        // log the count of business job versus monitor job
                        if (todo instanceof BusinessRunnable) {
                            try {
                                businessJobs.getAndIncrement();
                                todo.run();
                            } finally {
                                businessJobs.getAndDecrement();
                            }
                        } else {
                            todo.run();
                        }
                    }
                    synchronized (jobsArraylock) {
                        // is there a queued job?
                        if (queued > 0) {
                            queued--;
                            job = jobsArray[nextJob++];
                            if (nextJob == jobsArray.length)
                                nextJob = 0;
                            continue;
                        }
                        // Should we shrink?
                        final int threadSize = threads.size();
                        if (threadSize > minThreads &&
                                (threadSize > maxThreads ||
                                idleThreadList.size() > spawnOrShrinkAt)) {
                            long now = System.currentTimeMillis();
                            if ((now - lastShrink) > getMaxIdleTimeMs()) {
                                lastShrink = now;
                                idleThreadList.remove(this);
                                return;
                            }
                        }
                        if (!idle) {
                            // Add ourselves to the idle set.
                            idleThreadList.add(this);
                            idle = true;
                        }
                    }
                    // We are idle
                    // wait for a dispatched job
                    synchronized (this) {
                        if (jobToRun == null)
                            this.wait(getMaxIdleTimeMs());
                        job = jobToRun;
                        jobToRun = null;
                    }
                }
            } catch (InterruptedException e) {
            } finally {
                synchronized (jobsArraylock) {
                    idleThreadList.remove(this);
                }
                synchronized (threadsLock) {
                    threads.remove(this);
                }
                synchronized (this) {
                    job = jobToRun;
                }
                // we died with a job! reschedule it
                if (job != null) {
                    QueuedThreadPool.this.dispatch(job);
                }
            }
        }

        void dispatch(Runnable job) {
            synchronized (this) {
                jobToRun = job;
                this.notify();
            }
        }
    }
    
    // Setters and Getters...
    public int getBusinessJobs() {
        return businessJobs.get();
    }
    public int getIdleThreads() {
        return idleThreadList == null ? 0 : idleThreadList.size();
    }
    public int getLowThreads() {
        return lowThreads;
    }
    public int getMaxQueued() {
        return maxQueued;
    }
    public int getMaxIdleTimeMs() {
        return maxIdleTimeMs;
    }
    public int getMaxThreads() {
        return maxThreads;
    }
    public int getMinThreads() {
        return minThreads;
    }
    public String getName() {
        return threadPoolName;
    }
    public int getThreads() {
        return threads.size();
    }
    public int getThreadsPriority() {
        return threadPriority;
    }
    public int getQueueSize() {
        return queued;
    }
    public int getSpawnOrShrinkAt() {
        return spawnOrShrinkAt;
    }
    public void setSpawnOrShrinkAt(int spawnOrShrinkAt) {
        this.spawnOrShrinkAt = spawnOrShrinkAt;
    }
    public int getMaxStopTimeMs() {
        return maxStopTimeMs;
    }
    public void setMaxStopTimeMs(int stopTimeMs) {
        this.maxStopTimeMs = stopTimeMs;
    }
    public boolean isDaemon() {
        return isDaemon;
    }
    public boolean isLowOnThreads() {
        return queued > lowThreads;
    }
    public void setDaemon(boolean daemon) {
        this.isDaemon = daemon;
    }
    public void setLowThreads(int lowThreads) {
        this.lowThreads = lowThreads;
    }
    public void setMaxIdleTimeMs(int maxIdleTimeMs) {
        this.maxIdleTimeMs = maxIdleTimeMs;
    }
    public void setName(String name) {
        threadPoolName = name;
    }
    public void setThreadsPriority(int priority) {
        threadPriority = priority;
    }
    public void setMaxThreads(int maxThreads) {
        if (isStarted() && maxThreads < minThreads)
            throw new IllegalArgumentException("!minThreads<maxThreads");
        this.maxThreads = maxThreads;
    }
    public void setMinThreads(int minThreads) {
        if (isStarted() && (minThreads <= 0 || minThreads > maxThreads))
            throw new IllegalArgumentException("!0<=minThreads<maxThreads");
        this.minThreads = minThreads;
        synchronized (threadsLock) {
            while (isStarted() && threads.size() < this.minThreads) {
                newThread();
            }
        }
    }
}
