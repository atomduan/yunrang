package com.yunrang.hadoop.app.leetcode;

public class Palindrome {
	public int minCut(String s) {
		char[] sc = s.toCharArray();
		int a = greedyCheck(sc, 0, sc.length - 1) - 1;
		return a;
	}

	public int greedyCheck(char[] sc, int b, int e) {
		if (b == e)
			return 1;
		if (b > e)
			return 0;
		for (int w = e - b + 1; w >= 1; w--) {
			int[] idx = findeLongist(sc, b, e, w);
			if (idx != null) {
				int left = greedyCheck(sc, b, idx[0] - 1);
				int right = greedyCheck(sc, idx[1] + 1, e);
				return left + right + 1;
			}
		}
		return 0;
	}

	public int[] findeLongist(char[] sc, int b, int e, int w) {
		for (int i = b; i <= e - w + 1; i++) {
			if (isPalindrome(sc, i, i + w - 1)) {
				return new int[] { i, i + w - 1 };
			}
		}
		return null;
	}

	public boolean isPalindrome(char[] sc, int b, int e) {
		for (int i = b; i <= e; i++) {
			if (i >= e - i + b)
				return true;
			if (sc[i] != sc[e - i + b])
				return false;
		}
		return true;
	}

	public static void main(String[] args) {
		Palindrome p = new Palindrome();
		String s = "adabdcaebdcebdcacaaaadbbcadabcbeabaadcbcaaddebdbddcbdacdbbaedbdaaecabdceddccbdeeddccdaabbabbdedaaabcdadbdabeacbeadbaddcbaacdbabcccbaceedbcccedbeecbccaecadccbdbdccbcbaacccbddcccbaedbacdbcaccdcaadcbaebebcceabbdcdeaabdbabadeaaaaedbdbcebcbddebccacacddebecabccbbdcbecbaeedcdacdcbdbebbacddddaabaedabbaaabaddcdaadcccdeebcabacdadbaacdccbeceddeebbbdbaaaaabaeecccaebdeabddacbedededebdebabdbcbdcbadbeeceecdcdbbdcbdbeeebcdcabdeeacabdeaedebbcaacdadaecbccbededceceabdcabdeabbcdecdedadcaebaababeedcaacdbdacbccdbcece";
		System.out.println(p.minCut(s));
	}
}
