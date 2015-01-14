package com.example.olehsalamakha.d;

import java.util.List;

/**
 * Created by olehsalamakha on 1/13/15.
 */
public class Word {

	private String mWord;
	private List<String> mTranslationsList;
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
}
