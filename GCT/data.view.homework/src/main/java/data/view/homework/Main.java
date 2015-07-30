package data.view.homework;

import java.io.IOException;

import data.view.homework.model.MultiYearGctAnswer;
import data.view.homework.parse.FileHandler;
import data.view.homework.parse.Parser;
import data.view.homework.parse.Reporter;

public class Main {

	public static void main(String[] args) throws IOException {
		Parser parser = new Parser();
		MultiYearGctAnswer multiYearGctAnswer = parser.parse();
		Reporter reporter = new Reporter(multiYearGctAnswer);
		String abcdLayout = reporter.toLayoutJson("layoutData");
		FileHandler fileHandler = new FileHandler("data.js");
		fileHandler.write(abcdLayout);
		
	}

}
