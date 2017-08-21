package com.artgallery.tool;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EmergencyDBHelper extends SQLiteOpenHelper {
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
	
	public EmergencyDBHelper(Context context) {
		super(context, "emData.db", null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		this.db = db;
		String sql = "create table emergency(task_id text PRIMARY KEY, content text)";
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
				.query("emergency", need, tiaojian, zhi, null, null, "task_id desc");//"book_id desc"
		return cursor;
	}

	/**
	 * 添加
	 * @param bookname 	书名
	 * @param author	作者
	 * @return
	 */
	public long insert(String task_id, String content) {
		SQLiteDatabase db = this.getWritableDatabase();
		/* ContentValues */
		ContentValues cv = new ContentValues();
		cv.put("task_id", task_id);
		cv.put("content", content);
		long row = db.insert("emergency", null, cv);
		return row;
	}

	/**
	 * 删除
	 * @param id	图书的主键ID
	 */	
	public void delete(String id) {
		SQLiteDatabase db = this.getWritableDatabase();
		String where = "task_id" + " = ?";
		String[] whereValue = { id };
		db.delete("emergency", where, whereValue);
	}
	
	/**
	 * 清空表
	 */	
	public void delete() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete("emergency", null, null);
	}

	/**
	 * 修改
	 * @param id
	 * @param bookname
	 */
	public void update(String id, String content) {
		SQLiteDatabase db = this.getWritableDatabase();
		String where = "task_id" + " = ?";
		String[] whereValue = { id };

		ContentValues cv = new ContentValues();
		cv.put("content", content);
		db.update("emergency", cv, where, whereValue);
	}
}
