package com.example.olehsalamakha.d;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by olehsalamakha on 1/19/15.
 */
public class Question {
	private Word mWord;
	private ArrayList<String> mVariants = new ArrayList<>();

	public Question() {

	}

	public Question(Word word, ArrayList<String> variants) {
		mWord = word;
		mVariants = variants;
	}

	public Question(Word word, String variant) {
		mWord = word;
		mVariants.add(variant);
	}


	public void setWord(Word word) {
		mWord = word;
	}

	public void setVariants(ArrayList<String> variants) {
		mVariants = variants;
	}

	public boolean checkQuestion(String variant) {
		boolean result = false;
		if (mWord.getTranslations().get(0) == variant) {
			result = true;
		}
		return result;
	}

	public Word getword() {
		return mWord;
	}

	public String getVariant() {
		return mVariants.get(0);
	}
}
