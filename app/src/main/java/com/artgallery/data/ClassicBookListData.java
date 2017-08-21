package com.artgallery.data;

import java.util.ArrayList;
import java.util.List;

public class ClassicBookListData {

	public List<Data> IMG_DESCRIPTIONS = new ArrayList<Data>();

	public static final class Data {
		public final String bookid;
		public final String url;
		public final String bookname;
		public final String author;

		public Data(String bookid, String url, String bookname, String author) {
			super();
			this.bookid = bookid;
			this.url = url;
			this.bookname = bookname;
			this.author = author;
		}

	}

}
