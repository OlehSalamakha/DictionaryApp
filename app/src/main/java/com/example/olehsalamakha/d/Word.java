package com.example.olehsalamakha.d;

import java.util.List;

/**
 * Created by olehsalamakha on 1/13/15.
 */
public class Word {

	private String mWord;
	private List<String> mTranslationsList;
	private int mCountAnswer = 0;
	private int mCountValidAnswer = 0;
	public Word(String word, List<String> translations) {
		mWord = word;
		mTranslationsList = translations;
	}


	public String getWord() {
		return mWord;
	}

	public List<String> getTranslations() {
		return mTranslationsList;
	}

	public int  getCountAnswer() {
		return mCountAnswer;
	}

	public void setCountAnswer(int count) {
		mCountAnswer = count;
	}

	public int getCountValidAnswer() {
		return mCountValidAnswer;
	}

	public void setCountValidanswer(int count) {
		mCountValidAnswer = count;
	}


}
