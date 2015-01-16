package com.example.olehsalamakha.d;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.w3c.dom.Text;


public class WordActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_word);

		String t = getIntent().getStringExtra("value");
		TextView tview = (TextView) findViewById(R.id.word_text_view);
		tview.setText(t);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.menu_word, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
//		if (id == R.id.action_settings) {
//			return true;
//		}

		return super.onOptionsItemSelected(item);
	}
}
