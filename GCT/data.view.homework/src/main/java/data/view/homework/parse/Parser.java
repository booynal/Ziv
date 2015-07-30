package data.view.homework.parse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import data.view.homework.consts.CourseConsts;
import data.view.homework.model.CourseGctAnswer;
import data.view.homework.model.MultiYearGctAnswer;
import data.view.homework.model.OneYearGctAnswer;

public class Parser {
	
	
	public MultiYearGctAnswer parse() throws IOException {
		return load();
	}

	private MultiYearGctAnswer load() throws IOException {
		InputStream inputStream = Parser.class.getClassLoader().getResourceAsStream("GCTAnswerData.csv");
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
		
		MultiYearGctAnswer multiYearGctAnswer = new MultiYearGctAnswer();
		List<OneYearGctAnswer> oneYearGctAnswers = new ArrayList<OneYearGctAnswer>();
		multiYearGctAnswer.setOneYearGctAnswers(oneYearGctAnswers);
		OneYearGctAnswer oneYearGctAnswer = null;
		
		String line = null;
		int index = 0;
		while (null != (line = br.readLine())) {
//			System.out.println(line);
			if (line.isEmpty()) {
				continue;
			}
			switch (index) {
			case 0:
				oneYearGctAnswer = new OneYearGctAnswer();
				int year = Integer.parseInt(line.substring(0, line.indexOf(',')));
				oneYearGctAnswer.setYear(year);
//				yearToGctAnswer.put(year, oneYearGctAnswer);
				oneYearGctAnswers.add(oneYearGctAnswer);
				break;
			case 1:
				CourseGctAnswer chinese = parseOneLine(line);
				chinese.setName(CourseConsts.CHINESE);
				oneYearGctAnswer.setChinese(chinese);
				break;
			case 2:
				CourseGctAnswer math = parseOneLine(line);
				math.setName(CourseConsts.MATH);
				oneYearGctAnswer.setMath(math);
				break;
			case 3:
				CourseGctAnswer logic = parseOneLine(line);
				logic.setName(CourseConsts.LOGIC);
				oneYearGctAnswer.setLogic(logic);
				break;
			case 4:
				CourseGctAnswer english = parseOneLine(line);
				english.setName(CourseConsts.ENGLISH);
				oneYearGctAnswer.setEnglish(english);
				break;
			}
			index = ++index % 5;
		}
//		System.out.println(yearToGctAnswer);
		return multiYearGctAnswer;
	}

	private CourseGctAnswer parseOneLine(String line) {
		List<String> answers = new ArrayList<String>();
		for (char ch : line.toCharArray()) {
			if ('A' == ch || 'B' == ch || 'C' == ch || 'D' == ch) {
				answers.add(String.valueOf(ch));
			}
		}
		CourseGctAnswer chinese = new CourseGctAnswer();
		chinese.setAnswers(answers);
		chinese.setCount(answers.size());
		return chinese;
	}

}
