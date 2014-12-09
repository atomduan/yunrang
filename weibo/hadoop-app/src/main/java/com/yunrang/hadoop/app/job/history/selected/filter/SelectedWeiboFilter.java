package com.yunrang.hadoop.app.job.history.selected.filter;

import org.codehaus.jettison.json.JSONObject;

import com.yunrang.hadoop.app.common.config.HBaseWeiboColumns;
import com.yunrang.hadoop.app.common.filter.RecordFilter;
import com.yunrang.hadoop.app.utils.WeiboUtil;

public class SelectedWeiboFilter implements RecordFilter {
	public boolean accept(JSONObject record) {
		Integer QI = WeiboUtil.getIntField(record, HBaseWeiboColumns.QI);
		if (WeiboUtil.isCheating(QI)) return false;
		if (WeiboUtil.isBlackListUser(QI)) return false;
		
		Integer filter = WeiboUtil.getIntField(record, HBaseWeiboColumns.FILTER);
		Integer fwnum = WeiboUtil.getIntField(record, HBaseWeiboColumns.FWNUM);
		Integer valiFwnum = WeiboUtil.getIntField(record, HBaseWeiboColumns.VALIDFWNM);
		Integer valiLikenum = WeiboUtil.getIntField(record, HBaseWeiboColumns.LIKENUM);
		String content = WeiboUtil.getStringField(record, HBaseWeiboColumns.CONTENT);
		
		valiFwnum = (fwnum * valiFwnum) / 1000;
		if (valiFwnum > 0 || valiLikenum > 0) {
			return true;
		}
		if (WeiboUtil.isForwardedWeibo(filter) == false) {
			if (WeiboUtil.isLowQuality(QI) == false) {
				if (WeiboUtil.isImageContained(filter) || WeiboUtil.isMusicContained(filter) || WeiboUtil.isVedioContained(filter)) {
					return true;
				}
				if (WeiboUtil.chineseCharCount(content) > 50) {
					return true;
				}
			}
		}
		return false;
	}
}
