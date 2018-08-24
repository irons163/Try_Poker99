package com.example.try_poker99;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class Data {
	private DB helper;

	public interface Table {
		String TABLE_NAME = "record";
		String NAME = "name";
		String WINTIMES = "win_times";
		String BEST_TIME = "best_time";
	}

	Data(PokerGameTest L) {
		helper = new DB(L);
	}
	
	public static class DB extends SQLiteOpenHelper {
		private static final String DBNAME = "DB";
		private static final int DBVERSION = 1;

		// 建立資料庫
		public DB(Context context) {
			super(context, DBNAME, null, DBVERSION);

		}

		// 建立TABLE
		@Override
		public void onCreate(SQLiteDatabase db) {
			String sql = "CREATE TABLE " + Table.TABLE_NAME + "(" + Table.NAME
					+ " primary key NOT NULL ," + Table.WINTIMES
					+ " NOT NULL, " + Table.BEST_TIME + " INT NOT NULL ); ";
			db.execSQL(sql);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		}
	}
	

	public String[][] read() {
		final SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.query(Table.TABLE_NAME, null, null, null, null, null,
				Table.BEST_TIME, "5");// 按照最佳紀錄，抓出前5名
		c.moveToFirst();
		String[][] list = new String[c.getCount()][c.getColumnCount()];

		for (int i = 0; i < c.getCount(); i++) {
			for (int j = 0; j < c.getColumnCount(); j++) {
				list[i][j] = c.getString(j);
			}
			c.moveToNext();
		}
		c.close();
		return list;
	}

	public String[] find(String arg0) {
		final SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.query(Table.TABLE_NAME, null, "name='" + arg0 + "'",
				null, null, null, null);
		c.moveToFirst();
		String[] list;
		if (c.getCount() != 0) {
			list = new String[c.getColumnCount()];
			for (int i = 0; i < list.length; i++) {
				list[i] = c.getString(i);
			}
		} else {
			list = null;
		}
		c.close();
		return list;
	}

	public void add(String name, String sec) {
		ContentValues values = new ContentValues();
		SQLiteDatabase db = helper.getWritableDatabase();
		String[] c = find(name);
		if (c == null) {
			values.put(Table.NAME, name);
			values.put(Table.WINTIMES, "1");
			values.put(Table.BEST_TIME, sec);
			db.insert(Table.TABLE_NAME, null, values);
		} else {
			values.put(Table.NAME, name);
			Integer winTimes = Integer.valueOf(Integer.parseInt(c[1]));
			winTimes++;
			sec = (Long.parseLong(sec) < Long.parseLong(c[2])) ? sec : c[2];
			values.put(Table.WINTIMES, winTimes);
			values.put(Table.BEST_TIME, sec);
			db.replace(Table.TABLE_NAME, "name='" + name + "'", values);
		}
		db.close();
	}

	public void delete() {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete(Table.TABLE_NAME, null, null);
		db.close();
	}
}
