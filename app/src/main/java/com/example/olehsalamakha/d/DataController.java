package com.example.olehsalamakha.d;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by olehsalamakha on 1/21/15.
 */
public class DataController {

	static final String TAG = "DataController";
	private WordsAdapter mDictionaryAdapter;
	private ArrayList<Word> mWords = new ArrayList<Word>();
	private int mCountAllWords = 0;

	private ArrayList<String> mSearchList;
	private ArrayAdapter<String> mSearchAdapter;
	private DBHelper mDbHelper;
	private Context mContext;
	private Activity mActivity;
	private static DataController mDataController = null;



	public static DataController getInstance(Context context, Activity activity) {
		if (mDataController == null) {
			mDataController = new DataController(context, activity);
		}

		return  mDataController;
	}
	private  DataController(Context context, Activity activity) {
		mContext = context;
		mActivity = activity;
		mDbHelper = DBHelper.getInstance(mContext);
	}

	public void initializeDBHelper() {
		mDbHelper = DBHelper.getInstance(mContext);
	}
	public void addDataToDictAdapter() {
		mDictionaryAdapter.addAll(mDbHelper.selectWords());
		mDictionaryAdapter.notifyDataSetChanged();
	}

	public void updateWord(Word w) {
		for (int i = 0; i < mWords.size(); i++) {
			if (w.getWord().equals(mWords.get(i).getWord())) {
				mWords.get(i).setCountValidanswer(w.getCountValidAnswer());
				mWords.get(i).setCountAnswer(w.getCountAnswer());
				mDictionaryAdapter.notifyDataSetChanged();
				break;
			}
		}
		mDbHelper.update(w);
	}


	//data controller initialize SearchAdapter
	public void initializeSearchAdapter() {
		if (mSearchList != null) {
			mSearchList.clear();
		}
		mSearchList = mDbHelper.selectAllWord();
		mSearchAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_dropdown_item_1line, mSearchList);
		mSearchAdapter.notifyDataSetChanged();
	}


	public void refreshDB() {
		mDbHelper.refresh();
	}


	public int getCountAllWords() {
		mCountAllWords = mDbHelper.getCountOfWords();
		return mCountAllWords;
	}

	//initializeDictAdapter
	public void initializeDictAdapter() {
		//if (mCountAllWords > 0) {
			mWords.addAll(mDbHelper.selectWords());
			mDictionaryAdapter = new WordsAdapter(mActivity, mWords);
		//}
	}

	public void addWordstoList() {
		ArrayList<Word> l = mDbHelper.selectWords();
		mWords.addAll(l);

		if (mDictionaryAdapter == null) {
			mDictionaryAdapter = new WordsAdapter(mActivity, mWords);
		}

		mDictionaryAdapter.notifyDataSetChanged();

	}



	public WordsAdapter getDictionaryAdapter() {
		if (mDictionaryAdapter == null) {
			Log.d(TAG, "getDictionaryAdapter return null");
		}else {
			Log.d(TAG, "getDictionaryAdapter return good adapter");
		}
		return mDictionaryAdapter;
	}

	public ArrayList<Word> getWordList() {
		return mWords;
	}

	public Word getWord(String word) {
		return mDbHelper.selectWord(word);
	}


	public ArrayList<String> getSearchList() {
		return mSearchList;
	}

	public ArrayAdapter<String> getSearchAdapter() {
		mSearchAdapter.notifyDataSetChanged();
		return mSearchAdapter;
	}

	public void removeWord(int position, String word) {
		mDbHelper.deleteWord(word);
		mWords.remove(position);
		mDictionaryAdapter.notifyDataSetChanged();


		for (int i=0; i<mSearchList.size(); i++) {
			if (mSearchList.get(i).equals(word)) {
				mSearchList.remove(i);
				break;
			}
		}

		initializeSearchAdapter();



	}

	public void addWordtoDb(Word word) {
		try {
			mDbHelper.insertWord(word);
			initializeSearchAdapter();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}







}
