package com.artgallery.data;

import java.util.ArrayList;
import java.util.List;

public class FloorData {
	public List<Data> floor_data = new ArrayList<Data>();
	public static final class Data {
		public final String floorname;
		public final String floorpic;
		public Data(String floorname, String floorpic) {
			super();
			this.floorname = floorname;
			this.floorpic = floorpic;
		}
	}
}
