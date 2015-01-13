package com.example.olehsalamakha.d;

import android.app.Activity;
import android.provider.UserDictionary;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.olehsalamakha.d.R;

import org.w3c.dom.Text;

/**
 * Created by olehsalamakha on 1/13/15.
 */
public class WordsAdapter extends ArrayAdapter {

	public WordsAdapter(Activity activity, Word[] words) {
		super(activity, R.layout.word_layout, words);
		words_ = words;
		inflater_ = activity.getWindow().getLayoutInflater();
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = inflater_.inflate(R.layout.word_layout, parent, false);
		TextView wordView = (TextView) v.findViewById(R.id.WordTextView);
		wordView.setText("blabla");
		return v;
	}

	private LayoutInflater inflater_;
	private Word[] words_;
}
