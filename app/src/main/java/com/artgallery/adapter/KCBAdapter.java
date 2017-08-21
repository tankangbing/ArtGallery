package com.artgallery.adapter;

import java.util.ArrayList;
import java.util.List;

import com.artgallery.data.KCBData.Data;
import com.artgallery.wo.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class KCBAdapter extends BaseAdapter{
	
	private List<Data> Monday = new ArrayList<Data>();
	private List<Data> Tuesday = new ArrayList<Data>();
	private List<Data> Wednesday = new ArrayList<Data>();
	private List<Data> Thursday = new ArrayList<Data>();
	private List<Data> Friday = new ArrayList<Data>();
	private Context context;
	

	public KCBAdapter(Context context, List<Data> monday_am, List<Data> tuesday_am,
			List<Data> wednesday_am, List<Data> thursday_am,
			List<Data> friday_am) {
		super();
		this.context = context;
		Monday = monday_am;
		Tuesday = tuesday_am;
		Wednesday = wednesday_am;
		Thursday = thursday_am;
		Friday = friday_am;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		int size = Monday.size();
		if (Tuesday.size() > size) {
			size = Tuesday.size();
		}
		if (Wednesday.size() > size) {
			size = Wednesday.size();
		}
		if (Thursday.size() > size) {
			size = Thursday.size();
		}
		if (Friday.size() > size) {
			size = Friday.size();
		}
		return size;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.kcb_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.kcb_time = (TextView) convertView
					.findViewById(R.id.kcb_time);
			viewHolder.kcb_mon = (TextView) convertView
					.findViewById(R.id.kcb_mon);
			viewHolder.kcb_tue = (TextView) convertView
					.findViewById(R.id.kcb_tue);
			viewHolder.kcb_wed = (TextView) convertView
					.findViewById(R.id.kcb_wed);
			viewHolder.kcb_thu = (TextView) convertView
					.findViewById(R.id.kcb_thu);
			viewHolder.kcb_fri = (TextView) convertView
					.findViewById(R.id.kcb_fri);
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		for (int i = 0; i < Monday.size(); i++) {
			if (Monday.get(i).classNum == position+1) {
				viewHolder.kcb_mon.setText(Monday.get(i).className);
				viewHolder.kcb_time.setText(Monday.get(i).classHour);
				break;
			}
		}
		for (int i = 0; i < Tuesday.size(); i++) {
			if (Tuesday.get(i).classNum == position+1) {
				viewHolder.kcb_tue.setText(Tuesday.get(i).className);
				viewHolder.kcb_time.setText(Tuesday.get(i).classHour);
				break;
			}
		}
		for (int i = 0; i < Wednesday.size(); i++) {
			if (Wednesday.get(i).classNum == position+1) {
				viewHolder.kcb_wed.setText(Wednesday.get(i).className);
				viewHolder.kcb_time.setText(Wednesday.get(i).classHour);
				break;
			}
		}
		for (int i = 0; i < Thursday.size(); i++) {
			if (Thursday.get(i).classNum == position+1) {
				viewHolder.kcb_thu.setText(Thursday.get(i).className);
				viewHolder.kcb_time.setText(Thursday.get(i).classHour);
				break;
			}
		}
		for (int i = 0; i < Friday.size(); i++) {
			if (Friday.get(i).classNum == position+1) {
				viewHolder.kcb_fri.setText(Friday.get(i).className);
				viewHolder.kcb_time.setText(Friday.get(i).classHour);
				break;
			}
		}
		return convertView;
	}
	
	static class ViewHolder {
		public TextView kcb_time;
		public TextView kcb_mon;
		public TextView kcb_tue;
		public TextView kcb_wed;
		public TextView kcb_thu;
		public TextView kcb_fri;
	}

}
