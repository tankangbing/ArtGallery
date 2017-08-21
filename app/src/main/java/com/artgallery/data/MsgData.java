package com.artgallery.data;

import java.util.ArrayList;
import java.util.List;


public class MsgData {

	public List<Data> msgdata = new ArrayList<Data>();

	public static final class Data {
		public final String bookid;
		public final String url;

		public Data(String bookid, String url) {
			super();
			this.bookid = bookid;
			this.url = url;
		}
}
}
