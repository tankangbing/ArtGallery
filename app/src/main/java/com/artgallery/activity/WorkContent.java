package com.artgallery.activity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.artgallery.application.BaseActivity;
import com.artgallery.tool.AnimateFirstDisplayListener;
import com.artgallery.tool.ParseJson;
import com.artgallery.wo.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class WorkContent extends BaseActivity implements OnClickListener {
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options; // DisplayImageOptions是用于设置图片显示的类
	private SharedPreferences sp;
	private String url;
	private Map<String, String> DetailsData;

	private Button back;
	private ImageView fengmian;
	private ImageView work_erweima;
	private TextView zhichang_name;
	private TextView zhichang_pay;
	private TextView zhichang_companyname;
	private TextView zhichang_peopleCount;
	private TextView zhichang_time;
	private TextView zhichang_detail;

	private RelativeLayout remain_layout;
	private Button remain_hide;
	private Button remain_all;
	private Button remain_ret;

	public Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			// case ZhiChangDetailsActivity.GetData:
			case 101:
				setDetailsData();
				break;
			}
			super.handleMessage(msg);
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.work_content);
		new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565).build();
		sp = getSharedPreferences("SP", MODE_PRIVATE);
		Intent intent = new Intent();
		intent = getIntent();
		Bundle bundle = new Bundle(); 
		bundle = intent.getExtras();
		url = bundle.getString("url");
		DetailsData = new HashMap<String, String>();
		fengmian = (ImageView) findViewById(R.id.work_cimg);
		work_erweima = (ImageView) findViewById(R.id.work_erweima);
		zhichang_name = (TextView) findViewById(R.id.work_title);
		zhichang_pay = (TextView) findViewById(R.id.work_pay);
		zhichang_companyname = (TextView) findViewById(R.id.work_company);
		zhichang_peopleCount = (TextView) findViewById(R.id.work_num);
		zhichang_detail = (TextView) findViewById(R.id.work_content);

		zhichang_time = (TextView) findViewById(R.id.work_time);
		back = (Button) findViewById(R.id.work_contentback);
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

		getDetailsData();
		super.onCreate(savedInstanceState);
	}

	private void setDetailsData() {
		AnimateFirstDisplayListener aDisplayListener = new AnimateFirstDisplayListener();
		imageLoader.displayImage(DetailsData.get("cover"), fengmian, options,
				aDisplayListener);

		zhichang_name.setText(DetailsData.get("position"));
		zhichang_pay.setText("薪资：" + "￥" + DetailsData.get("pay"));
		zhichang_companyname.setText(DetailsData.get("company"));
		zhichang_peopleCount.setText("招聘人数：" + DetailsData.get("limit") + "人");
		zhichang_time.setText("发布时间：" + DetailsData.get("dt"));
		zhichang_detail.setText(DetailsData.get("intro"));

		imageLoader.displayImage(DetailsData.get("focusImg"), work_erweima,
				options, aDisplayListener);

	}

	private void getDetailsData() {
		// TODO Auto-generated method stub
		RequestQueue queue = Volley.newRequestQueue(this);
		StringRequest request = new StringRequest(url, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				// TODO Auto-generated method stub
				System.out.println(response);
				ParseJson parseJson = new ParseJson();
				try {
					parseJson.parseZhiChangDetail(response, DetailsData,
							sp.getString("UserName", ""));
					handler.sendEmptyMessage(101);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.work_contentback:
			finish();
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
			finish();
			break;
		default:
			break;
		}
	}

}
