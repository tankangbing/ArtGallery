package com.artgallery.tool;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DownDBHelper extends SQLiteOpenHelper {
	private SQLiteDatabase db = null;

	@Override
	public SQLiteDatabase getWritableDatabase() {
		// TODO Auto-generated method stub
		final SQLiteDatabase db;
		if(this.db != null){
			db = this.db;
		} else {
			db = super.getWritableDatabase();
		}
		return db;
	}
	
	public DownDBHelper(Context context) {
		super(context, "downData.db", null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		this.db = db;
		String sql = "create table download(down_name text PRIMARY KEY)";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		String sql = "DROP TABLE IF EXISTS emergency";
		db.execSQL(sql);
		onCreate(db);
	}
	/**
	 * 查询
	 * @param need 需要查询的
	 * @param tiaojian 条件
	 * @param zhi 条件的值
	 * @return
	 */
	public Cursor select(String[] need, String tiaojian, String[] zhi) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db
				.query("download", need, tiaojian, zhi, null, null, "down_name desc");//"down_name desc"
		return cursor;
	}

	/**
	 * 添加
	 * @return
	 */
	public long insert(String down_name) {
		SQLiteDatabase db = this.getWritableDatabase();
		/* ContentValues */
		ContentValues cv = new ContentValues();
		cv.put("down_name", down_name);
		long row = db.insert("download", null, cv);
		return row;
	}

	/**
	 * 删除
	 * @param id	主键ID
	 */	
	public void delete(String id) {
		SQLiteDatabase db = this.getWritableDatabase();
		String where = "down_name" + " = ?";
		String[] whereValue = { id };
		db.delete("download", where, whereValue);
	}
	
	/**
	 * 清空表
	 */	
	public void delete() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete("download", null, null);
	}

	/**
	 * 修改
	 */
	public void update(String keepname, String newname) {
		delete(keepname);
		insert(newname);
	}
}
