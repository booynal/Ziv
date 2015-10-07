package com.booynal.test.refactoring.gct;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


public class GctScoreHandler {

	public static void main(String[] args) throws IOException, URISyntaxException {
		List<String> standardAnswers = Files.readAllLines(Paths.get(ClassLoader.getSystemResource("0").toURI()), Charset.defaultCharset());
		List<String> myAnswers = Files.readAllLines(Paths.get(ClassLoader.getSystemResource("1").toURI()), Charset.defaultCharset());
		int[] wight = new int[] {2, 4, 2, 2};
		GctScoreHandler handler = new GctScoreHandler();
		handler.calc(standardAnswers, myAnswers, wight);
	}

	public int[] calc(List<String> standardAnswers, List<String> myAnswers, int[] wight) {
		String standardAnswer = "";
		String myAnswer = "";
		
		int totalScore = 0;
		int[] scores = new int[5];
		try {
			for (int i = 0; i < wight.length; i ++) {
				int w = wight[i];
				standardAnswer = standardAnswers.get(i);
				myAnswer = myAnswers.get(i);
				
				StringBuilder sb = new StringBuilder();
				int score = 0;
				for (int j = 0; j < standardAnswer.length(); j ++) {
					char aStandardAnswer = standardAnswer.charAt(j);
					char aMyAnswer = myAnswer.charAt(j);
					if (isAnswer(aStandardAnswer) == false || isAnswer(aMyAnswer) == false) {
						continue;
					}
					if (aStandardAnswer == aMyAnswer) {
						score += w;
					} else {
						sb.append(String.format("%d{%s|%s},", (j+1), aStandardAnswer, aMyAnswer));
					}
				}
				System.out.println("score=" + score + ", w=" + w + "\t" + sb.toString());
				totalScore += score;
				scores[i] = score;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("total score=" + totalScore);
		scores[scores.length - 1] = totalScore;
		return scores;
	}

	private boolean isAnswer(char answer) {
		if ('A' == answer || 'B' == answer || 'C' == answer || 'D' == answer) {
			return true;
		}
		return false;
	}

}
