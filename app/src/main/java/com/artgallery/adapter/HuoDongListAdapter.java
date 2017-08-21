package com.artgallery.adapter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.artgallery.data.HuoDongListData;
import com.artgallery.tool.AnimateFirstDisplayListener;
import com.artgallery.wo.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HuoDongListAdapter extends BaseAdapter {

	private Context context;

	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options; // DisplayImageOptions是用于设置图片显示的类
	private HuoDongListData huoDongListData;

	private int page;

	public HuoDongListAdapter(Context context, HuoDongListData huoDongListData) {
		super();
		this.context = context;
		this.huoDongListData = huoDongListData;
		page = 1;
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565).build();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return huoDongListData.List_Data.size();
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
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.active_item, null);
		}
		HuoDongListData.Data data = huoDongListData.List_Data.get(position);

		TextView a = (TextView) convertView
				.findViewById(R.id.huodong_list_title);
		a.setText(data.title);
		TextView b = (TextView) convertView
				.findViewById(R.id.huodong_list_unit);
		b.setText("组织单位：" + data.unit);
		TextView c = (TextView) convertView
				.findViewById(R.id.huodong_list_time);
		c.setText("活动开始时间：" + data.dtBegin);
		TextView dd = (TextView) convertView
				.findViewById(R.id.huodong_list_num);
		dd.setText("已报名人数：" + data.registCount);
		
		TextView e = (TextView) convertView.findViewById(R.id.huodong_list_xuefeng);
		
		if(data.credit.equals("0")){
			e.setBackgroundColor(000000);
			e.setText("");
		}else {
			e.setBackgroundResource(R.drawable.xuefeng);
			e.setText(data.credit+"学分");
		}

		AnimateFirstDisplayListener aDisplayListener = new AnimateFirstDisplayListener();
		imageLoader.displayImage(data.cover,
				(ImageView) convertView.findViewById(R.id.huodong_list_img),
				options, aDisplayListener);
		return convertView;
	}

	public int getPage() {
		return page - 1;
	}

	public void getMoreData() {
		// newBooksListData.getMoreData(num);
	}

	public static Bitmap getLoacalBitmap(String url) {
		try {
			FileInputStream fis = new FileInputStream(url);
			return BitmapFactory.decodeStream(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

}
