package com.artgallery.adapter;

import com.artgallery.data.ClassicBookListData;
import com.artgallery.tool.AnimateFirstDisplayListener;
import com.artgallery.wo.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.graphics.Bitmap;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BookGirdAdapter extends BaseAdapter {
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private AnimateFirstDisplayListener aDisplayListener = new AnimateFirstDisplayListener();
	private Context mContext;
	private ClassicBookListData data;

	public BookGirdAdapter(Context context, ClassicBookListData data) {
		this.data = data;
		this.mContext = context;
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
		// 定义一个ImageView,显示在GridView里
//		ImageView imageView;
//		if (convertView == null) {
//			imageView = new ImageView(mContext);
//
//			imageView.setLayoutParams(new GridView.LayoutParams(177, 242));
//			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//			imageView.setPadding(8, 18, 8, 18);
//			imageView.setBackgroundResource(R.drawable.book_item_bg);
//		} else {
//			imageView = (ImageView) convertView;
//		}
//
//		ClassicBookListData.Data listdata = data.IMG_DESCRIPTIONS.get(position);
//
//		imageLoader.displayImage(listdata.url, imageView, options,
//				aDisplayListener);
//		return imageView;
//	}
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.book_item, null);

			viewHolder = new ViewHolder();
			viewHolder.bookname = (TextView) convertView
					.findViewById(R.id.bookname);
			
			viewHolder.author = (TextView) convertView
					.findViewById(R.id.bookauthor);
			viewHolder.imageView = (ImageView) convertView
					.findViewById(R.id.bookimg);
			
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		ClassicBookListData.Data listdata = data.IMG_DESCRIPTIONS.get(position);
		viewHolder.bookname.setText(listdata.author);
		viewHolder.author.setText(listdata.bookname);
//		viewHolder.bookname.setBackgroundColor(Color.parseColor("#033f4f"));
//		viewHolder.author.setBackgroundColor(Color.parseColor("#eeeeee"));
		imageLoader.displayImage(listdata.url, viewHolder.imageView, options,
				aDisplayListener);
		return convertView;
	}

	static class ViewHolder {
		public ImageView imageView;
		public TextView bookname;
		public TextView author;
	}
}
