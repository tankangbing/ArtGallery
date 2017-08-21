package com.artgallery.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.artgallery.application.BaseActivity;
import com.artgallery.tool.Banner;
import com.artgallery.wo.R;

public class GuoXueActivity extends BaseActivity implements OnClickListener {

	private Button back;
	private Button btn1;
	private Button btn2;
	private Button btn3;
	private RelativeLayout remain_layout;
	private Button remain_hide;
	private Button remain_all;
	private Button remain_ret;
	private LinearLayout guoxue_bg;
	private LinearLayout guoxue_pic;
	private LinearLayout guoxue_pic2;
	private VideoView myVideoView;
	private String videopath;
	private Uri uri;

	private View dot0;
	private View dot1;
	private View dot2;
	private View dot3;
	private View dot4;
	private List<View> dots1; // 图片标题正文的那些点
	private ViewPager adViewPager;
	private Banner ban = new Banner();
	private ArrayList<String> imgurl1;
	private ArrayList<String> imgurl2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guoxue_activity);

		back = (Button) findViewById(R.id.guoxue_backbtn);
		btn1 = (Button) findViewById(R.id.guoxue_btn1);
		btn2 = (Button) findViewById(R.id.guoxue_btn2);
		btn3 = (Button) findViewById(R.id.guoxue_btn3);
		guoxue_bg = (LinearLayout) findViewById(R.id.guoxue_bg);
		guoxue_pic= (LinearLayout) findViewById(R.id.guoxue_pic);
		guoxue_pic2= (LinearLayout) findViewById(R.id.guoxue_pic2);
		remain_layout = (RelativeLayout) findViewById(R.id.remain_layout);
		remain_hide = (Button) findViewById(R.id.remain_hide);
		remain_all = (Button) findViewById(R.id.remain_all);
		remain_ret = (Button) findViewById(R.id.remain_ret);
		myVideoView = (VideoView) findViewById(R.id.hvideo);
		remain_layout.setOnClickListener(this);
		remain_hide.setOnClickListener(this);
		remain_all.setOnClickListener(this);
		remain_ret.setOnClickListener(this);

		back.setOnClickListener(this);
		btn1.setOnClickListener(this);
		btn2.setOnClickListener(this);
		btn3.setOnClickListener(this);
		
		guoxue_bg.setBackgroundResource(R.drawable.guoxue_bgrehe2);
		guoxue_pic.setVisibility(View.VISIBLE);
		guoxue_pic2.setVisibility(View.GONE);
		imgurl1 = new ArrayList<String>();
		imgurl1.add("assets://guoxue102.png");
		imgurl1.add("assets://guoxue103.png");
		dots1 = new ArrayList<View>();
		dot0 = findViewById(R.id.v1_dot0);
		dot1 = findViewById(R.id.v1_dot1);
		dot2 = findViewById(R.id.v1_dot2);
		dots1.add(dot0);
		dots1.add(dot1);

		adViewPager = (ViewPager) findViewById(R.id.msg_vp1);
		ban.banner(getApplicationContext(), imgurl1, adViewPager);
		ban.VpStop();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.guoxue_backbtn:
			finish();
			break;
		case R.id.guoxue_btn1:
			btn1.setBackgroundResource(R.drawable.guoxue_rehe1_btn1_on);
			btn2.setBackgroundResource(R.drawable.guoxue_rehe1_btn2_off);
			btn3.setBackgroundResource(R.drawable.guoxue_rehe1_btn3_off);
			guoxue_bg.setBackgroundResource(R.drawable.guoxue_bgrehe2);
			guoxue_pic.setVisibility(View.VISIBLE);
			guoxue_pic2.setVisibility(View.GONE);
			myVideoView.pause();
			imgurl1 = new ArrayList<String>();
			imgurl1.add("assets://guoxue102.png");
			imgurl1.add("assets://guoxue103.png");
			dots1 = new ArrayList<View>();
			dot0 = findViewById(R.id.v1_dot0);
			dot1 = findViewById(R.id.v1_dot1);
			dots1.add(dot0);
			dots1.add(dot1);

			adViewPager = (ViewPager) findViewById(R.id.msg_vp1);
			ban.banner(this, imgurl1, adViewPager);
			ban.VpStop();
			break;
		case R.id.guoxue_btn2:
			btn1.setBackgroundResource(R.drawable.guoxue_rehe1_btn1_off);
			btn2.setBackgroundResource(R.drawable.guoxue_rehe1_btn2_on);
			btn3.setBackgroundResource(R.drawable.guoxue_rehe1_btn3_off);
			guoxue_bg.setBackgroundResource(R.drawable.guoxue_bgrehe2);
			guoxue_pic.setVisibility(View.VISIBLE);
			guoxue_pic2.setVisibility(View.GONE);
			myVideoView.pause();
			imgurl2 = new ArrayList<String>();
			imgurl2.add("assets://guoxue101.png");
			imgurl2.add("assets://guoxue104.png");
			imgurl2.add("assets://guoxue105.png");
			dots1 = new ArrayList<View>();
			dot0 = findViewById(R.id.v1_dot0);
			dot1 = findViewById(R.id.v1_dot1);
			dot2 = findViewById(R.id.v1_dot2);
			dots1.add(dot0);
			dots1.add(dot1);
			dots1.add(dot2);

			adViewPager = (ViewPager) findViewById(R.id.msg_vp1);
			ban.banner(this, imgurl2, adViewPager);
			ban.VpStop();
			break;
		case R.id.guoxue_btn3:
			btn1.setBackgroundResource(R.drawable.guoxue_rehe1_btn1_off);
			btn2.setBackgroundResource(R.drawable.guoxue_rehe1_btn2_off);
			btn3.setBackgroundResource(R.drawable.guoxue_rehe1_btn3_on);
			guoxue_bg.setBackgroundResource(R.drawable.guoxue_bgrehe1);
			guoxue_pic.setVisibility(View.GONE);
			guoxue_pic2.setVisibility(View.VISIBLE);
			videopath = Environment.getExternalStorageDirectory().getPath()+"/xxxcsp1.mp4";
			uri = Uri.parse( videopath );
			myVideoView.setVideoURI(uri);
			myVideoView.requestFocus();

			myVideoView.start();
			myVideoView
					.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

						@Override
						public void onCompletion(MediaPlayer mp) {

							// videopath = Environment.getExternalStorageDirectory()
							// .getAbsolutePath()
							// + "/ZHDJ/"
							// + videoflag
							// + ".mp4";

							myVideoView.setVideoURI(uri);
							myVideoView.start();

						}
					});
			break;
		case R.id.remain_layout:
			remain_layout.setVisibility(View.GONE);
			remain_hide.setVisibility(View.VISIBLE);
			break;
		case R.id.remain_hide:
			remain_layout.setVisibility(View.VISIBLE);
			remain_hide.setVisibility(View.GONE);
			break;
		case R.id.remain_ret:
			remain_layout.setVisibility(View.GONE);
			remain_hide.setVisibility(View.VISIBLE);
			finish();
			break;
		case R.id.remain_all:
			remain_layout.setVisibility(View.GONE);
			remain_hide.setVisibility(View.VISIBLE);
			intent.setClass(this, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		unbindDrawables(adViewPager);
		ban.releaseImageViewResouce();
		super.onDestroy();
	}
}
