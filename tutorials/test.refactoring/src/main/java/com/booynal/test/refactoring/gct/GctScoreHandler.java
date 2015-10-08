package com.booynal.test.refactoring.gct;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class GctScoreHandler {

	public static final int[] WEIGHT = new int[] {2, 4, 2, 2};
	private List<Character> answerList = new ArrayList<Character>(Arrays.asList(new Character[] {'A', 'B', 'C', 'D'}));
	private List<String> messages = new ArrayList<String>();
	
	public int[] calc(List<String> standardAnswers, List<String> myAnswers, int[] weight) {
		int totalScore = 0;
		int[] scores = new int[5];
		for (int i = 0; i < weight.length; i ++) {
			int w = weight[i];
			scores[i] = calcOne(standardAnswers.get(i), myAnswers.get(i), w);
			totalScore += scores[i];
		}
		
		System.out.println("total score=" + totalScore);
		scores[scores.length - 1] = totalScore;
		return scores;
	}

	private int calcOne(String standardAnswer, String myAnswer, int weight) {
		StringBuilder message = new StringBuilder();
		int score = 0;
		for (int i = 0; i < standardAnswer.length(); i ++) {
			char aStandardAnswer = standardAnswer.charAt(i);
			char aMyAnswer = myAnswer.charAt(i);
			if (isAnswer(aStandardAnswer) == false || isAnswer(aMyAnswer) == false) {
				continue;
			}
			if (aStandardAnswer == aMyAnswer) {
				score += weight;
			} else {
				message.append(String.format("%d{%s|%s},", (i+1), aStandardAnswer, aMyAnswer));
			}
		}
		messages.add(message.toString());
		System.out.println("score=" + score + ", w=" + weight + "\t" + message.toString());
		return score;
	}

	private boolean isAnswer(char answer) {
		return answerList.contains(Character.toUpperCase(answer));
	}

	public List<String> getMessages() {
		return messages;
	}

}
