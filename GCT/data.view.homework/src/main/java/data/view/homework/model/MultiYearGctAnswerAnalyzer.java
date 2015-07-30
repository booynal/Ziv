package data.view.homework.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import data.view.homework.consts.ABCDConsts;

public class MultiYearGctAnswerAnalyzer {

	private int totalAbcd;
	private int totalA;
	private int totalB;
	private int totalC;
	private int totalD;
	private MultiYearGctAnswer multiYearGctAnswer;

	private Map<String, Double> ratios = new HashMap<String, Double>();

	private boolean isUpdated;

	public MultiYearGctAnswerAnalyzer(MultiYearGctAnswer multiYearGctAnswer) {
		this.multiYearGctAnswer = multiYearGctAnswer;
	}
	
	public void update() {
		if (isUpdated) {
			throw new RuntimeException("Already updated.");
		}
		List<OneYearGctAnswer> gctAnswers = multiYearGctAnswer.getOneYearGctAnswers();
		for (OneYearGctAnswer gctAnswer : gctAnswers) {
			updateOneCourse(gctAnswer.getChinese());
			updateOneCourse(gctAnswer.getMath());
			updateOneCourse(gctAnswer.getLogic());
			updateOneCourse(gctAnswer.getEnglish());
		}

		udpateRatio(ABCDConsts.A, totalA);
		udpateRatio(ABCDConsts.B, totalB);
		udpateRatio(ABCDConsts.C, totalC);
		udpateRatio(ABCDConsts.D, totalD);
		
		isUpdated = true;
	}

	private void udpateRatio(String abcd, int countOne) {
		ratios.put(abcd, 1D * countOne / totalAbcd);
	}

	private void updateOneCourse(CourseGctAnswer courseGctAnswer) {
		CourseGctAnswerAnalyzer analyzer = courseGctAnswer.getAnalyzer();
		if (analyzer.isUpdated() == false) {
			analyzer.update();
		}
		totalA += analyzer.getCountA();
		totalB += analyzer.getCountB();
		totalC += analyzer.getCountC();
		totalD += analyzer.getCountD();
		totalAbcd += analyzer.getCountAbcd();
	}

	public int getTotalA() {
		return totalA;
	}

	public int getTotalB() {
		return totalB;
	}

	public int getTotalC() {
		return totalC;
	}

	public int getTotalD() {
		return totalD;
	}

	public boolean isUpdated() {
		return isUpdated;
	}

	public Map<String, Double> getRatios() {
		return ratios;
	}


}
