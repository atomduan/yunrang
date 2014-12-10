package com.yunrang.location.integration.support.file.history.data_201308;

import com.yunrang.location.common.context.ContextFileProcess;
import com.yunrang.location.integration.support.file.HistoryFilesConfig;
import com.yunrang.location.integration.support.file.history.common.TopwangCellStationUpdater;


public class PreprocessTopwangUpdate201308 implements HistoryFilesConfig.Processor {
	private TopwangCellStationUpdater topwangCellStationUpdater;
	private ContextFileProcess contextFileProcess; 
	
	public void doProcess() throws Exception {
		contextFileProcess.doSingleThreadProcess(HistoryFilesConfig.yrDataPath_201308+"/topwang_update_201308.csv", "GBK", new ContextFileProcess.LineProcessor() {
			@Override
			public void processLine(String line) throws Exception {
				topwangCellStationUpdater.doUpdate(line);
			}
		});
	}

	public void setTopwangCellStationUpdater(TopwangCellStationUpdater topwangCellStationUpdater) {
		this.topwangCellStationUpdater = topwangCellStationUpdater;
	}
	public void setContextFileProcess(ContextFileProcess contextFileProcess) {
		this.contextFileProcess = contextFileProcess;
	}
}
