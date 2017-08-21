package com.artgallery.data;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

public class RemenData {

	private Context context;

	public RemenData(Context context) {

	}

	public List<Data> IMG_DESCRIPTIONS = new ArrayList<Data>();

	public static final class Data {

		public final String vname;
		public final String vauthor;
		public final String vurl;
		public final String vid;
		

		public Data(String vname, String vauthor, String vurl,String vid) {
			super();
			this.vname = vname;
			this.vauthor = vauthor;
			this.vurl = vurl;
			this.vid = vid;
		}

	}
}
