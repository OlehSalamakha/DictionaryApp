package com.example.olehsalamakha.d;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.TabHost;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import android.widget.TextView;
import android.widget.Toast;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;


public class MainActivity extends Activity {

	static final String TAG = "MainActivity";

	private DataController mDataController = DataController.getInstance(this, this);

	private TabHost mTabhost;
	private ListView mDictionaryListView;

	private int mCountAllWords = 0;
	private boolean mIsDictionaryListViewCreated = false;

	private Button mAddBtn;
	private AutoCompleteTextView mWordEdit;
	private TextView mTranslatedWordView;

	private Test mTest;
	private final Context mContext = this;

	private TextView mWordTestView;
	private RadioButton mRadioButtonTest1;
	private RadioButton mRadioButtonTest2;
	private Button mOkButtonTest;
	private Button mNextbuttonTest;

	private Dialog mAddDialog;
	private Dialog mWordInfromationDialog;


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
			String w = mWordEdit.getText().toString();
			final TextView tView = (TextView) mAddDialog.findViewById(R.id.add_word_text_view);
			final EditText eText = (EditText) mAddDialog.findViewById(R.id.add_word_edit_text);
			tView.setText(w);

			Button translateBtn = (Button) mAddDialog.findViewById(R.id.add_word_translate_button);
			translateBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String translatedWord = translate(tView.getText().toString());
					eText.setText(translatedWord);
				}
			});

			Button addBtn = (Button) mAddDialog.findViewById(R.id.add_word_ok_button);
			addBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Word word = new Word(tView.getText().toString(), eText.getText().toString());
						mDataController.addWordtoDb(word);
						mWordEdit.setAdapter(mDataController.getSearchAdapter());

						if (!mIsDictionaryListViewCreated) {
							mDataController.initializeDictAdapter();
							Log.e(TAG, "create list view");
							createListView();
						}
						mDataController.addWordstoList();
						mAddDialog.dismiss();
				}
			});
			mAddDialog.show();
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
			if (mDictionaryListView.getChildCount() > 0) {
				if (mDictionaryListView.getLastVisiblePosition() == mDictionaryListView.getCount() - 1
						&& mDictionaryListView.getChildAt(mDictionaryListView.getChildCount() - 1).getBottom()
						<= mDictionaryListView.getHeight()) {
					if (mDictionaryListView.getCount() < mCountAllWords) {
						mDataController.addDataToDictAdapter();
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

//			startActivity(new Intent(v.getContext(), TestActivity.class));


		}
	};

	private View.OnClickListener mOkButtonTestListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (!mTest.isTestFinished()) {
				TextView tView = (TextView) findViewById(R.id.status_test_view);
				Question q = mTest.getmCurrentQuestion();
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
						w.setCountAnswer(w.getCountAnswer() + 1);
						w.setCountValidanswer(w.getCountValidAnswer() + 1);
					} else {
						w.setCountAnswer(w.getCountAnswer() + 1);
						tView.setText("false");
					}

					mDataController.updateWord(w);
					mOkButtonTest.setEnabled(false);

				}
			}
		}
	};


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		createTabHost();

		mAddBtn = (Button) findViewById(R.id.add_button);
		mAddBtn.setOnClickListener(mAddBtnClick);

		mWordEdit = (AutoCompleteTextView) findViewById(R.id.word_edit_text);

		mDataController.initializeSearchAdapter();
		mWordEdit.setAdapter(mDataController.getSearchAdapter());


		mWordEdit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//get word in pistion where user clicked
				String word = mDataController.getSearchAdapter().getItem(position);

				Word w = mDataController.getWord(word);

				TextView wordTextView = (TextView) mWordInfromationDialog.findViewById(R.id.word_information_text_view);
				wordTextView.setText(w.getWord());

				TextView translationTextView = (TextView) mWordInfromationDialog.findViewById(R.id.word_translation_information_text_view);
				translationTextView.setText(w.getTranslations().get(0));

				TextView additionalInformationTextView = (TextView) mWordInfromationDialog.findViewById(R.id.word_additional_information_text_view);
				additionalInformationTextView.setText("Passed "+Integer.toString(w.getCountAnswer())+"/"+Integer.toString(w.getCountValidAnswer()));
				mWordInfromationDialog.show();

			}
		});

		mWordTestView = (TextView) findViewById(R.id.test_word_view);

		mRadioButtonTest1 = (RadioButton) findViewById(R.id.variant_test_radio_btn1);
		mRadioButtonTest2 = (RadioButton) findViewById(R.id.variant_test_radio_btn2);

		mOkButtonTest = (Button) findViewById(R.id.confirm_button);
		mOkButtonTest.setOnClickListener(mOkButtonTestListener);
		mNextbuttonTest = (Button) findViewById(R.id.next_button);
		mNextbuttonTest.setOnClickListener(mNextButtonTestListener);

		mTabhost.setOnTabChangedListener(mTabChangeListener);

		createAddDialog();
		createInformationDialog();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return true;
	}


	@Override
	protected void onStart() {
		super.onStart();
		mDataController.refreshDB();


		if (mDataController.getCountAllWords() > 0) {
			mDataController.initializeDictAdapter();
			createListView();
		}
	}

	public void onRadioButtonClicked(View view) {
		// Is the button now checked?
		boolean checked = ((RadioButton) view).isChecked();

		// Check which radio button was clicked
		switch(view.getId()) {
			case R.id.variant_test_radio_btn1:
				if (checked)
					// Pirates are the best

					break;
			case R.id.variant_test_radio_btn2:
				if (checked)
					// Ninjas rule

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

		mDictionaryListView.setAdapter(mDataController.getDictionaryAdapter());
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


				} catch (SAXException e) {

				} catch (IOException e) {
				}
			}
		});

		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {

		}

		return t.getTranslatedWord();
	}


	private void fillTestLayout() {
		Question q = mTest.getmCurrentQuestion();
		if (q != null) {
			mWordTestView.setText(q.getword().getWord());
			Random r = new Random();
			int indexRadionButton = r.nextInt(2);

			switch (indexRadionButton) {
				case 0: mRadioButtonTest1.setText(q.getword().getTranslations().get(0));
					mRadioButtonTest2.setText(q.getVariant());
					break;
				case 1:  mRadioButtonTest1.setText(q.getVariant());
					mRadioButtonTest2.setText(q.getword().getTranslations().get(0));
			}
		}
	}

	private void createAddDialog() {
		mAddDialog = new Dialog(this);
		mAddDialog.setContentView(R.layout.add_dialog_layout);
		mAddDialog.setTitle("Add Word");

	}

	private void createInformationDialog() {
		mWordInfromationDialog = new Dialog(this);
		mWordInfromationDialog.setContentView(R.layout.word_information_dialog_layout);
		mWordInfromationDialog.setTitle("Information");
	}


}
