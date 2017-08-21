package com.artgallery.tool;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FullscreenDBHelper extends SQLiteOpenHelper {
	private SQLiteDatabase db = null;

	@Override
	public SQLiteDatabase getWritableDatabase() {
		// TODO Auto-generated method stub
		final SQLiteDatabase db;
		if (this.db != null) {
			db = this.db;
		} else {
			db = super.getWritableDatabase();
		}
		return db;
	}

	public FullscreenDBHelper(Context context) {
		super(context, "fullData.db", null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		this.db = db;
		String sql = "create table fullscreen(task_id text, material_id integer,"
				+ " model text, duration text, content_id integer, type text,"
				+ " count text, areaid text, src_id integer, src text, dlpath text, link text,"
				+ " CONSTRAINT [pk_last] PRIMARY KEY (task_id,material_id,content_id,src_id))";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		String sql = "DROP TABLE IF EXISTS fullscreen";
		db.execSQL(sql);
		onCreate(db);
	}

	/**
	 * 查询
	 * 
	 * @param need
	 *            需要查询的
	 * @param tiaojian
	 *            条件
	 * @param zhi
	 *            条件的值
	 * @return
	 */
	public Cursor select(String[] need, String tiaojian, String[] zhi) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query("fullscreen", need, tiaojian, zhi, null, null,
				null);// "book_id desc"
		return cursor;
	}

	/**
	 * 添加
	 * 
	 * @param task_id
	 * @param material_id
	 * @param model
	 * @param duration
	 * @param content_id
	 * @param type
	 * @param count
	 * @param areaid
	 * @param src_id
	 * @param src
	 * @param dlpath
	 * @param link
	 * @return
	 */
	public long insert(String task_id, int material_id, String model,
			String duration, int content_id, String type, String count,
			String areaid, int src_id, String src, String dlpath, String link) {
		SQLiteDatabase db = this.getWritableDatabase();
		/* ContentValues */
		ContentValues cv = new ContentValues();
		cv.put("task_id", task_id);
		cv.put("material_id", material_id);
		cv.put("model", model);
		cv.put("duration", duration);
		cv.put("content_id", content_id);
		cv.put("type", type);
		cv.put("count", count);
		cv.put("areaid", areaid);
		cv.put("src_id", src_id);
		cv.put("src", src);
		cv.put("dlpath", dlpath);
		cv.put("link", link);
		long row = db.insert("fullscreen", null, cv);
		return row;
	}

	/**
	 * 删除
	 * 
	 * @param task_id
	 */
	public void delete(String task_id) {
		SQLiteDatabase db = this.getWritableDatabase();
		String where = "task_id" + " = ?";
		String[] whereValue = { task_id };
		db.delete("fullscreen", where, whereValue);
	}

	/**
	 * 清空表
	 */
	public void delete() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete("fullscreen", null, null);
	}

	/**
	 * 修改
	 * 
	 * @param id
	 * @param bookname
	 * @param author
	 * @param imgurl
	 * @param pub
	 * @param updatetime
	 * @param pubtime
	 * @param url
	 * @param path
	 * @param dlflag
	 */
	public void update(String src, String dlpath) {
		SQLiteDatabase db = this.getWritableDatabase();
		String where = "src" + " = ?";
		String[] whereValue = { src };

		ContentValues cv = new ContentValues();
		cv.put("dlpath", dlpath);
		db.update("fullscreen", cv, where, whereValue);
	}
}
