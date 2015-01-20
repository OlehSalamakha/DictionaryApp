package com.example.olehsalamakha.d;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TabHost;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import android.widget.TextView;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;


public class MainActivity extends Activity {

	static final String TAG = "MainActivity";

	private  boolean mDeleteWord = false;

	private TabHost mTabhost;
	private ListView mDictionaryListView;
	private WordsAdapter mAdapter;
	private DBHelper mDbHelper;
	private ArrayList<Word> mWords = new ArrayList<Word>();
	private int mCountAllWords = 0;
	private boolean mIsDictionaryListViewCreated = false;

	private Button mTranslateBtn;
	private Button mAddBtn;
	private EditText mWordEdit;
	private TextView mTranslatedWordView;

	private Test mTest;
	private final Context mContext = this;

	private TextView mWordTestView;
	private RadioButton mRadioButtonTest1;
	private RadioButton mRadioButtonTest2;
	private Button mOkButtonTest;
	private Button mNextbuttonTest;



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

				if (!mIsDictionaryListViewCreated) {
					mWords = mDbHelper.selectWords();
					//Log.e(TAG, "Create list view in add button click");
					createListView();
				}

				mWords.addAll(mDbHelper.selectWords());
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
			if (mDictionaryListView.getChildCount() > 0) {
				if (mDictionaryListView.getLastVisiblePosition() == mDictionaryListView.getCount() - 1
						&& mDictionaryListView.getChildAt(mDictionaryListView.getChildCount() - 1).getBottom()
						<= mDictionaryListView.getHeight()) {
					if (mDictionaryListView.getCount() < mCountAllWords) {
						mAdapter.addAll(mDbHelper.selectWords());
						mAdapter.notifyDataSetChanged();
					}
				}
			}
		}
	};

	private View.OnClickListener mCreateTestClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			TestBuilder.createTest(v.getContext());
		}
	};

	private TabHost.OnTabChangeListener mTabChangeListener = new TabHost.OnTabChangeListener() {
		@Override
		public void onTabChanged(String tabId) {
			if (tabId.equals("Test")) {
				mTest = TestBuilder.createTest(mContext);
				fillTestLayout();

			}
		}
	};

	private View.OnClickListener mNextButtonTestListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mTest.goToNextQuestion();
			mOkButtonTest.setEnabled(true);
			fillTestLayout();


		}
	};

	private View.OnClickListener mOkButtonTestListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			TextView tView = (TextView) findViewById(R.id.status_test_view);
			Question q = mTest.getCurrentQuestion();
			Word w = q.getword();

			if (q != null) {
				String answer = "";
				if (mRadioButtonTest2.isChecked()) {
					answer = mRadioButtonTest2.getText().toString();
				} else {
					answer = mRadioButtonTest1.getText().toString();
				}
				if (q.checkQuestion(answer)) {
					tView.setText("true");
					w.setCountAnswer(w.getCountAnswer()+1);
					w.setCountValidanswer(w.getCountValidAnswer()+1);


				} else {
					w.setCountAnswer(w.getCountAnswer()+1);
					tView.setText("false");
				}

				for (int i=0; i<mWords.size(); i++) {
					if (w.getWord().equals(mWords.get(i).getWord())) {
						mWords.get(i).setCountValidanswer(w.getCountValidAnswer());
						mWords.get(i).setCountAnswer(w.getCountAnswer());
						mAdapter.notifyDataSetChanged();
						break;
					}
				}

				mDbHelper.update(w);
				mOkButtonTest.setEnabled(false);
				fillTestLayout();
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
		Log.e(TAG, Integer.toString(mCountAllWords) + " Count of words when created");

		if (mCountAllWords > 0) {
			mWords.addAll(mDbHelper.selectWords());
			createListView();
		}

		mTranslateBtn = (Button) findViewById(R.id.translate_button);
		mTranslateBtn.setOnClickListener(mTranslateBtnClick);

		mAddBtn = (Button) findViewById(R.id.add_button);
		mAddBtn.setOnClickListener(mAddBtnClick);

		mWordEdit = (EditText) findViewById(R.id.word_edit_text);

		Button testButton = (Button) findViewById(R.id.create_test);
		testButton.setOnClickListener(mCreateTestClick);

		mTranslatedWordView = (TextView) findViewById(R.id.translated_word_view);
		mWordTestView = (TextView) findViewById(R.id.test_word_view);

		mRadioButtonTest1 = (RadioButton) findViewById(R.id.variant_test_radio_btn1);
		mRadioButtonTest2 = (RadioButton) findViewById(R.id.variant_test_radio_btn2);

		mOkButtonTest = (Button) findViewById(R.id.confirm_button);
		mOkButtonTest.setOnClickListener(mOkButtonTestListener);
		mNextbuttonTest = (Button) findViewById(R.id.next_button);
		mNextbuttonTest.setOnClickListener(mNextButtonTestListener);

		mTabhost.setOnTabChangedListener(mTabChangeListener);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return true;
	}



	public void onRadioButtonClicked(View view) {
		// Is the button now checked?
		boolean checked = ((RadioButton) view).isChecked();

		// Check which radio button was clicked
		switch(view.getId()) {
			case R.id.variant_test_radio_btn1:
				if (checked)
					// Pirates are the best
					Log.e(TAG, "button1 clicked");
					break;
			case R.id.variant_test_radio_btn2:
				if (checked)
					// Ninjas rule
					Log.e(TAG, "button2 clicked");
					break;
		}
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
		mDictionaryListView.setOnScrollListener(mDictionaryScrollListener);
		mIsDictionaryListViewCreated=true;
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


	private void fillTestLayout() {
		Question q = mTest.getCurrentQuestion();
		if (q != null) {
			mWordTestView.setText(q.getword().getWord());
			Random r = new Random();
			int indexRadionButton = r.nextInt(1);

			switch (indexRadionButton) {
				case 0: mRadioButtonTest1.setText(q.getword().getTranslations().get(0));
					mRadioButtonTest2.setText(q.getVariant());
					break;
				case 1:  mRadioButtonTest1.setText(q.getVariant());
					mRadioButtonTest2.setText(q.getword().getWord());
			}
		}
	}


}
