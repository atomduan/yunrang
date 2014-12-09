package com.yunrang.hadoop.app.leetcode;

import java.util.ArrayList;
import java.util.List;

public class RestoreIPAddresses {
	public List<String> restoreIpAddresses(String s) {
		if (s.length() > 12) return r;
        this.doRestoreIpAddresses("", s, 3);
        return r;
    }
	
	List<String> r = new ArrayList<String>();
	private void doRestoreIpAddresses(String pre, String sub, int cut) {
		if (sub.length()==0) return;
		if (cut == 0) {
			long sv = Long.parseLong(sub);
			if (sv <= 255) {
				if (sv > 0 && !sub.startsWith("0")) {
					r.add(pre+"."+sub);
				} else if (sv == 0 && sub.length()==1) {
					r.add(pre+"."+sub);
				}
			}
			return;
		}
		int l = Math.min(sub.length(), 3);
		for (int i=0; i<l; i++) {
			String p = sub.substring(0, i+1);
			long pv = Long.parseLong(p);
			if (pv <= 255) {
				String pn = pre.length()==0 ? p+"" : pre+"."+p;
				this.doRestoreIpAddresses(pn, sub.substring(i+1, sub.length()), cut-1);
			}
			if (sub.startsWith("0")) {
				break;
			}
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		RestoreIPAddresses ri = new RestoreIPAddresses();
		List<String> l = ri.restoreIpAddresses("1111");
		for (String e : l) {
			System.out.println(e);
		}
	}
}
