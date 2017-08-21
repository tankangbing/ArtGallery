package com.artgallery.data;

import java.util.ArrayList;
import java.util.List;

public class HuoDongListData {

	public HuoDongListData() {
	}

	public List<Data> List_Data = new ArrayList<Data>();

	public static final class Data {

		public final String cover;
		public final String title;
		public final String dtBegin;
		public final String unit;
		public final String registCount;
		public final String credit;
		public final String id;

		public Data(String cover, String title, String dtBegin, String unit,
				String registCount, String credit, String id) {
			super();
			this.cover = cover;
			this.title = title;
			this.dtBegin = dtBegin;
			this.unit = unit;
			this.registCount = registCount;
			this.credit = credit;
			this.id = id;
		}

	}
}
