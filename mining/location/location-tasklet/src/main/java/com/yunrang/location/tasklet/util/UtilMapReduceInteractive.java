package com.yunrang.location.tasklet.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import com.yunrang.location.common.util.UtilPackageScan;


public class UtilMapReduceInteractive {
	
	public static String[] getHadoopMapreduceArgsInteractively(String rootPack, Class<?> filter) {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			String jobPlanFormalName = getTargetFormalClassNameInteractively(rootPack, filter, br);
			String reducerTaskNum = getTargetReducerTaskNumInteractively(jobPlanFormalName, br);
			return new String[]{jobPlanFormalName, reducerTaskNum};
		} catch (Exception e) {
			return null;
		} finally {
			try {br.close();} catch (IOException e) {}
		}
	}
	
	private static String getTargetReducerTaskNumInteractively(String jobPlanFormalName, BufferedReader br) throws Exception {
		Integer reducerTaskNum = 0;
		System.out.println("\n Selected job plan is :["+jobPlanFormalName+"] \n");
		System.out.print("please enter the jobplan's reduce task number or enter 'exit' to quit:  ");
		String reducerTaskNumStr = null;
		while (true) {
			try {
				reducerTaskNumStr = br.readLine();
				if (reducerTaskNumStr.equalsIgnoreCase("exit") 
						|| reducerTaskNumStr.equalsIgnoreCase("quit") 
							|| reducerTaskNumStr.equalsIgnoreCase("q") ) {
					System.out.println("exit, bye...");
					throw new Exception("Exit exception");
				}
				reducerTaskNum = Integer.parseInt(reducerTaskNumStr);
				if (reducerTaskNum > 0) {
					System.out.println("\n Selected job plan is :["+jobPlanFormalName+"] \n");
					System.out.print("Do you confirm to execute the reducer task number:\t ["+reducerTaskNum +"] ?  [Y\\n]:  ");
					String confirm = br.readLine();
					if (confirm.equals("Y")) {
						return reducerTaskNum+"";
					} else {
						System.out.println("\n Selected job plan is :["+jobPlanFormalName+"] \n");
						System.out.print("you have discard the last determined reducer task number ["+reducerTaskNum+"], try again or enter 'exit' to quit:  ");
					}
				} else {
					System.out.println("\n Selected job plan is :["+jobPlanFormalName+"] \n");
					System.out.print("the input reducer task number ["+reducerTaskNum+"] out of range, try again or enter 'exit' to quit:  ");
				}
			} catch (NumberFormatException nfe) {
				System.out.println("\n Selected job plan is :["+jobPlanFormalName+"] \n");
				System.out.print("NumberFormatException...try again or enter 'exit' to quit:  ");
			}
		}
	}
	
	private static String getTargetFormalClassNameInteractively(String rootPack, Class<?> filter, BufferedReader br) throws Exception {
		String jobPlanFormalName = null;
		List<Class<?>> classList = UtilPackageScan.getMatchedBizClassList(rootPack, filter);
		printAvailableJobPlans(classList);
		System.out.print("please enter the jobplan's number you want to execute or enter 'exit' to quit:  ");
		String jobPlanNumberStr = null;
		while (true) {
			try {
				jobPlanNumberStr = br.readLine();
				if (jobPlanNumberStr.equalsIgnoreCase("exit") 
						|| jobPlanNumberStr.equalsIgnoreCase("quit") 
							|| jobPlanNumberStr.equalsIgnoreCase("q") ) {
					System.out.println("exit, bye...");
					throw new Exception("Exit exception");
				}
				int jobPlanNumber = Integer.parseInt(jobPlanNumberStr);
				if (jobPlanNumber >=0 && jobPlanNumber < classList.size()) {
					jobPlanFormalName = classList.get(jobPlanNumber).getName();
					System.out.print("Do you confirm to execute the jobPlan:"+classList.get(jobPlanNumber).getName()+"?  [Y\\n]:  ");
					String confirm = br.readLine();
					if (confirm.equals("Y")) {
						return jobPlanFormalName;
					} else {
						printAvailableJobPlans(classList);
						System.out.print("you have discard the last selection["+jobPlanNumber+"], try again or enter 'exit' to quit:  ");
					}
				} else {
					printAvailableJobPlans(classList);
					System.out.print("the input jobPlanNumber["+jobPlanNumber+"] out of range, joblist size is ["+classList.size()+"], try again or enter 'exit' to quit:  ");
				}
			} catch (NumberFormatException nfe) {
				printAvailableJobPlans(classList);
				System.out.print("NumberFormatException...try again or enter 'exit' to quit:  ");
			}
		}
	}

	private static void printAvailableJobPlans(List<Class<?>> classList) {
		System.out.println("==============available jobplan list===============\n");
		for (int i=0; i<classList.size(); i++) {
			Class<?> clz = classList.get(i);
			System.out.println("["+i+"]\t"+clz.getName()+"\n");
		}
		System.out.println("==============available jobplan list===============\n");
	}
}
