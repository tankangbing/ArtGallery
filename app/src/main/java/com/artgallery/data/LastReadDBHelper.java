package com.artgallery.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LastReadDBHelper extends SQLiteOpenHelper{
	private SQLiteDatabase db = null;
	
	public LastReadDBHelper(Context context) {
		super(context, "lastReading.db", null, 1);
		// TODO Auto-generated constructor stub
	}
	
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

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		this.db = db;
		String sql = "create table lastReading(last_id integer, book_name text, book_path text, book_imgurl text, username text,"
				+ " CONSTRAINT [pk_last] PRIMARY KEY (last_id,username))";
		db.execSQL(sql);
		System.out.println("建立lastReading");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		String sql = "DROP TABLE IF EXISTS lastReading";
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
				.query("lastReading", need, tiaojian, zhi, null, null, null);
		return cursor;
	}

	/**
	 * 添加
	 * @param bookname 	书名
	 * @return
	 */
	public long insert(int id, String bookname, String img, String pdf, String username) {
		SQLiteDatabase db = this.getWritableDatabase();
		/* ContentValues */
		ContentValues cv = new ContentValues();
		cv.put("last_id", id);
		cv.put("book_name", bookname);
		cv.put("book_path", pdf);
		cv.put("book_imgurl", img);
		cv.put("username", username);
		long row = db.insert("lastReading", null, cv);
		return row;
	}

	/**
	 * 删除
	 * @param id	图书的主键ID
	 */
	public void delete(int id ,String username) {
		SQLiteDatabase db = this.getWritableDatabase();
		String where = "last_id=? and username=?";
		String[] whereValue = { Integer.toString(id), username };
		db.delete("lastReading", where, whereValue);
	}

	/**
	 * 修改
	 * @param id
	 * @param bookname
	 */
	public void update(int id, String bookname, String img, String pdf, String username) {
		if(id == 1){
			Cursor item1 = select(new String[]{"book_name", "book_imgurl", "book_path"}, "last_id=? and username=?", new String[]{"1", username});
			Cursor item2 = select(new String[]{"book_name", "book_imgurl", "book_path"}, "last_id=? and username=?", new String[]{"2", username});
			Cursor item3 = select(new String[]{"book_name", "book_imgurl", "book_path"}, "last_id=? and username=?", new String[]{"3", username});
			item1.moveToNext();
			item2.moveToNext();
			item3.moveToNext();
			String name1 = item1.getString(item1.getColumnIndex("book_name"));
			String img1 = item1.getString(item1.getColumnIndex("book_imgurl"));
			String pdf1 = item1.getString(item1.getColumnIndex("book_path"));
			
			String name2 = item2.getString(item2.getColumnIndex("book_name"));
			String img2 = item2.getString(item2.getColumnIndex("book_imgurl"));
			String pdf2 = item2.getString(item2.getColumnIndex("book_path"));
			
			String name3 = item3.getString(item3.getColumnIndex("book_name"));
			String img3 = item3.getString(item3.getColumnIndex("book_imgurl"));
			String pdf3 = item3.getString(item3.getColumnIndex("book_path"));
			
			if (bookname.equals(name2)) {
				update(2, name1, img1, pdf1, username);
			}
			if (bookname.equals(name3)) {
				update(2, name1, img1, pdf1, username);
				update(3, name2, img2, pdf2, username);	
			}
			if (!bookname.equals(name1) && !bookname.equals(name2) && !bookname.equals(name3)){
				update(2, name1, img1, pdf1, username);
				update(3, name2, img2, pdf2, username);
			}
			
		}
		 
		SQLiteDatabase db = this.getWritableDatabase();
		String where = "last_id" + " = ? and username=?";
		String[] whereValue = { Integer.toString(id) , username};
		
		ContentValues cv = new ContentValues();
		cv.put("book_name", bookname);
		cv.put("book_imgurl", img);
		cv.put("book_path", pdf);
		db.update("lastReading", cv, where, whereValue);
	}
}
