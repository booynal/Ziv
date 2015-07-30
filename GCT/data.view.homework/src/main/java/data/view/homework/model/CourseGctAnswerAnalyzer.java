package data.view.homework.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import data.view.homework.consts.ABCDConsts;

public class CourseGctAnswerAnalyzer {

	private CourseGctAnswer answer;
	private int countAbcd;
	private int countA;
	private int countB;
	private int countC;
	private int countD;
	
	private boolean isUpdated;
	
	private Map<String, Double> ratios = new HashMap<String, Double>();
	
	public CourseGctAnswerAnalyzer(CourseGctAnswer answer) {
		this.answer = answer;
	}
	
	public void update() {
		if (isUpdated) {
			throw new RuntimeException("already updated.");
		}
		List<String> answers = answer.getAnswers();
		for (String an : answers) {
			countAbcd++;
			switch (an) {
			case ABCDConsts.A:
				countA++;
				break;
			case ABCDConsts.B:
				countB++;
				break;
			case ABCDConsts.C:
				countC++;
				break;
			case ABCDConsts.D:
				countD++;
				break;
			}
		}
		
		udpateRatio(ABCDConsts.A, countA);
		udpateRatio(ABCDConsts.B, countB);
		udpateRatio(ABCDConsts.C, countC);
		udpateRatio(ABCDConsts.D, countD);
		
		isUpdated = true;
	}

	private void udpateRatio(String abcd, int countOne) {
		ratios.put(abcd, 1D * countOne / countAbcd);
	}


	public int getCountA() {
		return countA;
	}

	public int getCountB() {
		return countB;
	}

	public int getCountC() {
		return countC;
	}

	public int getCountD() {
		return countD;
	}

	public int getCountAbcd() {
		return countAbcd;
	}

	public boolean isUpdated() {
		return isUpdated;
	}

}
