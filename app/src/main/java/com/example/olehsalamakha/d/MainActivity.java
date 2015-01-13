package com.example.olehsalamakha.d;

import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;

import org.w3c.dom.Text;


public class MainActivity extends ActionBarActivity {

	private TabHost myTabHost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		myTabHost = (TabHost) findViewById(R.id.TabHost01);

		myTabHost.setup();

		TabHost.TabSpec spec = myTabHost.newTabSpec("Dictionary");
		spec.setIndicator("Dictionary");
		spec.setContent(R.id.dict);

		myTabHost.addTab(spec);


		spec = myTabHost.newTabSpec("Test");
		spec.setIndicator("Test");
		spec.setContent(R.id.test);

		myTabHost.addTab(spec);


		Button b = (Button) findViewById(R.id.but);
		b.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Perform action on click

				TextView vd = (TextView) findViewById(R.id.testView);
				vd.setText("asdfasdf");

			}
		});

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
