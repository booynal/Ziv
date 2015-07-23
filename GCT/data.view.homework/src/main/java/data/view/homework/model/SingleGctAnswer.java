package data.view.homework.model;

import java.util.List;

public class SingleGctAnswer {
	
	private String name;
	private int year;
	private int count;
	private List<String> answers;
	
	public SingleGctAnswer() {
		super();
	}
	public SingleGctAnswer(String name, int year, int count,
			List<String> answers) {
		super();
		this.name = name;
		this.year = year;
		this.count = count;
		this.answers = answers;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public List<String> getAnswers() {
		return answers;
	}
	public void setAnswers(List<String> answers) {
		this.answers = answers;
	}

}
