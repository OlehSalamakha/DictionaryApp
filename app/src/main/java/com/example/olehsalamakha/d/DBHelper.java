package com.example.olehsalamakha.d; /**
 * Created by olehsalamakha on 1/14/15.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

public class DBHelper extends  SQLiteOpenHelper {

	public static final String DATABASENAME = "DBWords";
	public static final String WORDTABLENAME = "Words";
	public static final String TRANSLATIONSTABLENAME = "Translations";

	private int mLastSelectedWordId =-1;


	public DBHelper(Context context) {
		super(context, DATABASENAME , null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE Words (id INTEGER PRIMARY KEY AUTOINCREMENT, word text, count_answer integer, count_valid_answer integer);" +
				"create table Translations(id INTEGER PRIMARY KEY AUTOINCREMENT, id_word integer,  translation text);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXIST Words");
		db.execSQL("DROP TABLE IF EXIST  Translations");
		onCreate(db);
	}


	public Boolean insertWord(Word word) {
		String w = word.getWord();
		int countAnswer = word.getCountAnswer();
		int countValidAnswer = word.getCountValidAnswer();

		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();

		contentValues.put("word", w);
		contentValues.put("count_answer", countAnswer);
		contentValues.put("count_valid_answer", countValidAnswer);



		long id = db.insert(WORDTABLENAME, null, contentValues);
		contentValues.clear();
		List<String> translations = word.getTranslations();

		String idWord = Long.toString(id);
		for (int i=0; i<translations.size(); i++) {
			contentValues.put(idWord, translations.get(i));

		}

		db.insert(TRANSLATIONSTABLENAME, null, contentValues);
		return true;
	}

	public int getCountOfWords() {
		String countQuery = "SELECT  * FROM " + WORDTABLENAME;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int count = cursor.getCount();
		cursor.close();
		return count;
	}

//	public List<Word> selectWords(int count) {
//		SQLiteDatabase db = this.getReadableDatabase();
//
//
//	}

	public String select1() {
		SQLiteDatabase db = this.getReadableDatabase();

		String query = "SELECT  * FROM " + WORDTABLENAME;
		Cursor cursor = db.rawQuery(query, null);

		cursor.moveToLast();
		String t=cursor.getString(1);
		return t;
	}







}
