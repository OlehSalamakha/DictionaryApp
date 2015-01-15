package com.example.olehsalamakha.d;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;

import java.sql.SQLException;
import java.util.ArrayList;



public class MainActivity extends Activity {

	private TabHost mTabhost;
	private ListView mDictionaryListView;
	private WordsAdapter mAdapter;

	private AdapterView.OnItemClickListener mDictionaryItemClickListener =
			new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			view.setSelected(true);


		}
	};

	private View.OnClickListener mDeleteButtonClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			final int position = mDictionaryListView.getPositionForView((View) v.getParent());
			//datalist.remove(position);
			mAdapter.notifyDataSetChanged();
		}


	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		createTabHost();



		DBHelper db = new DBHelper(this);

//
//
//		Word[] words = new Word[10];
//		for (int i=0; i<10; i++) {
//			ArrayList<String> t = new ArrayList<String>();
//			t.add("adfasdf");
//			words[i] = new Word("adfasf", t);
//
//			try {
//				db.insertWord(words[i]);
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}

//		try {
//			db.insertWord(words[0]);
//		}
//		catch (SQLException e) {
//			startActivity(new Intent(this, TestDbActivity.class));
//			Log.d("exeption", "sql exception");
//		}


		ArrayList<Word> lWord = db.selectWords();
		Word[] words = new Word[lWord.size()];
		words = lWord.toArray(words);


		createListView(words);


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up round_button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}


	private void createTabHost() {
		mTabhost = (TabHost) findViewById(R.id.TabHost01);
		mTabhost.setup();

		TabHost.TabSpec spec = mTabhost.newTabSpec("Dictionary");
		spec.setIndicator("Dictionary");
		spec.setContent(R.id.dict);
		mTabhost.addTab(spec);

		spec = mTabhost.newTabSpec("Test");
		spec.setIndicator("Test");
		spec.setContent(R.id.test);
		mTabhost.addTab(spec);
	}

	private void createListView(Word[] words) {
		mDictionaryListView = (ListView) findViewById(R.id.dictionary_list_view);
		mAdapter = new WordsAdapter(this, words);
		mDictionaryListView.setAdapter(mAdapter);
		mDictionaryListView.setOnItemClickListener(mDictionaryItemClickListener);
	}

}
