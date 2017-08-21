package com.artgallery.adapter;

import com.artgallery.data.DepartmentData;
import com.artgallery.wo.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DepartmentListAdapter extends BaseAdapter{
	private Context context;

	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options; // DisplayImageOptions是用于设置图片显示的类
	private DepartmentData departmentData;


	private int page;
	
	public DepartmentListAdapter(Context context, DepartmentData departmentData) {
		super();
		this.context = context;
		this.departmentData = departmentData;
		page = 1;
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565).build();
	}
	
	

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return departmentData.List_Data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.department_item, null);

			viewHolder = new ViewHolder();
			viewHolder.de_name = (TextView) convertView
					.findViewById(R.id.depatrment_item_title);
			viewHolder.de_part = (TextView) convertView
					.findViewById(R.id.depatrment_item_part);
			viewHolder.de_time = (TextView) convertView
					.findViewById(R.id.depatrment_item_time);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		
		
		viewHolder.de_name.setText(departmentData.List_Data.get(position).de_title);
		viewHolder.de_part.setText(departmentData.List_Data.get(position).de_college);
		viewHolder.de_time.setText(departmentData.List_Data.get(position).de_time.substring(0, 10));
		
		
		return convertView;
	}
	
	static class ViewHolder {
		public TextView de_name;
		public TextView de_part;
		public TextView de_time;
	}

}
