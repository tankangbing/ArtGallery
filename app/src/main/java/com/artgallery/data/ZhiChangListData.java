package com.artgallery.data;

import java.util.ArrayList;
import java.util.List;

public class ZhiChangListData {
	
	public ZhiChangListData() {
	}

	public List<Data> List_Data = new ArrayList<Data>();
	
	public static final class Data {
		
		public final String title;// 职位标题
		public final String name;// 公司名字
		public final String cover;// 公司logo
		public final String num;//招聘人数
		public final String time;//时间
		public final String pay;//薪资
		public final String id;//id
		
		public Data(String title, String name, String cover, String num,
				String time, String pay, String id) {
			super();
			this.title = title;
			this.name = name;
			this.cover = cover;
			this.num = num;
			this.time = time;
			this.pay = pay;
			this.id = id;
		}
	}
}
