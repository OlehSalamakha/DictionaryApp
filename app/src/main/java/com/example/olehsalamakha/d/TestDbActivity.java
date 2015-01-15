package com.example.olehsalamakha.d;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class TestDbActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_db);

		final DBHelper db = new DBHelper(this);

		String word = "pen";
		ArrayList<String> trans = new ArrayList<String>();
		trans.add("Ручка");
		db.insertWord(new Word(word, trans));

		Word w;


		Button btn = (Button) findViewById(R.id.testbtn);
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				TextView t = (TextView) findViewById(R.id.textvvv);
				t.setText(db.select1());
			}
		});


		btn = (Button) findViewById(R.id.insertbtn);
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String word = "pdffdfdfdf";
				ArrayList<String> trans = new ArrayList<String>();
				trans.add("Ручка");
				db.insertWord(new Word(word, trans));

			}
		});


		btn = (Button) findViewById(R.id.countbtn);
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int n = db.getCountOfWords();
				TextView t = (TextView) findViewById(R.id.textvvv);
				t.setText(Integer.toString(n));
			}
		});
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_test_db, menu);
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
}
