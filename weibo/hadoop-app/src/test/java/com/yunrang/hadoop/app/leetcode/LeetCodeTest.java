package com.yunrang.hadoop.app.leetcode;

import java.util.ArrayList;
import java.util.List;

public class LeetCodeTest {
	public void solve(char[][] board) {
		if (board == null) return;
		while (true) {
			int[] sr = scanBoundary(board);
			if (sr != null) {
				int r = sr[0];
				int c = sr[1];
				board[r][c] = 'b';
				color(board, r, c, 'O', 'b');
			} else {
				break;
			}
		}
		
		while (true) {
			int[] sr = scan(board);
			if (sr != null) {
				int r = sr[0];
				int c = sr[1];
				colorInner(board, r, c);
			} else {
				break;
			}
		}

		for (int r = 0; r < board.length; r++) {
			char[] row = board[r];
			for (int c = 0; c < row.length; c++) {
				if (row[c] == 'b') {
					row[c] = 'O';
				}
			}
		}
	}

	private int[] scanBoundary(char[][] board) {
		int rowLength = board[0].length;
		for (int j=0; j<rowLength; j++) {
			char c = board[0][j];
			if (c == 'O') {
				return new int[]{0, j};
			}
		}
		
		for (int j=0; j<rowLength; j++) {
			char c = board[board.length-1][j];
			if (c == 'O') {
				return new int[]{board.length-1, j};
			}
		}
		
		for (int r=0 ; r<board.length; r++) {
			char c = board[r][0];
			if (c == 'O') {
				return new int[]{r, 0};
			}
		}
		
		for (int r=0 ; r<board.length; r++) {
			char c = board[r][rowLength-1];
			if (c == 'O') {
				return new int[]{r, rowLength-1};
			}
		}
		return null;
	}
	
	private int[] scan(char[][] board) {
		int[] result = new int[] { -1, -1 };
		for (int r = 0; r < board.length; r++) {
			char[] row = board[r];
			for (int c = 0; c < row.length; c++) {
				if (row[c] == 'O') {
					result[0] = r;
					result[1] = c;
					return result;
				}
			}
		}
		if (result[0] + result[1] == -2) {
			return null;
		} else {
			return result;
		}
	}

	private void colorInner(char[][] board, int i, int j) {
		board[i][j] = 'X';
		List<int[]> neibours = findNeibours(board, i, j, 'O');
		for (int[] n : neibours) {
			int r = n[0];
			int c = n[1];
			board[r][c] = 'X';
		}
		for (int[] n : neibours) {
			int r = n[0];
			int c = n[1];
			colorInner(board, r, c);
		}
	}

	private void color(char[][] board, int i, int j, char s, char d) {
		List<int[]> neibours = findNeibours(board, i, j, s);
		for (int[] n : neibours) {
			int r = n[0];
			int c = n[1];
			board[r][c] = d;
		}
		for (int[] n : neibours) {
			int r = n[0];
			int c = n[1];
			color(board, r, c, s, d);
		}
	}

	private List<int[]> findNeibours(char[][] board, int i, int j, char d) {
		List<int[]> result = new ArrayList<int[]>();
		if (i-1 > 0) {
			if (board[i-1][j] == d) {
				result.add(new int[]{i-1, j});
			}
		}
		if (i+1 < board.length) {
			if (board[i+1][j] == d) {
				result.add(new int[]{i+1, j});
			}
		}
		if (j-1 > 0) {
			if (board[i][j-1] == d) {
				result.add(new int[]{i, j-1});
			}
		}
		if (j+1 < board[0].length) {
			if (board[i][j+1] == d) {
				result.add(new int[]{i, j+1});
			}
		}
		return result;
	}
	
	public static void main(String[] args) {
		LeetCodeTest l = new LeetCodeTest();
		char[][] board = new char[][]{
				new char[]{'X', 'X', 'X', 'X'}, 
				new char[]{'X', 'O', 'X', 'X'}, 
				new char[]{'X', 'X', 'O', 'X'},
				new char[]{'O', 'O', 'O', 'O'}
		};
		l.solve(board);
		for (char[] row : board) {
			for (char c : row) {
				System.out.print(c+" ");
			}
			System.out.print("\n");
		}
		"".toCharArray();
	}
}
