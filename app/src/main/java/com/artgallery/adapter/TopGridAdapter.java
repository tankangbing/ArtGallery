package com.artgallery.adapter;

import com.artgallery.tool.AnimateFirstDisplayListener;
import com.artgallery.wo.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class TopGridAdapter extends BaseAdapter {
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private AnimateFirstDisplayListener aDisplayListener = new AnimateFirstDisplayListener();
	private Context Context;
	private int i;
	private int f;
	private String mThumbIds[] = { "", "", "", "", "", "", "", "" };



	public TopGridAdapter(Context context, int i, int f) {
		this.i = i;
		this.f = f;
		this.Context = context;
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565).build();
		imageLoader = ImageLoader.getInstance();

		init(i, f);

	}

	public void init(int i, int f) {
		if (i == 0) {
			for (int k = 0; k < 8; k++) {
				mThumbIds[k] = "drawable://" + (R.drawable.yuedujingdian_11 + k);
			}
			mThumbIds[f] = "drawable://" + (R.drawable.yuedujingdian_03 + f);

		} else {
			for (int k = 0; k < 8; k++) {
				mThumbIds[k] = "drawable://" + (R.drawable.suzhijiaoyu_10 + k);
			}
			mThumbIds[f] = "drawable://" + (R.drawable.suzhijiaoyu_02 + f);
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mThumbIds.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mThumbIds[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ImageView imageView;
		if (convertView == null) {
			imageView = new ImageView(Context);
			imageView.setLayoutParams(new GridView.LayoutParams(200, 100));
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imageView.setPadding(8, 8, 8, 8);
		} else {
			imageView = (ImageView) convertView;
		}

		imageLoader.displayImage(mThumbIds[position], imageView, options,
				aDisplayListener);
		
		

		return imageView;
	}

}
