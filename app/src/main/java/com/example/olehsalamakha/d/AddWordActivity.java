package com.example.olehsalamakha.d;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.sql.SQLException;

import javax.xml.parsers.ParserConfigurationException;


public class AddWordActivity extends ActionBarActivity {

	private TextView mWordTextView;
	private TextView mTranslateTextView;
	private Button mAddButton;
	private View.OnClickListener mAddListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(v.getContext(), MainActivity.class);
			DBHelper dbHelper = DBHelper.getInstance(v.getContext());

			Word w = new Word(mWordTextView.getText().toString(), mTranslateTextView.getText().toString());
			try {
				dbHelper.insertWord(w);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			startActivity(intent);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_word);

		Intent i = getIntent();
		final String word = i.getStringExtra("word");
		mWordTextView = (TextView) findViewById(R.id.added_word);
		mWordTextView.setText(word);

		String translatedWord = translate(word);
		mTranslateTextView = (TextView) findViewById(R.id.translation_word);
		mTranslateTextView.setText(translatedWord);

		mAddButton = (Button) findViewById(R.id.success_add_button);
		mAddButton.setOnClickListener(mAddListener);


	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_add_word, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
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
