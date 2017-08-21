package com.artgallery.data;

import java.util.ArrayList;
import java.util.List;

public class ContentData {


	public List<Data> IMG_DESCRIPTIONS = new ArrayList<Data>();
	public static final class Data {
		public final String bookname;
		public final String author;
		public final String pub;
		public final String imgurl;
		public final String pubtime;
		public final String intro;
		public final String dir;
		public final String updatetime;
		public final String pdfurl;
		public Data(String bookname, String author, String pub, String imgurl,
				String pubtime, String intro, String dir, String updatetime,
				String pdfurl) {
			super();
			this.bookname = bookname;
			this.author = author;
			this.pub = pub;
			this.imgurl = imgurl;
			this.pubtime = pubtime;
			this.intro = intro;
			this.dir = dir;
			this.updatetime = updatetime;
			this.pdfurl = pdfurl;
		}
		
		
		
	}
}
