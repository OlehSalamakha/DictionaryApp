package com.example.olehsalamakha.d;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import junit.framework.Test;


import static android.support.v4.content.ContextCompat.startActivities;


/**
 * Created by olehsalamakha on 1/13/15.
 */
public class WordsAdapter extends ArrayAdapter {

	private LayoutInflater mInflater;
	private Word[] mWords;
	public WordsAdapter(Activity activity, Word[] words) {
		super(activity, R.layout.word_layout, words);
		mWords = words;
		mInflater = activity.getWindow().getLayoutInflater();
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = mInflater.inflate(R.layout.word_layout, parent, false);
		TextView wordView = (TextView) v.findViewById(R.id.WordTextView);
		wordView.setText("blabla");
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
