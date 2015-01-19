package com.example.olehsalamakha.d;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;


/**
 * Created by olehsalamakha on 1/13/15.
 */
public class WordsAdapter extends ArrayAdapter {

	static final String TAG = "WordsAdapter";
	private LayoutInflater mInflater;
	private ArrayList<Word> mWords;
	private WordsAdapter mWordsAdapter=null;

	public WordsAdapter(Activity activity, ArrayList<Word> words) {
		super(activity, R.layout.word_layout, words);

		mWords = words;
		mInflater = activity.getWindow().getLayoutInflater();
		mWordsAdapter = this;
	}
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View v = mInflater.inflate(R.layout.word_layout, parent, false);

		TextView wordView = (TextView) v.findViewById(R.id.WordTextView);
		wordView.setText(mWords.get(position).getWord());

		wordView = (TextView) v.findViewById(R.id.TranslationTextView);

		String translation = "";
		ArrayList<String> translations = mWords.get(position).getTranslations();
		for (int i=0; i<translations.size(); i++) {
			translation += translations.get(i) + "\n";
		}
		wordView.setText(translation);

		TextView informationView = (TextView) v.findViewById(R.id.item_word_information);
		informationView.setText("Passed " + mWords.get(position).getCountAnswer() + "\\" + mWords.get(position).getCountValidAnswer());


		final int p = position;
		Button btnd = (Button) v.findViewById(R.id.RemoveWordButton);
		btnd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String word = mWords.get(p).getWord();
				DBHelper dbHelper = DBHelper.getInstance(v.getContext());
				Log.e(TAG, "Delete word: " + word);
				dbHelper.deleteWord(word);
				mWords.remove(position);
				mWordsAdapter.notifyDataSetChanged();


			}
		});

		return v;

	}

	@Override
	public boolean areAllItemsEnabled() {
		return true;
	}

	@Override
	public boolean isEnabled(int position) {
		return true;
	}
}
