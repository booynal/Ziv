package data.view.homework.model;

public class OneYearGctAnswer {

	private int year;
	private CourseGctAnswer chinese;
	private CourseGctAnswer math;
	private CourseGctAnswer logic;
	private CourseGctAnswer english;
	private OneYearGctAnswerAnalyzer oneYearGctAnswerAnalyzer;
	
	public OneYearGctAnswer() {
		oneYearGctAnswerAnalyzer = new OneYearGctAnswerAnalyzer(this);
	}
	
	public CourseGctAnswer getChinese() {
		return chinese;
	}
	public void setChinese(CourseGctAnswer chinese) {
		this.chinese = chinese;
	}
	public CourseGctAnswer getMath() {
		return math;
	}
	public void setMath(CourseGctAnswer math) {
		this.math = math;
	}
	public CourseGctAnswer getLogic() {
		return logic;
	}
	public void setLogic(CourseGctAnswer logic) {
		this.logic = logic;
	}
	public CourseGctAnswer getEnglish() {
		return english;
	}
	public void setEnglish(CourseGctAnswer english) {
		this.english = english;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	@Override
	public String toString() {
		return "GctAnswer [year=" + year + ", chinese=" + chinese + ", math="
				+ math + ", logic=" + logic + ", english=" + english + "]";
	}
	
}
