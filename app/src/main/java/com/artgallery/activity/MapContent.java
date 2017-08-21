package com.artgallery.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.artgallery.application.BaseActivity;
import com.artgallery.wo.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MapContent extends BaseActivity implements OnClickListener {

	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private ImageView a;

	private RelativeLayout remain_layout;
	private Button remain_hide;
	private Button remain_all;
	private Button remain_ret;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map1);
		
		
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565).build();
		imageLoader = ImageLoader.getInstance();

		// 悬浮按钮
		remain_layout = (RelativeLayout) findViewById(R.id.remain_layout);
		remain_hide = (Button) findViewById(R.id.remain_hide);
		remain_all = (Button) findViewById(R.id.remain_all);
		remain_ret = (Button) findViewById(R.id.remain_ret);
		remain_layout.setOnClickListener(this);
		remain_hide.setOnClickListener(this);
		remain_all.setOnClickListener(this);
		remain_ret.setOnClickListener(this);

		a = (ImageView) findViewById(R.id.mapc);
		Button back = (Button) findViewById(R.id.mapback);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// releaseImageViewResouce(a);
				finish();
			}
		});
		Bundle bundle = this.getIntent().getExtras();
		String lou = "";
		lou = bundle.getString("lou");

		String name[] = { "drawable://" + R.drawable.loucengzhiyin06,
				"drawable://" + R.drawable.loucengzhiyin03,
				"drawable://" + R.drawable.loucengzhiyin04,
				"drawable://" + R.drawable.loucengzhiyin02,
				"drawable://" + R.drawable.loucengzhiyin05 };
		if (lou.equals("qunxue")) {
			imageLoader.displayImage(name[0], a, options);
		} else if (lou.equals("qiusuo")) {
			imageLoader.displayImage(name[1], a, options);
		} else if (lou.equals("lianti")) {
			imageLoader.displayImage(name[2], a, options);
		} else if (lou.equals("deyuan")) {
			imageLoader.displayImage(name[3], a, options);
		} else if (lou.equals("weiwen")) {
			imageLoader.displayImage(name[4], a, options);
		}

	}

	public static void releaseImageViewResouce(ImageView imageView) {
		if (imageView == null)
			return;
		Drawable drawable = imageView.getDrawable();
		if (drawable != null && drawable instanceof BitmapDrawable) {
			BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
			Bitmap bitmap = bitmapDrawable.getBitmap();
			if (bitmap != null && !bitmap.isRecycled()) {
				bitmap.recycle();
			}
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
//		case R.id.help_return:
//			Intent intent2 = new Intent();
//			intent2.setClass(this, MainActivity.class);
//			intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(intent2);
//			// finish();
//			break;
		case R.id.remain_layout:
			remain_layout.setVisibility(View.GONE);
			remain_hide.setVisibility(View.VISIBLE);
			break;
		case R.id.remain_hide:
			remain_layout.setVisibility(View.VISIBLE);
			remain_hide.setVisibility(View.GONE);
			break;
		case R.id.remain_all:
			remain_layout.setVisibility(View.GONE);
			remain_hide.setVisibility(View.VISIBLE);
			Intent intent = new Intent();
			intent.setClass(this, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case R.id.remain_ret:
			remain_layout.setVisibility(View.GONE);
			remain_hide.setVisibility(View.VISIBLE);
			finish();
			break;
		}
	}

}
