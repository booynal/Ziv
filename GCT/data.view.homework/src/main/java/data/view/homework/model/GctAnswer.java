package data.view.homework.model;

public class GctAnswer {

	private int year;
	private SingleGctAnswer chinese;
	private SingleGctAnswer math;
	private SingleGctAnswer logic;
	private SingleGctAnswer english;
	
	public GctAnswer() {
		super();
	}
	public SingleGctAnswer getChinese() {
		return chinese;
	}
	public void setChinese(SingleGctAnswer chinese) {
		this.chinese = chinese;
	}
	public SingleGctAnswer getMath() {
		return math;
	}
	public void setMath(SingleGctAnswer math) {
		this.math = math;
	}
	public SingleGctAnswer getLogic() {
		return logic;
	}
	public void setLogic(SingleGctAnswer logic) {
		this.logic = logic;
	}
	public SingleGctAnswer getEnglish() {
		return english;
	}
	public void setEnglish(SingleGctAnswer english) {
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
