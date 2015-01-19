package com.example.olehsalamakha.d;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by olehsalamakha on 1/19/15.
 */
public class Test {
	static final String TAG = "Test";
	private ArrayList<Question> mQuestions;
	int currentQuestion = 0;
	public Test() {

	}

	public Test(ArrayList<Question> questions) {
		mQuestions = questions;
	}

	public int getQuestionCount() {
		return mQuestions.size();
	}

	public Question getCurrentQuestion() {
		if (currentQuestion >= mQuestions.size()) {
			return null;
		}
		Question q = mQuestions.get(currentQuestion);
		Log.e(TAG, "getCurrentQuestion");
		return q;

	}

	public void goToNextQuestion() {
		currentQuestion += 1;
	}

	public boolean checkAnswer(int index, String answer) {
		boolean result = false;
		if (mQuestions.get(index).getword().getTranslations().get(0).equals(answer)) {
			result = true;
		}
		return result;
	}


}
