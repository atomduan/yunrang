package com.yunrang.location.integration.support.file.history.data_201308;

import com.yunrang.location.common.context.ContextFileProcess;
import com.yunrang.location.integration.support.file.HistoryFilesConfig;
import com.yunrang.location.integration.support.file.history.common.TopwangCellStationUpdater;


public class PreprocessTopwangUpdate201306 implements HistoryFilesConfig.Processor {
	private TopwangCellStationUpdater topwangCellStationUpdater;
	
	public void doProcess() throws Exception {
		ContextFileProcess processor = new ContextFileProcess();
		processor.doSingleThreadProcess(HistoryFilesConfig.yrDataPath_201308+"/topwang_update_201306.csv", "GBK", new ContextFileProcess.LineProcessor() {
			@Override
			public void processLine(String line) throws Exception {
				topwangCellStationUpdater.doUpdate(line);
			}
		});
	}
	
	public void setTopwangCellStationUpdater(TopwangCellStationUpdater topwangCellStationUpdater) {
		this.topwangCellStationUpdater = topwangCellStationUpdater;
	}
}
