package com.example.olehsalamakha.d;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
	private AlertDialog mDialog;
	private int mPosition;
	public WordsAdapter(Activity activity, ArrayList<Word> words) {
		super(activity, R.layout.word_layout, words);

		mWords = words;
		mInflater = activity.getWindow().getLayoutInflater();
		mWordsAdapter = this;
		createDialog();
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
		mPosition = position;
		Button btnd = (Button) v.findViewById(R.id.RemoveWordButton);
		btnd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mDialog.show();
			}
		});

		return v;

	}

	private void createDialog() {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
		dialogBuilder = new AlertDialog.Builder(getContext());
		dialogBuilder.setMessage("Do you want to delete this word?");
		dialogBuilder.setTitle("Delete word");
		dialogBuilder.setPositiveButton("Ok",  new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {

				String word = mWords.get(mPosition).getWord();
				DBHelper dbHelper = DBHelper.getInstance(getContext());
				Log.e(TAG, "Delete word: " + word);
				dbHelper.deleteWord(word);

				mWords.remove(mPosition);
				mWordsAdapter.notifyDataSetChanged();
			}});


		dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});

		mDialog = dialogBuilder.create();
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
