package com.artgallery.data;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

public class RemenContent {

	private Context context;
	
	public  RemenContent(Context context) {
		
	}

	public List<Data> IMG_DESCRIPTIONS = new ArrayList<Data>();
	
	public static final class Data {


		public final String name;
		public final String thief;
		public final String imgurl;
		public final String id;
		public final String info;
		public final String chapters;
		public Data(String name, String thief, String imgurl, String id,
				String info, String chapters) {
			super();
			this.name = name;
			this.thief = thief;
			this.imgurl = imgurl;
			this.id = id;
			this.info = info;
			this.chapters = chapters;
		}
		
		
		
		
	}
}
