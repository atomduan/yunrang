package com.yunrang.location.datamgr.mysql.util;

import java.sql.Connection;

import org.apache.tomcat.jdbc.pool.PoolProperties;

final public class UtilPooledDataSource {
    private final static Object lock = new Object();
    private static UtilPooledDataSource tomCatConnPool;
    private org.apache.tomcat.jdbc.pool.DataSource tomcatConnectionPoolDataSource;
    
    public UtilPooledDataSource(String ipAddress, String port, String dbName, String username, String password) {
        PoolProperties p = new PoolProperties();
        p.setUrl("jdbc:mysql://"+ipAddress+":"+port+"/"+dbName+"?useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull");
        p.setDriverClassName("com.mysql.jdbc.Driver");
        p.setUsername(username);
        p.setPassword(password);
        p.setJmxEnabled(true);
        p.setTestWhileIdle(false);
        p.setTestOnBorrow(true);
        p.setValidationQuery("SELECT 1");
        p.setTestOnReturn(false);
        p.setValidationInterval(30000);
        p.setTimeBetweenEvictionRunsMillis(30000);
        p.setMaxActive(100);
        p.setInitialSize(10);
        p.setMaxWait(10000);
        p.setRemoveAbandonedTimeout(60);
        p.setMinEvictableIdleTimeMillis(30000);
        p.setMinIdle(10);
        p.setLogAbandoned(true);
        p.setRemoveAbandoned(true);
        p.setFairQueue(true);
        p.setJdbcInterceptors(
                "org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"+
                "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
        tomcatConnectionPoolDataSource = new org.apache.tomcat.jdbc.pool.DataSource();
        tomcatConnectionPoolDataSource.setPoolProperties(p);
    }
    
    private void close() {
    	this.tomcatConnectionPoolDataSource.close(true);
    }
    
    private Connection doGetConnection() throws Exception {
        return tomcatConnectionPoolDataSource.getConnection();
    }
    
    public static void refresh(String ipAddress, String port, String dbName, String username, String password) {
        synchronized (lock) {
            if (tomCatConnPool != null) {
            	tomCatConnPool.close();
            }
            UtilPooledDataSource templeReference = new UtilPooledDataSource(ipAddress, port, dbName, username, password);
            tomCatConnPool = templeReference;
        }
    }
    
    public static Connection getConnection() throws Exception {
        return tomCatConnPool.doGetConnection();
    }
}
