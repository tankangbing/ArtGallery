package com.artgallery.tool;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PermanentDBHelper extends SQLiteOpenHelper {
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
	
	public PermanentDBHelper(Context context) {
		super(context, "perData.db", null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		this.db = db;
		String sql = "create table permanent(task_id text, index_id integer,"
				+ " image text, link text,"
				+ " CONSTRAINT [pk_last] PRIMARY KEY (task_id,index_id))";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		String sql = "DROP TABLE IF EXISTS permanent";
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
				.query("permanent", need, tiaojian, zhi, null, null, "task_id desc");//"book_id desc"
		return cursor;
	}

	/**
	 * 添加
	 * @param task_id
	 * @param index_id
	 * @param image
	 * @param link
	 * @return
	 */
	public long insert(String task_id, int index_id, String image, String link) {
		SQLiteDatabase db = this.getWritableDatabase();
		/* ContentValues */
		ContentValues cv = new ContentValues();
		cv.put("task_id", task_id);
		cv.put("index_id", index_id);
		cv.put("image", image);
		cv.put("link", link);
		long row = db.insert("permanent", null, cv);
		return row;
	}

	/**
	 * 删除
	 * @param task_id	
	 */	
	public void delete(String task_id) {
		SQLiteDatabase db = this.getWritableDatabase();
		String where = "task_id" + " = ?";
		String[] whereValue = { task_id };
		db.delete("permanent", where, whereValue);
	}
	
	/**
	 * 清空表
	 */	
	public void delete() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete("permanent", null, null);
	}

//	/**
//	 * 修改
//	 * @param id
//	 * @param bookname
//	 * @param author
//	 * @param imgurl
//	 * @param pub
//	 * @param updatetime
//	 * @param pubtime
//	 * @param url
//	 * @param path
//	 * @param dlflag
//	 */
//	public void update(int id, String bookname, String author, String imgurl, String pub, String updatetime,
//			String pubtime, String url, String path, String dlflag, String restartflag, String username) {
//		SQLiteDatabase db = this.getWritableDatabase();
//		String where = "book_id" + " = ?";
//		String[] whereValue = { Integer.toString(id) };
//
//		ContentValues cv = new ContentValues();
//		cv.put("book_name", bookname);
//		cv.put("book_author", author);
//		cv.put("book_imgurl", imgurl);
//		cv.put("book_pub", pub);
//		cv.put("book_updatetime", updatetime);
//		cv.put("book_pubtime", pubtime);
//		cv.put("book_url", url);
//		cv.put("book_path", path);
//		cv.put("book_dlflag", dlflag);
//		cv.put("book_restartflag", restartflag);
//		cv.put("username", username);
//		db.update("permanent", cv, where, whereValue);
//	}
}
