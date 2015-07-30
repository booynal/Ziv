package data.view.homework.model;

import java.util.List;

public class MultiYearGctAnswer {
	
	List<OneYearGctAnswer> oneYearGctAnswers;
	MultiYearGctAnswerAnalyzer multiYearGctAnswerAnalyzer;

	public MultiYearGctAnswer() {
		this.multiYearGctAnswerAnalyzer = new MultiYearGctAnswerAnalyzer(this);
	}

	public List<OneYearGctAnswer> getOneYearGctAnswers() {
		return oneYearGctAnswers;
	}

	public void setOneYearGctAnswers(List<OneYearGctAnswer> oneYearGctAnswers) {
		this.oneYearGctAnswers = oneYearGctAnswers;
	}


}
