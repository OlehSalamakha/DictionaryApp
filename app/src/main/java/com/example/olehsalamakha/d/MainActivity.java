package com.example.olehsalamakha.d;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import android.widget.TextView;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;


public class MainActivity extends Activity {

	private TabHost mTabhost;
	private ListView mDictionaryListView;
	private WordsAdapter mAdapter;
	private DBHelper mDbHelper;
	private ArrayList<Word> mWords = new ArrayList<Word>();
	private int mCountAllWords = 0;

	private Button mTranslateBtn;
	private Button mAddBtn;
	private EditText mWordEdit;
	private TextView mTranslatedWordView;

	private AdapterView.OnItemClickListener mDictionaryItemClickListener =
			new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			view.setSelected(true);

		}
	};

	private View.OnClickListener mTranslateBtnClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			String word = mWordEdit.getText().toString();
			String translatedWord = translate(word);
			mTranslatedWordView.setText(translatedWord);
		}
	};

	private View.OnClickListener mAddBtnClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			try {
				Word w = new Word(mWordEdit.getText().toString(), mTranslatedWordView.getText().toString());
				mDbHelper.insertWord(w);
				mAdapter.insert(w, 0);
				mAdapter.notifyDataSetChanged();
			} catch (SQLException e) {
				e.printStackTrace();
			}
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

		mDbHelper = DBHelper.getInstance(this);
		mCountAllWords = mDbHelper.getCountOfWords();
		if (mCountAllWords > 0) {
			mWords.addAll(mDbHelper.selectWords());
			createListView();
			mDictionaryListView.setOnScrollListener(mDictionaryScrollListener);
		}

		mTranslateBtn = (Button) findViewById(R.id.translate_button);
		mTranslateBtn.setOnClickListener(mTranslateBtnClick);

		mAddBtn = (Button) findViewById(R.id.add_button);
		mAddBtn.setOnClickListener(mAddBtnClick);

		mWordEdit = (EditText) findViewById(R.id.word_edit_text);
		mWordEdit.setText("pen");

		mTranslatedWordView = (TextView) findViewById(R.id.translated_word_view);


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up round_button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
//		if (id == R.id.action_settings) {
//			return true;
//		}

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

	private String translate(final String word) {
		final Translator t = new Translator();

		Thread thread = new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					t.translate(word);
				} catch (ParserConfigurationException e) {
					e.printStackTrace();

				} catch (SAXException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return t.getTranslatedWord();
	}



}
