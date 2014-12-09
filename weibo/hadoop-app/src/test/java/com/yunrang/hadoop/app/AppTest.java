package com.yunrang.hadoop.app;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit test for simple App.
 */
public class AppTest {
	public int evalRPN(String[] tokens) {
        List<String> tl = new ArrayList<String>();
        for (int i=0; i<tokens.length; i++) {
            tl.add(tokens[i]);
        }
        while (true) {
            if (tl.size()==1) return Integer.parseInt(tl.get(0));
            tl = compute(tl);
        }
    }
    
    public List<String> compute(List<String> il) {
        List<String> r = new ArrayList<String>();
        boolean cp = false;
        for (int i=0; i<il.size()-3; i++) {
            if (cp == false) {
                int a = Integer.parseInt(il.get(i));
                int b = Integer.parseInt(il.get(i+1));
                if (isIntString(il.get(i+2))) {
                    r.add(il.get(i));
                    continue;
                } else {
                    r.add(compute(a, b, il.get(i+2)));
                    cp = true;
                    i = i+2;
                    continue;
                }
            } else {
                r.add(il.get(i));
            }
        }
        return r;
    }
    
    private String compute(int a, int b, String op) {
        if (op.equals("+")) return (a+b)+"";
        if (op.equals("-")) return (a-b)+"";
        if (op.equals("*")) return (a*b)+"";
        if (op.equals("/")) return (a/b)+"";
        throw new RuntimeException();
    }
    
    private boolean isIntString(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch(Exception e) {
            return false;
        }
    }
    
    public static void main(String[] args) {
    	AppTest a = new AppTest();
    	System.out.println(a.evalRPN(new String[]{"0","3","/"}));
    }
}
