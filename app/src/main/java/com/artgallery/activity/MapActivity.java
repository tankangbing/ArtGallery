package com.artgallery.activity;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.artgallery.application.BaseActivity;
import com.artgallery.data.FloorData;
import com.artgallery.tool.Banner;
import com.artgallery.tool.ParseJson;
import com.artgallery.wo.R;

public class MapActivity extends BaseActivity implements OnClickListener {

	private ViewPager adViewPager;
	private Banner ban = new Banner();
	private ArrayList<String> imgurl;
	
	private Button back;
	private SharedPreferences sp;
	private Context context;

	private RelativeLayout remain_layout;
	private Button remain_hide;
	private Button remain_all;
	private Button remain_ret;
	private FloorData floorData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.map0);
		sp = getSharedPreferences("SP", MODE_PRIVATE);
		context = this;
		floorData = new FloorData();
		getData();
		
		back = (Button) findViewById(R.id.mapback);
		back.setOnClickListener(this);

		// 悬浮按钮
		remain_layout = (RelativeLayout) findViewById(R.id.remain_layout);
		remain_hide = (Button) findViewById(R.id.remain_hide);
		remain_all = (Button) findViewById(R.id.remain_all);
		remain_ret = (Button) findViewById(R.id.remain_ret);
		remain_layout.setOnClickListener(this);
		remain_hide.setOnClickListener(this);
		remain_all.setOnClickListener(this);
		remain_ret.setOnClickListener(this);

	}


	private void getData() {
		String url = sp.getString("IPaddress", "http://192.168.30.160:8888")
				+ "/cirmSystem/camCul/cultureLou/findCultureLous_Client?param.page=1&param.rows=100&param.sidx=createTime&param.sord=desc";
		RequestQueue queue = Volley.newRequestQueue(this);
		StringRequest request = new StringRequest(url, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				// TODO Auto-generated method stub
				System.out.println(response);
				ParseJson parseJson = new ParseJson();
				try {
					parseJson.parseFloor(response, floorData.floor_data);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				setData();
			}

		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "网络异常，请返回重试", Toast.LENGTH_SHORT).show();
			}
		});
		queue.add(request);
	}
	
	private void setData() {
		// TODO Auto-generated method stub
		imgurl = new ArrayList<String>();
		for (int i = 0; i < floorData.floor_data.size(); i++) {
			imgurl.add(floorData.floor_data.get(i).floorpic);
		}
		
		adViewPager = (ViewPager) findViewById(R.id.msg_vp1);
		ban.banner(getApplicationContext(), imgurl, adViewPager);
		ban.VpStop();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		
			case R.id.mapback:
				Intent intent2 = new Intent();
				intent2.setClass(this, MainActivity.class);
				intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent2);
				break;
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
				Intent intent3 = new Intent();
				intent3.setClass(this, MainActivity.class);
				intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent3);
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
