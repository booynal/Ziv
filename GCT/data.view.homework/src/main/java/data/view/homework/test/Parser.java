package data.view.homework.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import data.view.homework.consts.GctAnswerConsts;
import data.view.homework.model.GctAnswer;
import data.view.homework.model.SingleGctAnswer;

public class Parser {
	
	public static final String KEY_CHINESE = "ÓïÎÄ";
	public static final String KEY_MATH = "ÊýÑ§";
	public static final String KEY_LOGIC = "Âß¼­";
	public static final String KEY_ENGLISH = "Ó¢Óï";

	public static void main(String[] args) throws IOException {
		Parser test = new Parser();
		test.before();
		test.test();
	}
	
	private Map<String, String> choiceToValue = new HashMap<String, String>();
	private Map<Integer, GctAnswer> yearToGctAnswer = new HashMap<Integer, GctAnswer>();
	
	
	private void before() throws IOException {
		choiceToValue.put(GctAnswerConsts.A, "10");
		choiceToValue.put(GctAnswerConsts.B, "20");
		choiceToValue.put(GctAnswerConsts.C, "30");
		choiceToValue.put(GctAnswerConsts.D, "40");
		
		InputStream inputStream = Parser.class.getClassLoader().getResourceAsStream("GCTAnswerData.csv");
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
		
		GctAnswer answer = null;
		
		String line = null;
		int index = 0;
		while (null != (line = br.readLine())) {
			System.out.println(line);
			if (line.isEmpty()) {
				continue;
			}
			switch (index) {
			case 0:
				answer = new GctAnswer();
				int year = Integer.parseInt(line.substring(0, line.indexOf(',')));
				answer.setYear(year);
				yearToGctAnswer.put(year, answer);
				break;
			case 1:
				SingleGctAnswer chinese = parseAnswer(line);
				chinese.setName(KEY_CHINESE);
				answer.setChinese(chinese);
				break;
			case 2:
				SingleGctAnswer math = parseAnswer(line);
				math.setName(KEY_MATH);
				answer.setMath(math);
				break;
			case 3:
				SingleGctAnswer logic = parseAnswer(line);
				logic.setName(KEY_LOGIC);
				answer.setLogic(logic);
				break;
			case 4:
				SingleGctAnswer english = parseAnswer(line);
				english.setName(KEY_ENGLISH);
				answer.setEnglish(english);
				break;
			}
			index = ++index % 5;
		}
		System.out.println(yearToGctAnswer);
	}

	private SingleGctAnswer parseAnswer(String line) {
		List<String> answers = new ArrayList<String>();
		for (char ch : line.toCharArray()) {
			if ('A' == ch || 'B' == ch || 'C' == ch || 'D' == ch) {
				answers.add(String.valueOf(ch));
			}
		}
		SingleGctAnswer chinese = new SingleGctAnswer();
		chinese.setAnswers(answers);
		chinese.setCount(answers.size());
		return chinese;
	}

	private void test() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		
		GctAnswer gctAnswer = yearToGctAnswer.get(2013);
		generateOne(sb, KEY_CHINESE, gctAnswer.getChinese().getAnswers());
		sb.append(",");
		generateOne(sb, KEY_MATH, gctAnswer.getMath().getAnswers());
		sb.append(",");
		generateOne(sb, KEY_LOGIC, gctAnswer.getLogic().getAnswers());
		sb.append(",");
		generateOne(sb, KEY_ENGLISH, gctAnswer.getEnglish().getAnswers());
		
		sb.append("]");
		
		
		System.out.println(sb.toString());
	}

	private void generateOne(StringBuilder sb, String rowid, List<String> list) {
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
