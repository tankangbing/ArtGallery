package com.artgallery.adapter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.aphidmobile.utils.UI;
import com.artgallery.data.ZhiChangListData;
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

public class ZhiChangListAdapter extends BaseAdapter {

	private Context context;

	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options; // DisplayImageOptions是用于设置图片显示的类
	private boolean typeflag;//true是全职，false是兼职
	
	private ZhiChangListData zhiChangListData;


	private int page;

	public ZhiChangListAdapter(Context context, ZhiChangListData zhiChangListData) {
		super();
		this.context = context;
		this.zhiChangListData = zhiChangListData;
		page = 1;
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565).build();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return zhiChangListData.List_Data.size();
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
		if (convertView == null)
		{
			convertView = LayoutInflater.from(context).inflate(
					R.layout.zhichang_list_item, null);
		}
		ZhiChangListData.Data data = zhiChangListData.List_Data
				.get(position);

		UI.<TextView>findViewById(convertView, R.id.zhichang_list_title).setText(data.title);
		UI.<TextView>findViewById(convertView, R.id.zhichang_list_companyname).setText("公司名称：" + data.name);
		UI.<TextView>findViewById(convertView, R.id.zhichang_list_pay).setText("￥" + data.pay);
		UI.<TextView>findViewById(convertView, R.id.zhichang_list_peoplenum).setText("招聘人数：" + data.num);
		UI.<TextView>findViewById(convertView, R.id.zhichang_list_time).setText("发布日期：" + data.time);
		if (typeflag) {
			UI.<TextView>findViewById(convertView, R.id.zhichang_list_paytime).setVisibility(View.VISIBLE);
		}else {	
			UI.<TextView>findViewById(convertView, R.id.zhichang_list_paytime).setVisibility(View.GONE);
		}
		AnimateFirstDisplayListener aDisplayListener = new AnimateFirstDisplayListener();
		imageLoader.displayImage(data.cover,UI.<ImageView> findViewById(convertView, R.id.zhichang_list_img),
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

	public boolean isTypeflag() {
		return typeflag;
	}

	public void setTypeflag(boolean typeflag) {
		this.typeflag = typeflag;
	}

	
}
