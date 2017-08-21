package com.artgallery.data;

import java.util.ArrayList;
import java.util.List;


public class KCBData {
	public List<Data> Monday_am = new ArrayList<Data>();
	public List<Data> Tuesday_am = new ArrayList<Data>();
	public List<Data> Wednesday_am = new ArrayList<Data>();
	public List<Data> Thursday_am = new ArrayList<Data>();
	public List<Data> Friday_am = new ArrayList<Data>();
	public List<Data> Monday_pm = new ArrayList<Data>();
	public List<Data> Tuesday_pm = new ArrayList<Data>();
	public List<Data> Wednesday_pm = new ArrayList<Data>();
	public List<Data> Thursday_pm = new ArrayList<Data>();
	public List<Data> Friday_pm = new ArrayList<Data>();
	
	public static final class Data {
		public final String className;
		public final int classNum;//第几节课
		public final String dayOfTime;//上午还是下午
		public final String classHour;
		public Data(String className, int classNum, String dayOfTime,
				String classHour) {
			super();
			this.className = className;
			this.classNum = classNum;
			this.dayOfTime = dayOfTime;
			this.classHour = classHour;
		}
		
	}
}
