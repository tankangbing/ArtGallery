package com.artgallery.data;

import java.util.ArrayList;
import java.util.List;

public class DepartmentTypeData {
	public List<Data> List_Data = new ArrayList<Data>();

	public static final class Data {
		public final String de_title;

		public final String de_id;

		public Data(String de_title, String de_id) {
			super();
			this.de_title = de_title;
			this.de_id = de_id;
		}

	}

}
