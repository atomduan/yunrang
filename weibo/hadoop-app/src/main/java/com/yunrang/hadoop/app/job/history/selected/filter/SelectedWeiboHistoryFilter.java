package com.yunrang.hadoop.app.job.history.selected.filter;

import org.codehaus.jettison.json.JSONObject;

import com.yunrang.hadoop.app.common.config.HBaseWeiboColumns;
import com.yunrang.hadoop.app.common.filter.RecordFilter;
import com.yunrang.hadoop.app.utils.WeiboUtil;

public class SelectedWeiboHistoryFilter implements RecordFilter {
	public boolean accept(JSONObject record) {
		Integer QI = WeiboUtil.getIntField(record, HBaseWeiboColumns.QI);
		if (WeiboUtil.isCheating(QI)) return false;
		if (WeiboUtil.isBlackListUser(QI)) return false;
		
		Integer filter = WeiboUtil.getIntField(record, HBaseWeiboColumns.FILTER);
		Integer fwnum = WeiboUtil.getIntField(record, HBaseWeiboColumns.FWNUM);
		Integer valiFwnum = WeiboUtil.getIntField(record, HBaseWeiboColumns.VALIDFWNM);
		Integer valiLikenum = WeiboUtil.getIntField(record, HBaseWeiboColumns.LIKENUM);
		String content = WeiboUtil.getStringField(record, HBaseWeiboColumns.CONTENT);
		
		String time = WeiboUtil.getStringField(record, HBaseWeiboColumns.TIME);
		valiFwnum = (fwnum * valiFwnum) / 1000;
		
		if (valiFwnum < 10) {
			if (WeiboUtil.isBlackListSource(QI)) {
				return false;
			}
			if (WeiboUtil.isIllegalEvent(QI)) {
				return false;
			}
		}
		
		if (time.contains("2014")) {
			if (valiFwnum > 0 || valiLikenum > 0) {
				return true;
			}
		} else {
			if (valiFwnum > 5 || valiLikenum > 5) {
				return true;
			}
		}
		
		if (WeiboUtil.isForwardedWeibo(filter) == false) {
			if (WeiboUtil.isLowQuality(QI) == false) {
				if (WeiboUtil.chineseCharCount(content) > 50) {
					return true;
				}
				if (WeiboUtil.isImageContained(filter) || WeiboUtil.isMusicContained(filter) || WeiboUtil.isVedioContained(filter)) {
					if (WeiboUtil.chineseCharCount(content) > 30) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
