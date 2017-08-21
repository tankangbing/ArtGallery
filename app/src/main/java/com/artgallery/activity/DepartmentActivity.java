package com.artgallery.activity;

import java.util.ArrayList;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.artgallery.adapter.DepartmentListAdapter;
import com.artgallery.application.BaseActivity;
import com.artgallery.data.DepartmentData;
import com.artgallery.data.DepartmentTypeData;
import com.artgallery.tool.ParseJson;
import com.artgallery.wo.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class DepartmentActivity extends BaseActivity implements OnClickListener {

	private Button back;
	private Button uppage;
	private Button downpage;
	private Button startpage;
	private Button endpage;
	private Button fabushijian;
	private Button fabuxueyuan;
	private ListView de_list;
	private Context context;
	private DepartmentListAdapter adapter;
	private String url;
	private int page;
	private String mac;
	private String keepUrl;
	private String[] breakurl;
	private String newurl;
	private ArrayList<String> departmentList;
	private ArrayList<String> departmentList2;
	private DepartmentData datas;
	private DepartmentTypeData typedatas;

	private int pages;
	private int current_page;
	private int count;

	private TextView pagesView;
	private TextView currentView;
	private TextView countView;
	private ImageView depart_title;

	private Boolean click = true;

	private RelativeLayout remain_layout;
	private Button remain_hide;
	private Button remain_all;
	private Button remain_ret;
	private SharedPreferences sp;

	private ImageLoader imageLoader;
	private DisplayImageOptions options;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.department_activity);
		back = (Button) findViewById(R.id.de_backbtn);
		uppage = (Button) findViewById(R.id.de_up);
		downpage = (Button) findViewById(R.id.de_down);
		startpage = (Button) findViewById(R.id.de_start);
		endpage = (Button) findViewById(R.id.de_end);
		fabushijian = (Button) findViewById(R.id.department_fabushijian);
		fabuxueyuan = (Button) findViewById(R.id.department_fabuxueyuan);
		de_list = (ListView) findViewById(R.id.de_list);
		pagesView = (TextView) findViewById(R.id.de_page);
		currentView = (TextView) findViewById(R.id.de_currentpage);
		countView = (TextView) findViewById(R.id.de_count);
		depart_title = (ImageView) findViewById(R.id.depart_title);

		setbg(depart_title, 0);

		datas = new DepartmentData();
		typedatas = new DepartmentTypeData();

		// 悬浮按钮
		remain_layout = (RelativeLayout) findViewById(R.id.remain_layout);
		remain_hide = (Button) findViewById(R.id.remain_hide);
		remain_all = (Button) findViewById(R.id.remain_all);
		remain_ret = (Button) findViewById(R.id.remain_ret);
		remain_layout.setOnClickListener(this);
		remain_hide.setOnClickListener(this);
		remain_all.setOnClickListener(this);
		remain_ret.setOnClickListener(this);

		back.setOnClickListener(this);
		uppage.setOnClickListener(this);
		downpage.setOnClickListener(this);
		startpage.setOnClickListener(this);
		endpage.setOnClickListener(this);
		fabushijian.setOnClickListener(this);
		fabuxueyuan.setOnClickListener(this);
		sp = getSharedPreferences("SP", MODE_PRIVATE);
		mac = sp.getString("mac", "null mac");
		page = 1;

		url = sp.getString("IPaddress", "http://192.168.30.160:8000")
				+ getString(R.string.url_department) + "page=" + page
				+ "&size=13&mac=" + mac;

		System.out.println(url);
		gethttp(url, 101);

		// de_list = new ListView(this);

	}

	private void gethttp(String url, final int state) {
		// TODO Auto-generated method stub
		keepUrl = url;
		RequestQueue queue = Volley.newRequestQueue(this);
		StringRequest request = new StringRequest(url, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				// TODO Auto-generated method stub
				System.out.println(response);
				datas.List_Data.clear();
				typedatas.List_Data.clear();
				ParseJson parseJson = new ParseJson();
				try {
					JSONObject jsonObject = new JSONObject(response);
					pages = jsonObject.getJSONObject("paginator").getInt(
							"pages");
					current_page = jsonObject.getJSONObject("paginator")
							.getInt("current_page");
					count = jsonObject.getJSONObject("paginator").getInt(
							"count");
					parseJson.parseDepartmentData(response, datas.List_Data,
							typedatas.List_Data);

					if (state == 101) {
						handler.sendEmptyMessage(101);
					} else {
						handler.sendEmptyMessage(102);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				Toast.makeText(context, "网络异常，请返回重试", Toast.LENGTH_SHORT)
						.show();
			}
		});
		queue.add(request);

	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case 101:
				// 获得解析后的数据，进行UI安排
				adapter = new DepartmentListAdapter(context, datas);
				de_list.setAdapter(adapter);
				pagesView.setText(current_page + "/" + pages + "页");
				currentView.setText("第" + current_page + "页");
				countView.setText("总共有" + count + "条记录");

				de_list.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						final String idString = datas.List_Data.get(arg2).de_url;
						if (!(idString == null || idString.length() <= 0)) {
							// TODO 自动生成的方法存根
							System.out.println("url===" + idString);
							Bundle bundle = new Bundle();
							Intent intent = new Intent();
							bundle.putString("url", idString);
							intent.putExtras(bundle);
							intent.setClass(context, DepartmentContent.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(intent);
						}
					}
				});
				click = true;

				break;
			case 102:
				pagesView.setText(current_page + "/" + pages + "页");
				currentView.setText("第" + current_page + "页");
				countView.setText("总共有" + count + "条记录");
				adapter.notifyDataSetChanged();

				break;
			case 103:

				break;
			case 104:

				break;

			default:
				break;
			}
		};
	};

	private class datelistener implements OnDateSetListener {

		@Override
		public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
			// TODO Auto-generated method stub
			// 调用完日历控件点完成后干的事
			String data = arg1 + "-" + (arg2 + 1) + "-" + arg3;
			keepUrl = url + "&created_at=" + data;
			gethttp(keepUrl, 102);
			System.out.println(data);

		}
	}

	private void setbg(ImageView v, int i) {

		String bgname[] = { "drawable://" + R.drawable.yuanxizhuye_03,
				"drawable://" + R.drawable.xiaoyuanzixun_02,
				"drawable://" + R.drawable.yuanxizhuye_07};

		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565).build();
		imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(bgname[i], v, options);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (click) {
			switch (v.getId()) {
			case R.id.de_backbtn:
				finish();
				break;
			case R.id.de_start:
				if (page == 1) {
					// 已经是首页了
				} else {
					click = false;
					breakurl = keepUrl.split("page=" + page);
					page = 1;
					newurl = breakurl[0] + "page=" + "1" + breakurl[1];
					gethttp(newurl, 102);
				}
				break;
			case R.id.de_end:
				if (page == pages) {
				} else {
					click = false;
					breakurl = keepUrl.split("page=" + page);
					page = pages;
					newurl = breakurl[0] + "page=" + pages + breakurl[1];
					gethttp(newurl, 102);
				}

				break;
			case R.id.de_up:
				if (page > 1) {
					click = false;
					breakurl = keepUrl.split("page=" + page);
					page -= 1;
					newurl = breakurl[0] + "page=" + page + breakurl[1];
					gethttp(newurl, 102);
				} else {
					Toast.makeText(DepartmentActivity.this, "这已经是首页了",
							Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.de_down:
				if (page < pages) {
					click = false;
					breakurl = keepUrl.split("page=" + page);
					page += 1;
					newurl = breakurl[0] + "page=" + page + breakurl[1];
					gethttp(newurl, 102);
				} else {
					Toast.makeText(DepartmentActivity.this, "这已经是最后一页了",
							Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.department_fabushijian:
				//
				new DatePickerDialog(this, new datelistener(), 2016, 10, 10)
						.show();

				break;
			case R.id.department_fabuxueyuan:
				final String a[] = new String[typedatas.List_Data.size()];
				final String b[] = new String[typedatas.List_Data.size()];
				for (int i = 0; i < typedatas.List_Data.size(); i++) {
					a[i] = (typedatas.List_Data.get(i).de_title);
					b[i] = (typedatas.List_Data.get(i).de_id);
				}

				Builder builder = new AlertDialog.Builder(
						DepartmentActivity.this);
				builder.setTitle("请选择分类");
				builder.setSingleChoiceItems(a, 0,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO 自动生成的方法存根
								click = false;
								keepUrl = url + "&college=" + b[which];

								System.out.println(keepUrl);
								gethttp(keepUrl, 102);
								dialog.cancel();

							}
						});

				builder.setNegativeButton("取消", null);
				AlertDialog dialog = builder.create();
				dialog.show();
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
				click = false;
				remain_layout.setVisibility(View.GONE);
				remain_hide.setVisibility(View.VISIBLE);
				Intent intent = new Intent();
				intent.setClass(this, MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				break;
			case R.id.remain_ret:
				click = false;
				remain_layout.setVisibility(View.GONE);
				remain_hide.setVisibility(View.VISIBLE);
				finish();
				break;
			}
		}
		click = true;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		unbindDrawables(de_list);
		super.onDestroy();
	}
}
