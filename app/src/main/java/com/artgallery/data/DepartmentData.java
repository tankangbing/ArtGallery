package com.artgallery.data;

import java.util.ArrayList;
import java.util.List;

public class DepartmentData {
	public List<Data> List_Data = new ArrayList<Data>();

	public static final class Data {
		public final String de_title;
		public final String de_college;
		public final String de_time;
		public final String de_id;
		public final String de_url;

		public Data(String de_title, String de_college, String de_time,
				String de_id, String de_url) {
			super();
			this.de_title = de_title;
			this.de_college = de_college;
			this.de_time = de_time;
			this.de_id = de_id;
			this.de_url = de_url;
		}

	}

}
