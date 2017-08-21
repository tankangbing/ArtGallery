package com.artgallery.adapter;

import com.artgallery.data.RemenData;
import com.artgallery.tool.AnimateFirstDisplayListener;
import com.artgallery.wo.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class VideoAdapter extends BaseAdapter {
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private AnimateFirstDisplayListener aDisplayListener = new AnimateFirstDisplayListener();
	private Context context;
	private RemenData data;

	public VideoAdapter(Context context, RemenData data) {
		this.data = data;
		this.context = context;
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565).build();
		imageLoader = ImageLoader.getInstance();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.IMG_DESCRIPTIONS.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.IMG_DESCRIPTIONS.get(position);
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
					R.layout.video_item, null);

			viewHolder = new ViewHolder();
			viewHolder.title = (TextView) convertView
					.findViewById(R.id.kecheng_Title1);
			viewHolder.image = (ImageView) convertView
					.findViewById(R.id.kecheng_Img1);
			viewHolder.author = (TextView) convertView
					.findViewById(R.id.kecheng_Author1);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		RemenData.Data listdata = data.IMG_DESCRIPTIONS.get(position);
		viewHolder.title.setText(listdata.vname);
		viewHolder.author.setText(listdata.vauthor);
		imageLoader.displayImage(listdata.vurl, viewHolder.image, options,
				aDisplayListener);
		return convertView;
	}

	static class ViewHolder {
		public ImageView image;
		public TextView title;
		public TextView author;
	}
}
