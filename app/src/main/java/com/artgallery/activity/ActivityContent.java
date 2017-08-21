package com.artgallery.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONObject;

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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.artgallery.adapter.HuoDongContentListAdapter;
import com.artgallery.application.BaseActivity;
import com.artgallery.data.HuoDongContentListData;
import com.artgallery.tool.AnimateFirstDisplayListener;
import com.artgallery.tool.ParseJson;
import com.artgallery.tool.XListView;
import com.artgallery.tool.XListView.IXListViewListener;
import com.artgallery.wo.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ActivityContent extends BaseActivity implements OnClickListener {
	private Button back;
	private ImageView cover;
	private ImageView erweima;
	private TextView title;
	private TextView dtBegin;
	private TextView dtEnd;
	private TextView dteEnd;
	private TextView unit;
	private TextView class1;
	private TextView num;
	private TextView limit;
	private TextView creit;
	private TextView rate1;
	private TextView rate2;
	private TextView rate3;

	private TextView intro;
	private String id;

	XListView listView;
	private ArrayList<String> datas;
	private final static int GetData = 111;
	private final static int MoreData = 222;
	private final static int REFRESH = 333;
	private final static int LISTREF = 444;
	private int officialpage = 1;
	private HuoDongContentListData officialListData;
	private HuoDongContentListAdapter adapter;
	private String url;
	private String sayUrl;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options; // DisplayImageOptions是用于设置图片显示的类
	private SharedPreferences sp;
	private RelativeLayout remain_layout;
	private Button remain_hide;
	private Button remain_all;
	private Button remain_ret;

	private ProgressBar goodProgressBar;
	private ProgressBar midProgressBar;
	private ProgressBar badProgressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.active_content);
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565).build();
		back = (Button) findViewById(R.id.act_contentback);
		cover = (ImageView) findViewById(R.id.act_cimg);
		erweima = (ImageView) findViewById(R.id.act_erweima);
		title = (TextView) findViewById(R.id.act_ctitle);
		dtBegin = (TextView) findViewById(R.id.act_cbegin);
		dtEnd = (TextView) findViewById(R.id.act_cend);
		dteEnd = (TextView) findViewById(R.id.act_ceend);
		unit = (TextView) findViewById(R.id.act_cunit);
		class1 = (TextView) findViewById(R.id.act_ctype);
		num = (TextView) findViewById(R.id.act_cnum);
		limit = (TextView) findViewById(R.id.act_cenum);
		creit = (TextView) findViewById(R.id.act_ccreit);
		intro = (TextView) findViewById(R.id.act_cintro);
		rate1 = (TextView) findViewById(R.id.rate1);
		rate2 = (TextView) findViewById(R.id.rate2);
		rate3 = (TextView) findViewById(R.id.rate3);
		goodProgressBar = (ProgressBar) findViewById(R.id.goodp);
		midProgressBar = (ProgressBar) findViewById(R.id.midp);
		badProgressBar = (ProgressBar) findViewById(R.id.badp);

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

		Bundle bundle = this.getIntent().getExtras();
		url = bundle.getString("url");
		id = bundle.getString("id");
		gethttp(url);

		
		sp = getSharedPreferences("SP", MODE_PRIVATE);
		
		officialListData = new HuoDongContentListData();
		sayUrl = getString(R.string.url_activeSay)
				+ "n=androiddp001&d=8a515e992833d2f1dcb8bec66a064255&"
				+ "&size=2&page=" + officialpage + "&androidcode="
				+ sp.getString("mac", "null mac") + "&actid=" + id;

		System.out.println("sayUrl:" + sayUrl);
		listView = (XListView) findViewById(R.id.active_clist);
		listView.setPullLoadEnable(true);
		listView.setPullRefreshEnable(false);
		listView.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						System.out.println("刷新数据");
						onLoad();
					}
				}, 2000);

			}

			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				LoadMoreData();
			}
		});

		getListData();

	}

	private void getListData() {
		// TODO Auto-generated method stub

		RequestQueue queue = Volley.newRequestQueue(this);
		StringRequest request = new StringRequest(sayUrl,
				new Listener<String>() {
					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						System.out.println(response);
						ParseJson parseJson = new ParseJson();
						try {
							parseJson.parseHuoDongContentList(response,
									officialListData.List_Data);
							handler.sendEmptyMessage(ActivityContent.LISTREF);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub

					}
				});
		queue.add(request);
	}

	private void gethttp(String url2) {
		// TODO Auto-generated method stub
		RequestQueue queue = Volley.newRequestQueue(this);
		StringRequest request = new StringRequest(url2, new Listener<String>() {

			@Override
			public void onResponse(String response) {
				// TODO Auto-generated method stub
				System.out.println(response);
				ParseJson parseJson = new ParseJson();
				try {
					datas = parseJson.parseActiveContent(response);
					Message message = new Message();
					message.what = ActivityContent.REFRESH;
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

			}
		});
		queue.add(request);
	}

	private void onLoad() {
		listView.stopRefresh();
		listView.stopLoadMore();
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy年MM月dd日    HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String time = formatter.format(curDate);
		listView.setRefreshTime(time);
	}

	private boolean LoadMoreData() {
		officialpage++;
		url = getString(R.string.url_activeSay)
				+ "n=androiddp001&d=8a515e992833d2f1dcb8bec66a064255&"
				+ "&size=2&page=" + officialpage + "&androidcode="
				+ sp.getString("mac", "null mac") + "&actid=" + id;

		RequestQueue queue = Volley.newRequestQueue(this);
		StringRequest request = new StringRequest(url, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				// TODO Auto-generated method stub
				System.out.println(response);
				ParseJson parseJson = new ParseJson();

				try {
					JSONObject job = new JSONObject(response);
					if (job.getString("data").equals("[]")) {
						Toast.makeText(getApplicationContext(), "暂无更多数据",
								Toast.LENGTH_SHORT).show();
						onLoad();
						officialpage--;
					} else {
						parseJson.parseHuoDongContentList(response,
								officialListData.List_Data);
						handler.sendEmptyMessage(ActivityContent.MoreData);
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
				Toast.makeText(getApplicationContext(), "暂无更多数据!",
						Toast.LENGTH_SHORT).show();
				onLoad();
				officialpage--;
			}
		});
		queue.add(request);
		return true;
	}

	public Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ActivityContent.MoreData:
				adapter.notifyDataSetChanged();
				onLoad();
				break;
			case ActivityContent.REFRESH:

				AnimateFirstDisplayListener aDisplayListener = new AnimateFirstDisplayListener();
				if (datas.get(0) != null)
					imageLoader.displayImage(datas.get(0), cover, options,
							aDisplayListener);
				if (datas.get(1) != null)
					imageLoader.displayImage(datas.get(1), erweima, options,
							aDisplayListener);

				title.setText(datas.get(2));
				dtBegin.setText("开始时间：" + datas.get(3));
				dtEnd.setText("结束时间：" + datas.get(4));
				dteEnd.setText(datas.get(5));
				unit.setText("发布单位：" + datas.get(6));
				class1.setText(datas.get(7));
				num.setText("已报名人数：" + datas.get(8));
				limit.setText("限制" + datas.get(9) + "人");
				creit.setText(datas.get(10) + "学分");
				intro.setText(datas.get(11));
				rate1.setText(datas.get(12)); // good
				rate2.setText(datas.get(13));// mid
				rate3.setText(datas.get(14));// bad
				// rate4.setText(datas.get(15)); all

				goodProgressBar.setMax(Integer.parseInt(datas.get(15)));
				goodProgressBar.setProgress(Integer.parseInt(datas.get(12)));
				midProgressBar.setMax(Integer.parseInt(datas.get(15)));
				midProgressBar.setProgress(Integer.parseInt(datas.get(13)));
				badProgressBar.setMax(Integer.parseInt(datas.get(15)));
				badProgressBar.setProgress(Integer.parseInt(datas.get(14)));
				break;
			case ActivityContent.LISTREF:
				adapter = new HuoDongContentListAdapter(
						getApplicationContext(), officialListData);
				listView.setAdapter(adapter);

				break;
			}
			super.handleMessage(msg);
		}

	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		switch (v.getId()) {

		case R.id.act_contentback:
			intent.setClass(this, ActiveActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
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

			intent.setClass(this, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case R.id.remain_ret:
			remain_layout.setVisibility(View.GONE);
			remain_hide.setVisibility(View.VISIBLE);
			intent.setClass(this, ActiveActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		unbindDrawables(listView);
		super.onDestroy();
	}
}
