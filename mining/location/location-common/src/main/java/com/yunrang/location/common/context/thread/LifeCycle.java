package com.yunrang.location.common.context.thread;

import java.util.EventListener;

/**
 * The lifecycle interface for generic components
 * Classes implementing this interface have a defined life cycle defined by the
 * methods of this interface.
 */
public interface LifeCycle {
    
    public void start() throws Exception;

    /**
     * Stops the component. The component may wait for current activities to
     * complete normally, but it can be interrupted.
     */
    public void stop()throws Exception;

    /**
     * @return true if the component is starting or has been started.
     */
    public boolean isRunning();

    /**
     * @return true if the component has been started.
     */
    public boolean isStarted();

    /**
     * @return true if the component is starting.
     */
    public boolean isStarting();

    /**
     * @return true if the component is stopping.
     */
    public boolean isStopping();

    /**
     * @return true if the component has been stopped.
     */
    public boolean isStopped();

    /**
     * @return true if the component has failed to start or has failed to stop.
     */
    public boolean isFailed();

    public void addLifeCycleListener(LifeCycle.Listener listener);

    public void removeLifeCycleListener(LifeCycle.Listener listener);

    /**
     * Listener. A listener for Lifecycle events.
     */
    public interface Listener extends EventListener {
        public void lifeCycleStarting(LifeCycle event);
        public void lifeCycleStarted(LifeCycle event);
        public void lifeCycleFailure(LifeCycle event, Throwable cause);
        public void lifeCycleStopping(LifeCycle event);
        public void lifeCycleStopped(LifeCycle event);
    }
}
