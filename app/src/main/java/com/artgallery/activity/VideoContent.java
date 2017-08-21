package com.artgallery.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.artgallery.adapter.KeChengDirUrlAdapter;
import com.artgallery.application.BaseActivity;
import com.artgallery.data.UrlData;
import com.artgallery.tool.ParseJson;
import com.artgallery.wo.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class VideoContent extends BaseActivity implements OnClickListener {
	private String url;
	public final static int REFRESH = 111;
	public final static int GETNULL = 222;
	public final static int ADAPTER = 333;
	private Context context;
	private ArrayList<String> fatherList;
	private List<List<UrlData>> childList = new ArrayList<List<UrlData>>();
	private EditText suosuoText;
	// private Button moreButton;
	private String mac;
	private Button ret;
	private ImageView imgview;
	private TextView nameTextView;
	private TextView thiefTextView;
	private TextView infoTextView;
	private ImageView erweima;

	private Button find;

	private RelativeLayout remain_layout;
	private Button remain_hide;
	private Button remain_all;
	private Button remain_ret;

	private int ss;
	private String idString;
	private String sBitmap;
	private ArrayList<String> datas;
	private Boolean clickableBoolean = true;
	private SharedPreferences sp;
	private Boolean checkflag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.video_content);

		Bundle bundle = this.getIntent().getExtras();
//		bundle= null;
		idString = "";
		if (bundle == null) {
			Toast.makeText(context, "数据加载失败，请返回重试", Toast.LENGTH_SHORT).show();
		}else {
			idString = bundle.getString("id");
		}
		

		context = this;
		ret = (Button) findViewById(R.id.rmcontent_return);
		imgview = (ImageView) findViewById(R.id.content_img_kc);
		nameTextView = (TextView) findViewById(R.id.content_name_kc);
		thiefTextView = (TextView) findViewById(R.id.content_chief_kc);
		infoTextView = (TextView) findViewById(R.id.content_intro_kc);
		erweima = (ImageView) findViewById(R.id.content_erweima_kc);
		find = (Button) findViewById(R.id.ss_btn);
		suosuoText = (EditText) findViewById(R.id.suosuo_edit);
		remain_layout = (RelativeLayout) findViewById(R.id.remain_layout);
		remain_hide = (Button) findViewById(R.id.remain_hide);
		remain_all = (Button) findViewById(R.id.remain_all);
		remain_ret = (Button) findViewById(R.id.remain_ret);

		remain_layout.setOnClickListener(this);
		remain_hide.setOnClickListener(this);
		remain_all.setOnClickListener(this);
		remain_ret.setOnClickListener(this);
		ret.setOnClickListener(this);
		sp = getSharedPreferences("SP", MODE_PRIVATE);
		mac = sp.getString("mac", "null mac");
		url = "http://data.iego.net:88/m/lessonInfoJson2.aspx?"
				+ "n=androiddp001&d=8a515e992833d2f1dcb8bec66a064255&"
				+ "lesson_id=" + idString + "&androidcode=" + mac;
		checkflag = true;
		init();

	}

	private void init() {
		RequestQueue queue = Volley.newRequestQueue(this);
		StringRequest request = new StringRequest(url, new Listener<String>() {

			@Override
			public void onResponse(String response) {
				// TODO Auto-generated method stub
				System.out.println(response);
				ParseJson parseJson = new ParseJson();
				try {
					datas = parseJson.parseKcContent(response);
					fatherList = parseJson.parseKechengFather(response);
					for (int i = 0; i < fatherList.size(); i++) {
						List<UrlData> data1 = parseJson.parseKechengChild(
								response, i);
						childList.add(data1);
					}
					Message message = new Message();
					message.what = VideoContent.REFRESH;
					handler.sendMessage(message);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "网络异常，请返回重试",
						Toast.LENGTH_SHORT).show();
			}
		});
		queue.add(request);
	}

	public Handler handler = new Handler() {
		@SuppressLint("NewApi")
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case VideoContent.REFRESH:
				ImageLoader imageLoader = ImageLoader.getInstance();
				DisplayImageOptions options;
				options = new DisplayImageOptions.Builder().cacheInMemory(true)
						.cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565)
						.build();
				nameTextView.setText("标题：" + datas.get(0));
				thiefTextView.setText("作者：" + datas.get(1));
				// 简介
				infoTextView.setText(datas.get(4));
				// 目录

				imageLoader.displayImage(datas.get(2), imgview, options);

				try {
					sBitmap = "http://data.iego.net:85/qrcode/qrimg.aspx?str=VIDEO|"
							+ mac
							+ "|"
							+ idString
							+ "|"
							+ URLEncoder.encode(datas.get(0), "UTF-8")
							+ "|"
							+ datas.get(6) + "|" + url;

				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				System.out.println("url:" + sBitmap);
				imageLoader.displayImage(sBitmap, erweima, options);

				KeChengDirUrlAdapter adapter = new KeChengDirUrlAdapter(
						context, fatherList, childList, getIntent().getExtras()
								.getString("listurl",	 "null"), idString);
				ExpandableListView listView = (ExpandableListView) findViewById(R.id.listView_kc);
				listView.setAdapter(adapter);
				int groupCount = listView.getCount();
				for (int i = 0; i < groupCount; i++) {
					listView.expandGroup(i);
				}
				;

				break;
			case VideoContent.GETNULL:
				Toast.makeText(VideoContent.this, "搜索内容不存在", Toast.LENGTH_SHORT)
						.show();
				break;
			case VideoContent.ADAPTER:
				break;

			}
			super.handleMessage(msg);
			clickableBoolean = true;
		}

	};

	@Override
	public void onClick(View v) {
		if (clickableBoolean) {

			String kString;
			switch (v.getId()) {
			case R.id.ss_btn:

				kString = suosuoText.getText().toString();
				if (kString.equals("")) {
					Toast.makeText(VideoContent.this, "请输入内容再搜索",
							Toast.LENGTH_SHORT).show();
				} else {
					clickableBoolean = false;
					Ref(kString);
					ss = 1;
				}

				break;
			case R.id.more_btn:
				final String[] strings = { "全部", "精选专题", "美术类", "设计类", "摄影与影视",
						"音乐", "戏剧艺术", "文学", "外语", "教育", "体育", "旅游", "家居生活",
						"IT与互联网", "游戏", "医学与健康", "数学与科学", "法律", "财务会计", "国际贸易",
						"市场营销", "经济和金融", "工商管理", "面试求职", "创业创新", "安全教育",
						"励志人生", "其它" };
				Builder builder = new AlertDialog.Builder(VideoContent.this);
				builder.setTitle("请选择分类");
				builder.setSingleChoiceItems(strings, 0,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								if (which == 0) {
									strings[0] = "";
									Ref("");
									dialog.cancel();
								} else {
									Ref(strings[which]);
									dialog.cancel();
								}
								clickableBoolean = false;
							}
						});

				builder.setNegativeButton("取消", null);
				AlertDialog dialog = builder.create();
				dialog.show();

				break;

			case R.id.rmcontent_return:
				if (checkflag) {
					checkflag = false;
					finish();
				}
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
				if (checkflag) {
					checkflag = false;
					finish();
				}
				break;
			default:
				break;
			}
		}
	}

	private void Ref(String kString) {

		Bundle bundle = new Bundle();
		Intent intent = new Intent();
		bundle.putString("kString", kString);
		bundle.putString("acType", "to");

		if (ss == 1) {
			bundle.putBoolean("kbool", false);
		} else {
			bundle.putBoolean("kbool", true);
		}
		intent.putExtras(bundle);
		intent.setClass(context, VideoActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		
		super.onDestroy();
	}
}
