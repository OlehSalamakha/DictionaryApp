package com.example.olehsalamakha.d;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by olehsalamakha on 1/19/15.
 */
public class TestBuilder {
	static final String TAG = "TestBuilder";
	public static Test createTest(Context context) {
		Log.e(TAG, " createTest: ");
		DBHelper dbHelper = DBHelper.getInstance(context);

		ArrayList<Word> words = dbHelper.getLastWords(10);
		ArrayList<Question> questions = new ArrayList<>();

		Random r = new Random();
		int wordsCount = words.size();
		for (int i=0; i<wordsCount; i++) {
			int randomNumber = r.nextInt(wordsCount);
			while (randomNumber == i) {
				randomNumber = r.nextInt(wordsCount);
			}
			String variant = words.get(randomNumber).getTranslations().get(0);
			Question q = new Question(words.get(i), variant);

			questions.add(q);
			Log.e(TAG, q.getword().getWord() + " translation: " + q.getVariant());
		}

		Test test = new Test(questions);
		return test;
	}
}
