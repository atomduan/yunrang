package com.yunrang.location.service.report;

import com.yunrang.location.common.context.ContextFileProcess;
import com.yunrang.location.common.util.UtilPattern;

public class ReportDelunSession {
	private String filePath = "/home/juntaoduan/workspace/test/debug/delun_session.csv";
	public void doReport() {
		new ContextFileProcess().doSingleThreadProcess(filePath, new ContextFileProcess.LineProcessor() {
			@Override
			public void processLine(String line) throws Exception {
				String sessionId = UtilPattern.extractFirst(line, UtilPattern.REGEX_SESSION_ID);
				String appId = UtilPattern.extractFirst(line, UtilPattern.REGEX_APP_ID);
				String ipStr = UtilPattern.extractFirst(line, "\\[GATEWAY_IP\\]:\\["+UtilPattern.REGEX_IP+"\\]");
				ipStr = UtilPattern.extractFirst(ipStr, UtilPattern.REGEX_IP);
				System.out.println(sessionId+","+appId+","+ipStr);
			}
		});
	}
	
	public static void __main(String[] args) {
		ReportDelunSession ReportDelunSession = new ReportDelunSession();
		ReportDelunSession.doReport();
	}
}
