package com.example.olehsalamakha.d;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

//Created by olehsalamakha on 1/21/15.

public class WordsContentProvider extends ContentProvider{

	static final String TAG = "Content provider";
	private static final Uri CONTENT_URI = Uri.parse(
			"content://com/example.olehsalamakha.d/WordsContentProvider"
	);

	private SQLiteDatabase mDb;
	public  boolean onCreate() {
		Log.d(TAG, " begin provider create method");
		DBHelper helper =  DBHelper.getInstance(getContext());
		mDb = helper.getWritableDatabase();
		if (mDb == null) {
			Log.d(TAG, "error");
		}
		else  {
			Log.d(TAG, "googd");
		}
		Log.d(TAG, "end create method in provider");
		return (mDb == null) ? false : true;

	}

	@Override
	public Cursor query(Uri url, String[] columns, String selection,
	                    String[] selectionArgs, String sort) {
		//Cursor c = mDb.query(DBHelper.WORDTABLENAME, columns, selection, selectionArgs, null, null, null);
		Cursor c = mDb.rawQuery("Select Words.word, Translations.translation From Words Join Translations On Words.id=Translations.id_word", null);
		c.setNotificationUri(getContext().getContentResolver(), url);
		return c;
	}

	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
		return 0;
	}

	@Override
	public Uri insert(Uri arg0, ContentValues arg1) {
		return null;
	}

	@Override
	public int update(Uri arg0, ContentValues arg1, String arg2,
	                  String [] arg3) {
		return 0;
	}

	@Override
	public String getType(Uri arg0) {
		return null;
	}



}
