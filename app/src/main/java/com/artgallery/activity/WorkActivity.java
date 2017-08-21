package com.artgallery.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.artgallery.adapter.VideoAdapter;
import com.artgallery.adapter.ZhiChangListAdapter;
import com.artgallery.application.BaseActivity;
import com.artgallery.data.RemenData;
import com.artgallery.data.ZhiChangListData;
import com.artgallery.tool.ParseJson;
import com.artgallery.tool.XListView;
import com.artgallery.tool.XListView.IXListViewListener;
import com.artgallery.wo.R;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class WorkActivity extends BaseActivity implements OnClickListener {

	private Button activebackbtn;
	private Button btn1;
	private Button btn2;
	private Button btn3;
	private Button btn4;
	private XListView listView;

	private LinearLayout titletype;
	private LinearLayout ac_type;
	private LinearLayout ac_work;
	private LinearLayout ac_video;
	private Context context;
	private RemenData datas;
	private String mac;
	private RelativeLayout remain_layout;
	private Button remain_hide;
	private Button remain_all;
	private Button remain_ret;
	private GridView gridView;
	private VideoAdapter videoAdapter;
	private Boolean videoOk = false;
	private String sendUrl;

	private ZhiChangListData zhaopinListData;
	private ZhiChangListData jianzhiListData;
	private ZhiChangListAdapter adapter;
	private String url = "";
	private String state = "official";
	private int iflag = 0;
	private SharedPreferences sp;

	private void onLoad() {

		listView.stopRefresh();
		listView.stopLoadMore();
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy年MM月dd日    HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String time = formatter.format(curDate);
		listView.setRefreshTime(time);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.work_activity);

		zhaopinListData = new ZhiChangListData();
		jianzhiListData = new ZhiChangListData();

		context = this;
		datas = new RemenData(getApplicationContext());

		titletype = (LinearLayout) findViewById(R.id.active_titletype);
		ac_video = (LinearLayout) findViewById(R.id.ac_video);
		ac_work = (LinearLayout) findViewById(R.id.ac_work);
		ac_type = (LinearLayout) findViewById(R.id.active_type);

		activebackbtn = (Button) findViewById(R.id.activebackbtn);
		btn1 = (Button) findViewById(R.id.content_btn1);
		btn2 = (Button) findViewById(R.id.content_btn2);
		btn3 = (Button) findViewById(R.id.work_item);
		btn4 = (Button) findViewById(R.id.video_item);
		remain_layout = (RelativeLayout) findViewById(R.id.remain_layout);
		remain_hide = (Button) findViewById(R.id.remain_hide);
		remain_all = (Button) findViewById(R.id.remain_all);
		remain_ret = (Button) findViewById(R.id.remain_ret);
		gridView = (GridView) findViewById(R.id.work_video);

		remain_layout.setOnClickListener(this);
		remain_hide.setOnClickListener(this);
		remain_all.setOnClickListener(this);
		remain_ret.setOnClickListener(this);
		activebackbtn.setOnClickListener(this);
		btn1.setOnClickListener(this);
		btn2.setOnClickListener(this);
		btn3.setOnClickListener(this);
		btn4.setOnClickListener(this);
		sp = getSharedPreferences("SP", MODE_PRIVATE);
		mac = sp.getString("mac", "null mac");

		listView = (XListView) findViewById(R.id.work_list);
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
		getListData("official");

	}

	private String token = "";
	private int officialpage = 1;
	private int personalpage = 1;

	private void getListData(final String type) {
		// TODO Auto-generated method stub
		// if (!dialog.isShowing()) {
		// dialog.setTitle("正在加载数据，请稍候...");
		// dialog.show();
		// dialog.setCanceledOnTouchOutside(false);
		// }

		if (type.equals("official")) {
			url = getString(R.string.url_work)
					+ "n=androiddp001&d=8a515e992833d2f1dcb8bec66a064255"
					+ "&page=" + officialpage + "&size=7" + "&type=full";
		} else {
			url = getString(R.string.url_work)
					+ "n=androiddp001&d=8a515e992833d2f1dcb8bec66a064255"
					+ "&page=" + personalpage + "&size=7" + "&type=part";
		}
		System.out.println(url);

		RequestQueue queue = Volley.newRequestQueue(this);
		StringRequest request = new StringRequest(url, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				// TODO Auto-generated method stub
				System.out.println(response);
				zhaopinListData.List_Data.clear();
				jianzhiListData.List_Data.clear();
				ParseJson parseJson = new ParseJson();
				try {
					if (type.equals("official")) {
						parseJson.parseZhiChangList(response,
								zhaopinListData.List_Data);
					} else {
						parseJson.parseZhiChangList(response,
								jianzhiListData.List_Data);
					}
					handler.sendEmptyMessage(456);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Toast.makeText(getApplicationContext(), "服务器异常，请稍后重试",
							Toast.LENGTH_LONG).show();
					// if (dialog.isShowing()) {
					// dialog.dismiss();
					// }
				}
			}

		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "网络异常，请返回重试", Toast.LENGTH_SHORT).show();
				// if (dialog.isShowing()) {
				// dialog.dismiss();
				// }
			}
		});
		queue.add(request);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.content_btn1:
			if (iflag == 1) {
				titletype.setBackgroundResource(R.drawable.zhichangxinxi_04);
				iflag = 0;
				state="official";
				getListData(state);
			}
			break;
		case R.id.content_btn2:
			if (iflag == 0) {
				iflag = 1;
				titletype.setBackgroundResource(R.drawable.zhichangxinxi_02);
				state="part";
				getListData(state);
			}

			break;
		case R.id.activebackbtn:
			Intent intent2 = new Intent();
			intent2.setClass(this, MainActivity.class);
			intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent2);
			break;
		case R.id.work_item:
			ac_type.setBackgroundResource(R.drawable.zhichangxinxi_06);
			ac_work.setVisibility(View.VISIBLE);
			ac_video.setVisibility(View.GONE);

			break;
		case R.id.video_item:
			ac_type.setBackgroundResource(R.drawable.zhichangxinxi_05);
			ac_video.setVisibility(View.VISIBLE);
			ac_work.setVisibility(View.GONE);
			String url = "http://data.iego.net:88/m/lessonlistJson.aspx?"
					+ "n=androiddp001&d=8a515e992833d2f1dcb8bec66a064255"
					+ "&size=27&page=1&androidcode=" + mac;
			try {
				sendUrl = url + "&cat=" + URLEncoder.encode("面试求职", "GB2312");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			if (!videoOk) {
				Ref(sendUrl, 123);
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
			intent.setClass(this, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case R.id.remain_ret:
			remain_layout.setVisibility(View.GONE);
			remain_hide.setVisibility(View.VISIBLE);
			intent.setClass(this, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		}

	}

	public Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 123:
				videoAdapter = new VideoAdapter(context, datas);
				gridView.setAdapter(videoAdapter);
				videoOk = true;
				gridView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						final String idString = datas.IMG_DESCRIPTIONS
								.get(position).vid;
						if (!(idString == null || idString.length() <= 0)) {
							// TODO 自动生成的方法存根
							Bundle bundle = new Bundle();
							Intent intent = new Intent();
							bundle.putString("id", idString);
							bundle.putString("sendUrl", sendUrl);
							intent.putExtras(bundle);
							intent.setClass(context, VideoContent.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(intent);
						}
					}

				});
				break;

			case 777:
				adapter.notifyDataSetChanged();
				onLoad();

				break;
			case 456:
				if (state.equals("official")) {
					adapter = new ZhiChangListAdapter(getApplicationContext(),
							zhaopinListData);
					adapter.setTypeflag(true);
					listView.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							// TODO Auto-generated method stub
							url = getString(R.string.url_workcontent)
									+ "n=androiddp001&d=8a515e992833d2f1dcb8bec66a064255"
									+ "&jobid="
									+ zhaopinListData.List_Data.get(arg2 - 1).id;
							System.out.println(url);
							Bundle bundle = new Bundle();
							bundle.putString("url", url);
							Intent intent = new Intent();
							intent.putExtras(bundle);
							intent.setClass(getApplicationContext(),
									WorkContent.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(intent);
						}
					});
				} else {
					adapter = new ZhiChangListAdapter(getApplicationContext(),
							jianzhiListData);
					adapter.setTypeflag(false);
					listView.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							// TODO Auto-generated method stub
							url = getString(R.string.url_workcontent)
									+ "n=androiddp001&d=8a515e992833d2f1dcb8bec66a064255"
									+ "&jobid="
									+ jianzhiListData.List_Data.get(arg2 - 1).id;
							System.out.println(url);
							Bundle bundle = new Bundle();
							bundle.putString("url", url);
							Intent intent = new Intent();
							intent.putExtras(bundle);
							intent.setClass(getApplicationContext(),
									WorkContent.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(intent);
						}
					});
				}
				listView.setAdapter(adapter);
				break;
			}
			super.handleMessage(msg);
		}

	};

	private boolean LoadMoreData() {
		final Boolean flag;
		if (state.equals("official")) {
			officialpage++;
			url = getString(R.string.url_work)
					+ "n=androiddp001&d=8a515e992833d2f1dcb8bec66a064255"
					+ "&page=" + officialpage + "&size=10" + "&type=full";
			flag = true;
		} else {
			personalpage++;
			url = getString(R.string.url_work)
					+ "n=androiddp001&d=8a515e992833d2f1dcb8bec66a064255"
					+ "&page=" + personalpage + "&size=10" + "&type=part";
			flag = false;
		}

		RequestQueue queue = Volley.newRequestQueue(this);
		StringRequest request = new StringRequest(url, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				// TODO Auto-generated method stub
				System.out.println(response);
				ParseJson parseJson = new ParseJson();
				try {
					if (response.indexOf("\"data\":[]") != -1) {
						Toast.makeText(getApplicationContext(), "暂无更多数据",
								Toast.LENGTH_SHORT).show();
						onLoad();
						if (state.equals("official")) {
							officialpage--;
						} else {
							personalpage--;
						}
					} else {
						if (flag) {
							parseJson.parseZhiChangList(response,
									zhaopinListData.List_Data);
						} else {
							parseJson.parseZhiChangList(response,
									jianzhiListData.List_Data);
						}
						handler.sendEmptyMessage(777);
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
				Toast.makeText(getApplicationContext(), "暂无更多数据",
						Toast.LENGTH_SHORT).show();
				onLoad();
				if (state.equals("official")) {
					officialpage--;
				} else {
					personalpage--;
				}
			}
		});
		queue.add(request);
		return true;
	}

	private void Ref(String kkurl, final int state) {
		RequestQueue queue = Volley.newRequestQueue(this);
		StringRequest request = new StringRequest(kkurl,
				new Listener<String>() {

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						System.out.println(response);

						ParseJson parseJson = new ParseJson();
						try {
							if (state == 123) {
								parseJson.parseRemenContent(
										datas.IMG_DESCRIPTIONS, response);
								Message message = new Message();
								message.what = 123;
								handler.sendMessage(message);
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
						Toast.makeText(getApplicationContext(), "网络异常，请返回重试", Toast.LENGTH_SHORT).show();
					}
				});
		queue.add(request);
	}

}
