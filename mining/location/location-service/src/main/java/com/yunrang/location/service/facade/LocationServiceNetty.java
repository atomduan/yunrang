package com.yunrang.location.service.facade;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.StringUtils;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.HttpServerCodec;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.springframework.context.ApplicationContext;

import com.yunrang.location.service.facade.handler.IpLookUpHandler;
import com.yunrang.location.service.util.UtilLocationServiceContextSingaletonFactory;

public class LocationServiceNetty {
	private ApplicationContext locationApplicationContext;
	private int port=8081;
	private ServerBootstrap serverBootstrap;
	private Map<String, RequestHandler> handlerMap;
    
	public LocationServiceNetty(int port) {
		this.port = port;
	}
	
	public void initialize() throws Exception {
    	this.locationApplicationContext = UtilLocationServiceContextSingaletonFactory.getInstance();
		this.handlerMap = new ConcurrentHashMap<String, RequestHandler>();
		this.handlerMap.put("ipLookUp", new IpLookUpHandler(locationApplicationContext));
	}
	
	public void start() {
    	this.serverBootstrap = new ServerBootstrap(
    		new NioServerSocketChannelFactory(
    			Executors.newCachedThreadPool(),Executors.newCachedThreadPool()));
        this.serverBootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            public ChannelPipeline getPipeline() {
            	ChannelPipeline pipeline = Channels.pipeline();
                pipeline.addLast("1", new HttpServerCodec());
                pipeline.addLast("2", new SimpleChannelHandler() {
            		public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
            			HttpRequest request = (HttpRequest) e.getMessage();
            			try {
            				RequestHandler requestHandler = getRequestHandler(request.getUri());
                			if (requestHandler != null) {
                				processHttpResponse(e.getChannel(), requestHandler.handle(request),HttpResponseStatus.OK);
                			} else {
                				processHttpResponse(e.getChannel(), "STATUS 404 : NOT FOUND",HttpResponseStatus.NOT_FOUND);
                			}
            			} catch (Exception ex) {
            				processHttpResponse(e.getChannel(), "STATUS 400 : BAD_REQUEST",HttpResponseStatus.BAD_REQUEST);
            			}
                    }
            	});
                return pipeline;
            }
        });
        this.serverBootstrap.bind(new InetSocketAddress(this.port));
    }
	
	private void processHttpResponse(Channel channel, String result, HttpResponseStatus status) {
		byte[] responseContentBytes = result.getBytes();
		HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, status);
		ChannelBuffer channelBuffer = ChannelBuffers.buffer(responseContentBytes.length);
		channelBuffer.writeBytes(responseContentBytes);
		response.setContent(channelBuffer);
		channel.write(response).addListener(ChannelFutureListener.CLOSE);
	}
	
	private RequestHandler getRequestHandler(String uri) {
		if (uri.indexOf("?") > 0) {
			uri = uri.substring(0, uri.indexOf("?"));
		}
		uri = StringUtils.trimToNull(uri.replace("/", ""));
		return handlerMap.get(uri);
	}
	
    public void stop() {
    	this.serverBootstrap.releaseExternalResources();
    }
    
    public interface RequestHandler {
       	public String handle(HttpRequest request);
    }
    
    public static void main(String[] args) throws Exception {
    	int port = Integer.parseInt(args[0].trim());
    	LocationServiceNetty locationServiceNetty = new LocationServiceNetty(port);
    	locationServiceNetty.initialize();
    	locationServiceNetty.start();
    }
}