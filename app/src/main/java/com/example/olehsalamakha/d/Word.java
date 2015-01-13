package com.example.olehsalamakha.d;

import java.util.List;

/**
 * Created by olehsalamakha on 1/13/15.
 */
public class Word {

	public Word(String word, List<String> translations) {
		word_ = word;
		translations_ = translations;
	}


	public String getWord() {
		return word_;
	}

	public List<String> getTranslations() {
		return translations_;
	}

	private String word_;
	private List<String> translations_;
}
