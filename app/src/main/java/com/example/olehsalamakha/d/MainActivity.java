package com.example.olehsalamakha.d;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TabHost;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.sql.SQLException;
import java.util.ArrayList;



public class MainActivity extends Activity {

	private TabHost mTabhost;
	private ListView mDictionaryListView;
	private WordsAdapter mAdapter;
	private DBHelper mDbHelper;
	private ArrayList<Word> mWords = new ArrayList<Word>();
	private int mCountAllWords = 0;

	private AdapterView.OnItemClickListener mDictionaryItemClickListener =
			new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			view.setSelected(true);

			startActivity(new Intent(parent.getContext(), WordActivity.class));

		}
	};


	private AbsListView.OnScrollListener mDictionaryScrollListener = new AbsListView.OnScrollListener() {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			// not used
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
		                     int visibleItemCount, int totalItemCount) {
			//check if position of listview is in bottom
			if (mDictionaryListView.getLastVisiblePosition() == mDictionaryListView.getCount() - 1
					&& mDictionaryListView.getChildAt(mDictionaryListView.getChildCount() - 1).getBottom()
					<= mDictionaryListView.getHeight()) {
				if (mDictionaryListView.getCount() < mCountAllWords) {
					mAdapter.addAll(mDbHelper.selectWords());
					mAdapter.notifyDataSetChanged();
				}
			}
		}
	};


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		createTabHost();
		mDbHelper = new DBHelper(this);
		mCountAllWords = mDbHelper.getCountOfWords();
		mWords.addAll(mDbHelper.selectWords());
		createListView();
		mDictionaryListView.setOnScrollListener(mDictionaryScrollListener);


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

	private void createListView() {
		mDictionaryListView = (ListView) findViewById(R.id.dictionary_list_view);
		mAdapter = new WordsAdapter(this, mWords);
		mDictionaryListView.setAdapter(mAdapter);
		mDictionaryListView.setOnItemClickListener(mDictionaryItemClickListener);
	}


}
