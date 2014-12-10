package com.yunrang.location.integration;

import com.yunrang.location.common.context.ContextFileProcess;
import com.yunrang.location.integration.support.file.HistoryFilesConfig;

public class FileCharSetConverter implements HistoryFilesConfig.Processor {
	private ContextFileProcess contextFileProcess = new ContextFileProcess();
	private String filePath;
	public FileCharSetConverter(String filePath) {
		this.filePath = filePath;
	}
	@Override
	public void doProcess() throws Exception {
		contextFileProcess.doSingleThreadProcess(filePath, "GBK", new ContextFileProcess.LineProcessor() {
			@Override
			public void processLine(String line) throws Exception {
				System.out.println(line);
			}
		});
	}
	public static void main(String[] args) throws Exception {
		FileCharSetConverter converter = new FileCharSetConverter(args[0]);
		converter.doProcess();
	}
}
