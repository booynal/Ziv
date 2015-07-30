package data.view.homework.parse;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import data.view.homework.consts.ABCDConsts;
import data.view.homework.consts.CourseConsts;
import data.view.homework.model.CourseGctAnswer;
import data.view.homework.model.MultiYearGctAnswer;
import data.view.homework.model.OneYearGctAnswer;

public class Reporter {
	public static Map<String, String> choiceToValue = new HashMap<String, String>();
	public MultiYearGctAnswer multiYearGctAnswer;

	static {
		before();
	}
	
	private static void before() {
		choiceToValue.put(ABCDConsts.A, "10");
		choiceToValue.put(ABCDConsts.B, "20");
		choiceToValue.put(ABCDConsts.C, "30");
		choiceToValue.put(ABCDConsts.D, "40");
	}


	public Reporter(MultiYearGctAnswer multiYearGctAnswer) {
		this.multiYearGctAnswer = multiYearGctAnswer;
	}

	public String toLayoutJson(String variableName) {
		StringBuilder sb = new StringBuilder();
		sb.append("var ").append(variableName).append("=");
		sb.append("{");
		sb.append("'title':'MultiYearGctAnswer','data':[");
		sb.append(System.lineSeparator());
		for (Iterator<OneYearGctAnswer> iterator = multiYearGctAnswer.getOneYearGctAnswers().iterator(); iterator.hasNext();) {
			sb.append(generateLayoutByOneYearGctAnswer(iterator.next()));
			if (iterator.hasNext()) {
				sb.append(",");
			}
			sb.append(System.lineSeparator());
		}
		sb.append("]}");
		sb.append(System.lineSeparator());
		return sb.toString();
	}
	
	public StringBuilder generateLayoutByYear(int year) {
		OneYearGctAnswer oneYearGctAnswer = null;
		for (OneYearGctAnswer answer : multiYearGctAnswer.getOneYearGctAnswers()) {
			if (year == answer.getYear()) {
				oneYearGctAnswer = answer;
			}
		}
		return generateLayoutByOneYearGctAnswer(oneYearGctAnswer);
	}

	private StringBuilder generateLayoutByOneYearGctAnswer(OneYearGctAnswer oneYearGctAnswer) {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("'year':'").append(oneYearGctAnswer.getYear()).append("',");
		sb.append("'data':[");
		generateLayoutByOneCourse(sb, CourseConsts.CHINESE, oneYearGctAnswer.getChinese());
		sb.append(",");
		generateLayoutByOneCourse(sb, CourseConsts.MATH, oneYearGctAnswer.getMath());
		sb.append(",");
		generateLayoutByOneCourse(sb, CourseConsts.LOGIC, oneYearGctAnswer.getLogic());
		sb.append(",");
		generateLayoutByOneCourse(sb, CourseConsts.ENGLISH, oneYearGctAnswer.getEnglish());
		sb.append("]");
		sb.append("}");
		return sb;
	}

	private void generateLayoutByOneCourse(StringBuilder sb, String rowid, CourseGctAnswer courseGctAnswer) {
		List<String> list = courseGctAnswer.getAnswers();
		int index = 1;
		for (Iterator<String> iterator = list.iterator(); iterator.hasNext();) {
			String choice = iterator.next();
			sb.append("{");
			sb.append("'rowid':'").append(rowid).append("',");
			sb.append("'columnid':'").append(index++).append("',");
			sb.append("'value':'").append(choiceToValue.get(choice)).append("'");
			sb.append("}");
			if (iterator.hasNext()) {
				sb.append(",");
			}
		}
	}

}