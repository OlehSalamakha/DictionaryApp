package com.example.olehsalamakha.d;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by olehsalamakha on 1/13/15.
 */
public class Word implements Parcelable {

	private String mWord;
	private int mCountAnswer = 0;
	private int mCountValidAnswer = 0;
	private ArrayList<String> mTranslationsList;
	public Word(String word, ArrayList<String> translations, int countAnswer, int countValidAnswer) {
		mWord = word;
		mTranslationsList = translations;
		mCountAnswer = countAnswer;
		mCountValidAnswer = countValidAnswer;
	}

	public Word(String word, String translation) {
		mWord = word;
		mTranslationsList = new ArrayList<>();
		mTranslationsList.add(translation);
	}

	public Word(String word, ArrayList<String> translations) {
		mWord = word;
		mTranslationsList = translations;
	}

	public Word(Parcel p) {
		String data[] = new String [p.dataSize()];
		p.readStringArray(data);

		this.mWord = data[0];
		this.mCountAnswer = Integer.parseInt(data[1]);
		this.mCountValidAnswer = Integer.parseInt(data[2]);


		for (int i=3; i<data.length; i++) {
			this.mTranslationsList.add(data[i]);
		}
	}


	public String getWord() {
		return mWord;
	}

	public ArrayList<String> getTranslations() {
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

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		String[] dataArr = new String[3+mTranslationsList.size()];

		dataArr[0] = mWord;
		dataArr[1] = Integer.toString(mCountAnswer);
		dataArr[2] = Integer.toString(mCountValidAnswer);

		for (int i=0; i<mTranslationsList.size(); i++) {
			dataArr[i+3] = mTranslationsList.get(i);
		}

		dest.writeStringArray(dataArr);
	}


	public static final Parcelable.Creator<Word> CREATOR= new Parcelable.Creator<Word>() {
		@Override
		public Word createFromParcel(Parcel source) {
			return new Word(source);
		}

		@Override
		public Word[] newArray(int size) {
			return new Word[size];
		}
	};
}
