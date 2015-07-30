package data.view.homework.model;

import java.util.HashMap;
import java.util.Map;

import data.view.homework.consts.ABCDConsts;

public class OneYearGctAnswerAnalyzer {

	private OneYearGctAnswer oneYearGctAnswer;
	private int countAbcd;
	private int countA;
	private int countB;
	private int countC;
	private int countD;

	private Map<String, Double> ratios = new HashMap<String, Double>();
	
	private boolean isUpdated;

	public OneYearGctAnswerAnalyzer(OneYearGctAnswer oneYearGctAnswer) {
		this.oneYearGctAnswer = oneYearGctAnswer;
	}
	
	public void update() {
		if (isUpdated) {
			throw new RuntimeException("Already updated.");
		}
		
		updateOneCourse(oneYearGctAnswer.getChinese());
		updateOneCourse(oneYearGctAnswer.getMath());
		updateOneCourse(oneYearGctAnswer.getLogic());
		updateOneCourse(oneYearGctAnswer.getEnglish());
		
		udpateRatio(ABCDConsts.A, countA);
		udpateRatio(ABCDConsts.B, countB);
		udpateRatio(ABCDConsts.C, countC);
		udpateRatio(ABCDConsts.D, countD);
		
		isUpdated = true;
	}

	private void udpateRatio(String abcd, int countOne) {
		ratios.put(abcd, 1D * countOne / countAbcd);
	}

	private void updateOneCourse(CourseGctAnswer chinese) {
		CourseGctAnswerAnalyzer analyzer = chinese.getAnalyzer();
		if (analyzer.isUpdated() == false) {
			analyzer.update();
		}
		countA += analyzer.getCountA();
		countB += analyzer.getCountB();
		countC += analyzer.getCountC();
		countD += analyzer.getCountD();
		countAbcd += analyzer.getCountAbcd();
	}

	public int getCountAbcd() {
		return countAbcd;
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

	public Map<String, Double> getRatios() {
		return ratios;
	}

	public boolean isUpdated() {
		return isUpdated;
	}
	
}
