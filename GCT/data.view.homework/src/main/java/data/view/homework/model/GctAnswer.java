package data.view.homework.model;

public class GctAnswer {

	private SingleGctAnswer chinese;
	private SingleGctAnswer math;
	private SingleGctAnswer logic;
	private SingleGctAnswer english;
	
	public GctAnswer() {
		super();
	}
	public GctAnswer(SingleGctAnswer chinese, SingleGctAnswer math,
			SingleGctAnswer logic, SingleGctAnswer english) {
		super();
		this.chinese = chinese;
		this.math = math;
		this.logic = logic;
		this.english = english;
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
	
}
