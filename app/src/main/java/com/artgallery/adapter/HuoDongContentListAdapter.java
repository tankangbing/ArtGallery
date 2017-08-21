package com.artgallery.adapter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.json.JSONException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.artgallery.data.HuoDongContentListData;
import com.artgallery.tool.AnimateFirstDisplayListener;
import com.artgallery.wo.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class HuoDongContentListAdapter extends BaseAdapter {
	private Context context;

	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options; // DisplayImageOptions是用于设置图片显示的类
	private HuoDongContentListData huoDongContentListData;

	private int page;

	private TextView name;
	private TextView time;
	private TextView thecontent;
	private TextView lou;

	public HuoDongContentListAdapter(Context context,
			HuoDongContentListData huoDongContentListData) {
		super();
		this.context = context;
		this.huoDongContentListData = huoDongContentListData;
		page = 1;
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565).build();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return huoDongContentListData.List_Data.size();
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
					R.layout.active_content_item, null);
		}
		HuoDongContentListData.Data data = huoDongContentListData.List_Data
				.get(position);

		name = (TextView) convertView.findViewById(R.id.act_username);
		name.setText(data.name);
		time = (TextView) convertView.findViewById(R.id.act_time);
		time.setText(data.time);
		thecontent = (TextView) convertView.findViewById(R.id.act_content);
		thecontent.setText(data.content);
		lou = (TextView) convertView.findViewById(R.id.lou);
		lou.setText((position+1)+"楼");

		AnimateFirstDisplayListener aDisplayListener = new AnimateFirstDisplayListener();

		try {

			if (data.imglist.length() > 0) {
				for (int i = 0; i < data.imglist.length(); i++) {
					convertView.findViewById(R.id.act_cimg1 + i).setVisibility(
							View.VISIBLE);
					imageLoader.displayImage(
							data.imglist.getString(i),
							(ImageView) convertView.findViewById(R.id.act_cimg1
									+ i), options, aDisplayListener);
				}
			} else {
				for (int j = 0; j < 5; j++)
					convertView.findViewById(R.id.act_cimg1 + j).setVisibility(
							View.GONE);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
