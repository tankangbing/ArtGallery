package com.artgallery.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.artgallery.adapter.HuoDongListAdapter;
import com.artgallery.application.BaseActivity;
import com.artgallery.data.HuoDongListData;
import com.artgallery.tool.ParseJson;
import com.artgallery.tool.XListView;
import com.artgallery.tool.XListView.IXListViewListener;
import com.artgallery.wo.R;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ActiveActivity extends BaseActivity implements OnClickListener, OnGestureListener{
	private Button back;
	XListView gridView;
	private int officialpage = 1;
	
	GestureDetector detector;

	private SharedPreferences sp;

	private final static int GetMoreData = 222;
	private final static int REFRESH = 333;
	private final static int PULL_REFRESH = 444;

	private HuoDongListData officialListData;
	private HuoDongListAdapter adapter;
	private String url;

	private RelativeLayout remain_layout;
	private Button remain_hide;
	private Button remain_all;
	private Button remain_ret;
	private String mac;

	public Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ActiveActivity.GetMoreData:
				adapter.notifyDataSetChanged();
				onLoad();
				break;
			case ActiveActivity.REFRESH:
				adapter = new HuoDongListAdapter(getApplicationContext(),
						officialListData);
				gridView.setAdapter(adapter);
				gridView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
							long arg3) {
						// TODO Auto-generated method stub
						url = getString(R.string.url_activeContent)
								+ "&n="+sp.getString("UserName", "")
								+ "&d="+sp.getString("KEY", "")
								+ "&actid=" + officialListData.List_Data.get(arg2 - 1).id;
						System.out.println("详情：" + url);
						Bundle bundle = new Bundle();
						bundle.putString("url", url);
						bundle.putString("id",
								officialListData.List_Data.get(arg2 - 1).id);

						Intent intent = new Intent();
						intent.putExtras(bundle);
						intent.setClass(ActiveActivity.this, ActivityContent.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
					}
				});
				break;
			case ActiveActivity.PULL_REFRESH:

				break;
			}
			super.handleMessage(msg);
		}

	};

	private void onLoad() {
		gridView.stopRefresh();
		gridView.stopLoadMore();
//		SimpleDateFormat formatter = new SimpleDateFormat(
//				"yyyy年MM月dd日    HH:mm:ss");
//		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
//		String time = formatter.format(curDate);
//		listView.setRefreshTime(time);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.active_activity);
		sp = getSharedPreferences("SP", MODE_PRIVATE);
		detector = new GestureDetector(this,this);
		mac = sp.getString("mac", "null mac");
		officialListData = new HuoDongListData();

		url = getString(R.string.url_active)
				+ "n=androiddp001&d=8a515e992833d2f1dcb8bec66a064255&"
				+ "&size=7&page=" + officialpage + "&androidcode=" + mac;
		back = (Button) findViewById(R.id.activebackbtn);
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

		gridView = (XListView) findViewById(R.id.active_mainlist);
		gridView.setPullLoadEnable(true);
		gridView.setPullRefreshEnable(false);
		gridView.setXListViewListener(new IXListViewListener() {

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

	private void getListData(final String type) {
		// TODO Auto-generated method stub
		String danweis = sp.getString("DanWei", "");
		try {
			url = getString(R.string.url_active)
                    + "&unit=" + URLEncoder.encode(danweis, "UTF-8")
                    + "&n=" + sp.getString("UserName", "")
                    + "&d=" + sp.getString("KEY", "")
                    + "&page=" + officialpage;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		System.out.println(url);

		RequestQueue queue = Volley.newRequestQueue(this);
		StringRequest request = new StringRequest(url, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				// TODO Auto-generated method stub
				System.out.println(response);
				
				
				try {
					JSONObject job = new JSONObject(response);
					if (job.getString("data").equals("[]")) {
						Toast.makeText(getApplicationContext(), "暂无更多数据",
								Toast.LENGTH_SHORT).show();
						onLoad();
						officialpage--;
					}else {
						ParseJson parseJson = new ParseJson();
						parseJson.parseHuoDongList(response,
								officialListData.List_Data);
						handler.sendEmptyMessage(ActiveActivity.REFRESH);
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.activebackbtn:
//			intent.setClass(this, MainActivity.class);
//			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(intent);
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
		case R.id.remain_ret:
			remain_layout.setVisibility(View.GONE);
			remain_hide.setVisibility(View.VISIBLE);
//			intent.setClass(this, MainActivity.class);
//			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(intent);
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

	private boolean LoadMoreData() {
		final Boolean flag;
		officialpage++;
		try {
			url = getString(R.string.url_active)
                    + "&n=" + sp.getString("UserName", "")
                    + "&d=" + sp.getString("KEY", "")
                    +"&unit=" +URLEncoder.encode(sp.getString("DanWei", ""), "UTF-8")
                    + "&page=" + officialpage;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		flag = true;

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
						parseJson.parseHuoDongList(response,
								officialListData.List_Data);
						handler.sendEmptyMessage(ActiveActivity.GetMoreData);
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
	
	//将该activity上的触碰事件交给GestureDetector处理
    public boolean onTouchEvent(MotionEvent me){
    	return detector.onTouchEvent(me);
    }

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * 滑屏监测
	 * 
	 */
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		float minMove = 120;         //最小滑动距离
		float minVelocity = 0;      //最小滑动速度
		float beginX = e1.getX();     
		float endX = e2.getX();
		float beginY = e1.getY();     
		float endY = e2.getY();
		
		if(beginX-endX>minMove&&Math.abs(velocityX)>minVelocity){   //左滑
//			Toast.makeText(this,velocityX+"左滑",Toast.LENGTH_SHORT).show();
			if(officialpage > 1){
				officialpage--;
			}
			getListData("official");
		}else if(endX-beginX>minMove&&Math.abs(velocityX)>minVelocity){   //右滑
//			Toast.makeText(this,velocityX+"右滑",Toast.LENGTH_SHORT).show();
			officialpage++;
			getListData("official");
		}else if(beginY-endY>minMove&&Math.abs(velocityY)>minVelocity){   //上滑
//			Toast.makeText(this,velocityX+"上滑",Toast.LENGTH_SHORT).show();
		}else if(endY-beginY>minMove&&Math.abs(velocityY)>minVelocity){   //下滑
//			Toast.makeText(this,velocityX+"下滑",Toast.LENGTH_SHORT).show();
		}
		
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		unbindDrawables(gridView);
		super.onDestroy();
	}
}
