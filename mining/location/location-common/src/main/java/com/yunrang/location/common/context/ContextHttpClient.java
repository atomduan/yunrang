package com.yunrang.location.common.context;

import java.io.BufferedInputStream;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;


public class ContextHttpClient {
    private final SoftReferenceCache<HttpClient> HTTP_CLIENT_SOFT_REFERENCE_CACHE = new SoftReferenceCache<HttpClient>();
    private final Integer REQUEST_TIME_OUT = 1000*5;
    
    public Integer getRequestTimeOutParam() {
        return REQUEST_TIME_OUT;
    }
    
    public PostMethod getPostMethod(String url) {
        PostMethod method = new PostMethod(url);
        preprocessHttpMethod(method);
        return method;
    }
    
    public GetMethod getGetMethod(String url) {
        GetMethod method = new GetMethod(url);
        preprocessHttpMethod(method);
        return method;
    }

    public String invokeHttpRequest(HttpMethod method) throws Exception  {
        return invokeHttpRequest(method, "UTF-8");
    }
    
    public String invokeHttpRequest(HttpMethod method, String charSet) throws Exception  {
        HttpClient httpClient = null;
        SoftReference<HttpClient> softReference = null;
        softReference = HTTP_CLIENT_SOFT_REFERENCE_CACHE.getSoftReference();
        if (softReference != null) {
            httpClient = softReference.get();
        }
        Boolean needCacheNewInstance = false;
        try {
            if (httpClient == null) { 
                httpClient = new HttpClient();
                needCacheNewInstance = true;    
            }
            return doRequest(httpClient, method, charSet);
        } finally {
            if (needCacheNewInstance) {
                HTTP_CLIENT_SOFT_REFERENCE_CACHE.cacheNew(httpClient);
            } else {
                HTTP_CLIENT_SOFT_REFERENCE_CACHE.cacheOld(softReference);
            }
        }
    }
    
    private void preprocessHttpMethod(HttpMethod method) {
        method.addRequestHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:2.0.1) Gecko/20100101 Firefox/4.0.1");
        method.addRequestHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        method.addRequestHeader("Accept-Language", "zh-cn,en-us;q=0.7,en;q=0.3");
        method.addRequestHeader("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
        method.getParams().setParameter("http.protocol.cookie-policy", CookiePolicy.BROWSER_COMPATIBILITY);
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
        method.getParams().setSoTimeout(REQUEST_TIME_OUT);
        method.addRequestHeader("Host", "www.weibo.com");
        method.addRequestHeader("Referer", "http://www.weibo.com/");
    }
    
    private String doRequest(HttpClient httpClient, HttpMethod method, String charSet) throws Exception {
        byte[] resultBytes = null;
        byte[] primbuf = new byte[1024];
        List<byte[]> bufferList = new ArrayList<byte[]>();
        Integer totalSize = 0;
        BufferedInputStream bis = null;
        try {
            int statusCode = httpClient.executeMethod(method);
            if (statusCode == HttpStatus.SC_OK) {
                bis = new BufferedInputStream(method.getResponseBodyAsStream());
                while (true) {
                    int p = bis.read(primbuf);
                    if (p > 0) {
                        byte[] blet = new byte[p];
                        for (int i=0 ; i<p; i++) {
                            blet[i] = primbuf[i];
                        }
                        bufferList.add(blet);
                        totalSize += p;
                    } else {
                        break;
                    }
                }
                if (totalSize > 0) {
                    resultBytes = new byte[totalSize];
                    Integer curp = 0;
                    for (byte[] br : bufferList) {
                        for (byte b : br) {
                            resultBytes[curp] = b;
                            curp++;
                        }
                    }
                }
            }
            return new String(resultBytes, charSet);
        } finally {
            try {if (bis !=null) {bis.close();}} catch (Exception e) {}
            method.releaseConnection();
        }
    }
    
    private class SoftReferenceCache<T> {
        private Queue<SoftReference<T>> softRefQueue;
        private ReferenceQueue<T> referenceQueue = new ReferenceQueue<T>();
        public SoftReferenceCache() {
            softRefQueue = new ConcurrentLinkedQueue<SoftReference<T>>();
            referenceQueue = new ReferenceQueue<T>();
        }
        public void cacheNew(T obj) {
            SoftReference<T> ref = new SoftReference<T>(obj, referenceQueue);
            softRefQueue.add(ref);
        }
        public void cacheOld(SoftReference<T> softReference) {
            softRefQueue.add(softReference);
        }
        public SoftReference<T> getSoftReference() {
            return  softRefQueue.poll();
        }
    }
}