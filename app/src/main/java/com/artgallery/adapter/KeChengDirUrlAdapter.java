package com.artgallery.adapter;

import java.util.ArrayList;
import java.util.List;


import com.artgallery.activity.VideoWeb;
import com.artgallery.data.UrlData;
import com.artgallery.wo.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class KeChengDirUrlAdapter extends BaseExpandableListAdapter {
	private ArrayList<String> father;
	private List<List<UrlData>> chilerd = new ArrayList<List<UrlData>>();
	private Context context;
	private String listurl;
	private String idString;

	public KeChengDirUrlAdapter(Context context, ArrayList<String> fatherList,
			List<List<UrlData>> childList, String listurl, String idString) {
		// TODO Auto-generated constructor stub
		this.father = fatherList;
		this.chilerd = childList;
		this.context = context;
		this.listurl = listurl;
		this.idString = idString;
	}

	 @Override
		public Object getChild(int groupPosition, int childPosition) {
			return chilerd.get(groupPosition).get(childPosition);   //获取父类下面的每一个子类项
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;  //子类位置
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) { //显示子类数据的view
			View view = null;
			view = LayoutInflater.from(context).inflate(
					R.layout.kc_contenturl, null);
			TextView textView = (TextView) view
					.findViewById(R.id.kccontenturl_name);
			textView.setText(chilerd.get(groupPosition).get(childPosition).name);
			final String url = chilerd.get(groupPosition).get(childPosition).url;
			textView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Bundle bundle = new Bundle();
					Intent intent = new Intent();
					
					bundle.putString("url", url);
					bundle.putString("listurl", listurl);
					bundle.putString("id", idString);
					intent.putExtras(bundle);
					intent.setClass(context, VideoWeb.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					System.out.println("web start");
					context.startActivity(intent);
					//直接调用浏览器
//					Uri uri = Uri.parse(url);
//					Intent intent = new Intent(Intent.ACTION_VIEW,uri);
//					context.startActivity(intent);
				}
			});
			return view;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return chilerd.get(groupPosition).size();  //子类item的总数
		}

		@Override
		public Object getGroup(int groupPosition) {   //父类数据
			return father.get(groupPosition);
		}

		@Override
		public int getGroupCount() {
			return father.size();  ////父类item总数
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;   //父类位置
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			View view = LayoutInflater.from(context).inflate(
					R.layout.kc_contenturl, null);
			TextView textView = (TextView) view.findViewById(R.id.kccontenturl_name);
			textView.setText(father.get(groupPosition));
			return view;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {  //点击子类触发事件
			return true;
		}

}
