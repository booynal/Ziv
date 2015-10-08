package com.booynal.test.refactoring.gct.year2011;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.booynal.test.refactoring.gct.GctScoreHandler;

public class Year2011 {

	public static void main(String[] args) throws IOException, URISyntaxException {
		GctScoreHandler handler = new GctScoreHandler();
		List<String> standardAnswers = Files.readAllLines(Paths.get(Year2011.class.getResource("0").toURI()), Charset.defaultCharset());
		List<String> myAnswers = Files.readAllLines(Paths.get(Year2011.class.getResource("1").toURI()), Charset.defaultCharset());
		handler.calc(standardAnswers, myAnswers, GctScoreHandler.WEIGHT);
	}

}
