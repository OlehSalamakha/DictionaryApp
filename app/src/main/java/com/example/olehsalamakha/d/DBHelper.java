package com.example.olehsalamakha.d; /**
 * Created by olehsalamakha on 1/14/15.
 */

import java.sql.SQLException;
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
import android.util.Log;

import com.google.api.translate.Translate;

public class DBHelper extends  SQLiteOpenHelper {

	static final String TAG = "DBhelper";
	public static final String DATABASENAME = "DBWords";
	public static final String WORDTABLENAME = "Words";
	public static final String TRANSLATIONSTABLENAME = "Translations";

	private int mCountSelectedWords = 0;
	private static DBHelper mDbHelper = null;
	public static DBHelper getInstance(Context context) {
		if (mDbHelper == null) {
			mDbHelper = new DBHelper(context);
		}

		return mDbHelper;
	}


	private DBHelper(Context context) {
		super(context, DATABASENAME , null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE Words (id INTEGER PRIMARY KEY AUTOINCREMENT, word text UNIQUE, count_answer integer, count_valid_answer integer);");
		db.execSQL("create table Translations (id INTEGER PRIMARY KEY AUTOINCREMENT, id_word integer,  translation text);");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXIST Words");
		db.execSQL("DROP TABLE IF EXIST  Translations");
		onCreate(db);
	}

	public Boolean deleteWord(String word) {
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery("Select id FROM " + WORDTABLENAME + " WHERE word='"+word+"'", null);
		cursor.moveToFirst();

		int id = cursor.getInt(0);

//		db.execSQL("delete * FROM " + WORDTABLENAME + " Where word='" + word+"';");
		mCountSelectedWords -=1;
		db.delete(TRANSLATIONSTABLENAME, "id_word='"+Integer.toString(id)+"'", null);
		db.delete(WORDTABLENAME, "word='" + word+"'", null);

		return true;
	}
	public Boolean insertWord(Word word) throws SQLException {
		String w = word.getWord();
		int countAnswer = word.getCountAnswer();
		int countValidAnswer = word.getCountValidAnswer();

		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();

		contentValues.put("word", w);
		contentValues.put("count_answer", countAnswer);
		contentValues.put("count_valid_answer", countValidAnswer);



		long id = db.insert(WORDTABLENAME, null, contentValues);
		if (id == -1) {
			throw new SQLException("Error insert into words");
		}
		contentValues.clear();
		List<String> translations = word.getTranslations();

		String idWord = Long.toString(id);
		for (int i=0; i<translations.size(); i++) {
			contentValues.put("id_word", idWord);
			contentValues.put("translation", translations.get(i));
			id = db.insert(TRANSLATIONSTABLENAME, null, contentValues);
			if (id == -1) {
				throw new SQLException("Error insert intro translations");
			}
			contentValues.clear();
		}
		return true;
	}


	public int getCountOfWords() {
		String countQuery = "SELECT COUNT(*) FROM " + WORDTABLENAME;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.moveToFirst();
		int count = cursor.getInt(0);
		cursor.close();
		return count;
	}

	public ArrayList<Word> selectWords() {
		int count = 10;

		String query = "SELECT * FROM " + WORDTABLENAME + " LIMIT " + Integer.toString(count) + " OFFSET " + Integer.toString(mCountSelectedWords);
		Cursor cursor = select(query);
		ArrayList<Word> words = new ArrayList<Word>();

		while (cursor.moveToNext()) {
			int idWord = cursor.getInt(0);
			String word =cursor.getString(1);
			int countAnswer = cursor.getInt(2);
			int countValidAnswer = cursor.getInt(3);
			ArrayList<String> translations = selectTranslations(idWord);

			words.add(new Word(word, translations, countAnswer, countValidAnswer));
		}
		mCountSelectedWords += cursor.getCount();

		String wd = "";
		for (int i=0; i<words.size(); i++) {
			wd+= words.get(i).getWord();
		}
		Log.e(TAG, "DBHELPER offset: " + Integer.toString(mCountSelectedWords));
		Log.e(TAG, "DBhelper select: " + wd);
		return words;
	}

	private static ArrayList<String> selectTranslations(int idWord) {
		String query = "SELECT * FROM " + TRANSLATIONSTABLENAME + " WHERE " + "id_word=" + Integer.toString(idWord);
		Cursor cursor = select(query);

		ArrayList<String> translations = new ArrayList<String>();
		while (cursor.moveToNext()) {
			translations.add(cursor.getString(2));
		}
		return translations;
	}


	private static Cursor select(String query) {
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(query, null);
		return cursor;
	}

}
