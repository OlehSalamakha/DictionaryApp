package com.example.olehsalamakha.d;

import java.util.ArrayList;

/**
 * Created by olehsalamakha on 1/19/15.
 */
public class Test {
	private ArrayList<Question> mQuestions;
	public Test() {

	}

	public Test(ArrayList<Question> questions) {
		mQuestions = questions;
	}

	public boolean checkAnswer(int index, String answer) {
		boolean result = false;
		if (mQuestions.get(index).getword().getTranslations().get(0).equals(answer)) {
			result = true;
		}
		return result;
	}
}
