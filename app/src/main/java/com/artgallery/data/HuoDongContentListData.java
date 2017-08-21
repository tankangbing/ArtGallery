package com.artgallery.data;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

public class HuoDongContentListData {
	public HuoDongContentListData() {
	}

	public List<Data> List_Data = new ArrayList<Data>();

	public static final class Data {

		public final String name;
		public final String time;
		public final String content;
		public final String lou;
		public final JSONArray imglist;
		public Data(String name, String time, String content,String lou,
				JSONArray imglist) {
			super();
			this.name = name;
			this.time = time;
			this.content = content;
			this.lou = lou;
			this.imglist = imglist;
		}
		


	}
}