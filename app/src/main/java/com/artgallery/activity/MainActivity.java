package com.artgallery.activity;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.format.Time;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.artgallery.application.BaseActivity;
import com.artgallery.tool.Banner;
import com.artgallery.tool.Getmac;
import com.artgallery.tool.PermanentDBHelper;
import com.artgallery.tool.PreferencesUtils;
import com.artgallery.wo.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import static com.artgallery.wo.R.id.btn_dianzibaozhi;

public class MainActivity extends BaseActivity implements OnClickListener {

	private Context context;
	private ImageView btn1;
	private ImageView btn2;
	private ImageView btn3;
	private ImageView btn4;
	private ImageView btn5;
	private ImageView btn6;
	private ImageView btn7;
	private ImageView kcb;
	private ImageView guoxue;

	private TextView nianyue;
	private TextView shijian;
	private TextView xingqi;
	private String nianyues;
	private String shijians;
	private String xingqis;
	private TextView tianqi;
	private TextView wendu;
	private static String mMonth;
	private static String mDay;
	private static String mWay;

	private static Bundle weatherBundle = new Bundle();
	private static Handler handler;
	private Editor editor;

	private View dot0;
	private View dot1;
	private View dot2;
	private View dot3;
	private View dot4;
	private List<View> dots; // 图片标题正文的那些点
	private ViewPager adViewPager;
	private Banner ban = new Banner();

//	private Intent timeService;
//	private Intent msgService;
	private String mac;
	private SharedPreferences sp;
	int imghas;
	private Boolean checkflag;

	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private String mData;
	public static final String INIT = "init";
	private String mRoute;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		//请求数据所有馆的；
		getData();
		context = this;
		sp = getSharedPreferences("SP", MODE_PRIVATE);
		if (sp.getString("mac", "").equals("")){
			mac = new Getmac(this).getLocalMacAddress();
			System.out.println("mac=" + mac);
			editor = sp.edit();
			editor.putString("mac", mac);
			editor.commit();
		}
		Intent open = new Intent();
		open.setAction("com.checkzyp.start");
		sendBroadcast(open);
//		msgService = new Intent(this, MsgService.class);
//		timeService = new Intent(this, TimeService.class);
//		switch (isServiceRunning()) {
//
//		case 3:
////			System.out.println("都在-停止重开");
////			this.stopService(msgService);
////			this.stopService(timeService);
//			System.out.println("开始启动");
//			this.startService(msgService);
//			this.startService(timeService);
//
//			break;
//		case 1:
//			this.startService(msgService);
//			System.out.println("开启msg");
//			break;
//		case 2:
//			this.startService(timeService);
//			System.out.println("开启time");
//			break;
//		case 0:
//			this.startService(timeService);
//			this.startService(msgService);
//			System.out.println("开启msg-time");
//			break;
//		}

		checkflag = true;
		nianyue = (TextView) findViewById(R.id.nianyue);
		shijian = (TextView) findViewById(R.id.shijian);
		xingqi = (TextView) findViewById(R.id.xingqi);
		tianqi = (TextView) findViewById(R.id.tianqi);
		wendu = (TextView) findViewById(R.id.wendu);

		 btn1 = (ImageView) findViewById(R.id.btn_benguanwenhua);
		 btn2 = (ImageView) findViewById(R.id.btn_huodong);
		btn3 = (ImageView) findViewById(R.id.btn_benguanziyuan);
		 btn4 = (ImageView) findViewById(R.id.btn_dianzibaozhi);
		btn5 = (ImageView) findViewById(R.id.btn_other);
		btn6 = (ImageView) findViewById(R.id.btn_zhongwaijingdian);
		btn7 = (ImageView) findViewById(R.id.btn_usehelp);
		ImageView ab = (ImageView) findViewById(R.id.mainbg);
		 setbg(btn1, 0);
		 setbg(btn2, 1);
		setbg(btn3, 2);
		 setbg(btn4, 3);
		setbg(btn5, 4);
		setbg(btn6, 5);
		// setbg(btn6, R.drawable.xiaoyuanzx);
		setbg(btn7, 6);
		setbg(ab, 7);
//		setbg(kcb, 8);
//		setbg(guoxue, 9);

		 btn1.setOnClickListener(this);
		 btn2.setOnClickListener(this);
		btn3.setOnClickListener(this);
		 btn4.setOnClickListener(this);
		btn5.setOnClickListener(this);
		btn6.setOnClickListener(this);
		btn7.setOnClickListener(this);
//		kcb.setOnClickListener(this);
//		guoxue.setOnClickListener(this);

		PermanentDBHelper permanentDBHelper = new PermanentDBHelper(context);
		Cursor cursor = permanentDBHelper.select(null, null, null);
		// banner的使用
		ArrayList<String> img = new ArrayList<String>();
//==========================不用网络加载=================================================================
//		imghas = sp.getInt("imghas", 0);
//
//		if (imghas == 0) {
//			permanentDBHelper.insert("0", 0, "drawable://"
//					+ R.drawable.banner05, "null");
//			permanentDBHelper.insert("0", 1, "drawable://"
//					+ R.drawable.banner04, "null2");
//			permanentDBHelper.insert("0", 2, "drawable://"
//					+ R.drawable.banner03, "null3");
//			permanentDBHelper.insert("0", 3, "drawable://"
//					+ R.drawable.banner02, "null4");
//			permanentDBHelper.insert("0", 4, "drawable://"
//					+ R.drawable.banner01, "null4");
//			editor.putInt("imghas", 1);
//			editor.commit();
//		}
//		// img.clear();
//		while (cursor.moveToNext()) {
//			Log.e("MainActivity","read img");
//			img.add(cursor.getString(cursor.getColumnIndex("image")));
//		}
//		cursor.close();
//		Log.e("MainActivity","imgSize"+img.size());
//==========================不用网络加载=================================================================
//=====================改用静态的图片加载======================================================================================
		String str[] = { "drawable://" + R.drawable.banner01,
				"drawable://" + R.drawable.banner02,
				"drawable://" + R.drawable.banner03,
				"drawable://" + R.drawable.banner04,
				"drawable://" + R.drawable.banner05};
		for(int i = 0;i<str.length;i++){
			img.add(str[i]);
		}
		dots = new ArrayList<View>();
		dot0 = findViewById(R.id.v_dot0);
		dot1 = findViewById(R.id.v_dot1);
		dot2 = findViewById(R.id.v_dot2);
		dot3 = findViewById(R.id.v_dot3);
		dot4 = findViewById(R.id.v_dot4);
		dots.add(dot0);
		dots.add(dot1);
		dots.add(dot2);
		dots.add(dot3);
		dots.add(dot4);
		adViewPager = (ViewPager) findViewById(R.id.vp);
		ban.banner(context, img, adViewPager);

		handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 000:
					// 时间
					StringData();
					nianyue.setText(nianyues);
					shijian.setText(shijians);
					xingqi.setText(xingqis);
					break;
				case 111:
					// 天气设置
					if (sp.getString("time", "flag").equals(nianyues)) {
					} else {
						weatherData();
					}
					tianqi.setText(weatherBundle.getString("state"));
					wendu.setText(weatherBundle.getString("temperature"));
					System.out.println(weatherBundle.getString("state")
							+ weatherBundle.getString("temperature")
							+ "-------------------------");
					break;
				case 222:
					// 从数据表里读出天气数据
					tianqi.setText(sp.getString("weather1", "err!!"));
					wendu.setText(sp.getString("weather2", "err!!"));
					System.out.println(weatherBundle.getString("state")
							+ weatherBundle.getString("temperature")
							+ "-------------------------2");
					break;

				case 888:
//					finish();
					Intent intent = new Intent(Intent.ACTION_MAIN);
					intent.addCategory(Intent.CATEGORY_HOME);
					startActivity(intent);
					System.exit(0);// 使虚拟机停止运行并退出程序
				case 99900:
					// btn3.performClick();
					break;
				}
			}
		};

		Thread thread = new Thread() {
			public void run() {
				// TODO Auto-generated method stub
				try {
					while (true) {
						handler.sendEmptyMessage(000);
						Thread.sleep(1000);
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		};

		thread.start();

		if (sp.getString("time", "flag").equals(nianyues)) {
			handler.sendEmptyMessage(222);
		} else {
			System.out.println("no weather");
			gethttp();

		}
		// getWeather("广州市");
		// sp = getSharedPreferences("SP", MODE_PRIVATE);

		// String IPaddress = sp.getString("IPaddress",
		// "http://192.168.20.160:8888");

//		不用开始设IP地址
//		String IPaddress = sp.getString("IPaddress", null);
//		if (IPaddress == null) {
//			System.out.println("ipad" + IPaddress);
//			setIPaddress();
//		}

	}

	private void weatherData() {

		if (weatherBundle != null) {
			editor.putString("time", nianyues);
			editor.putString("weather1", weatherBundle.getString("state"));
			editor.putString("weather2", weatherBundle.getString("temperature"));
			editor.commit();

		}
	}

	private void setIPaddress() {
		final EditText ip = new EditText(this);
		ip.setHint("例如：http://192.168.20.160:8088");
		// TODO Auto-generated method stub
		Dialog dialog = new AlertDialog.Builder(MainActivity.this)
				.setTitle("请设置网关地址").setView(ip)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						System.out.println(ip.getText().toString());
						Editor editor = sp.edit();
						String ipadd = ip.getText().toString();
						if (ipadd.equals("")) {
							ipadd = "http://192.168.20.160:8088";
						}
						editor.putString("IPaddress", ipadd);
						editor.commit();
					}
				})// 设置确定按钮
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
					}
				})// 设置取消按钮
				.create();
		dialog.show();
	}

	public void StringData() {
		final Calendar c = Calendar.getInstance();
		c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
		t.setToNow(); // 取得系统时间。
		String m;
		int hour = t.hour; // 0-23
		int minute = t.minute;
		if (minute < 10) {
			m = ":" + "0" + minute;
		} else {
			m = ":" + minute;
		}
		mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
		mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
		mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
		if ("1".equals(mWay)) {
			mWay = "天";
		} else if ("2".equals(mWay)) {
			mWay = "一";
		} else if ("3".equals(mWay)) {
			mWay = "二";
		} else if ("4".equals(mWay)) {
			mWay = "三";
		} else if ("5".equals(mWay)) {
			mWay = "四";
		} else if ("6".equals(mWay)) {
			mWay = "五";
		} else if ("7".equals(mWay)) {
			mWay = "六";
		}
		nianyues = mMonth + "月 " + mDay + "日 ";
		shijians = hour + m + "";
		xingqis = "星期" + mWay;

//		Thread a = new Thread() {
//			public void run() {
//				try {
//					sleep(5000);
//					handler.sendEmptyMessage(99900);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			};
//		};
//		a.start();

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		if (checkflag) {
			checkflag = false;
			switch (v.getId()) {
			 case R.id.btn_benguanwenhua://学习平台
				 intent.setClass(this, VideoActivity.class);
				 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				 startActivity(intent);
			 break;
			 case R.id.btn_huodong://活动
			 intent.setClass(this, ActiveActivity.class);
			 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			 startActivity(intent);
			 break;
			case R.id.btn_benguanziyuan://本馆资源
				intent.setClass(this, BenGuanZiYuanActivity.class);
//				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("roure", mRoute);
				intent.setAction(INIT);// 用于区分是初次加载的
				startActivity(intent);
//				Toast.makeText(getApplication(),"本馆资源",Toast.LENGTH_SHORT).show();
				break;
			case R.id.btn_dianzibaozhi://电子报纸
//				//图书荐购
//				intent.setClass(this, CultrueActivity.class);
//				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				intent.putExtra("type", "bookbuy");
//				startActivity(intent);
				intent.setClass(this, CultrueActivity.class);
				intent.putExtra("url", "http://data.iego.net:85/data/paperlist2.aspx?lib=paper_all");
				intent.putExtra("type", "newspaper");
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				break;
			case R.id.btn_other://其他馆资源
				// 特色库
//				intent.setClass(this, CultrueActivity.class);
//				intent.putExtra("url", "http://data.iego.net:85/book/realList2.aspx?code=88888888&lib=realbook&order=issueDate");
//				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				intent.putExtra("type", "features");
//				startActivity(intent);
//				Toast.makeText(getApplication(),"其他馆资源",Toast.LENGTH_SHORT).show();
				intent.setClass(this, OtherActivity.class);
				intent.putExtra("data",mData);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				break;
			case R.id.btn_zhongwaijingdian:
				// 悦读经典
				intent.setClass(this, BookActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				break;
			case R.id.btn_usehelp://使用帮助
				intent.setClass(this, HelpActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				break;
			}
			checkflag = true;
		}

	}

	private void setbg(ImageView v, int i) {

		String bgname[] = { "drawable://" + R.drawable.zhuyemian_02,
				"drawable://" + R.drawable.icon_activity,
				"drawable://" + R.drawable.icon_benguanziyuan,
				"drawable://" + R.drawable.icon_paper,
				"drawable://" + R.drawable.icon_taguanziyuan,
				"drawable://" + R.drawable.icon_zhongwaijingdian,
				"drawable://" + R.drawable.icon_help,
				"drawable://" + R.drawable.index_bg,};

		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT).cacheOnDisc(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(bgname[i], v, options);
	}

	public static class exitReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO 自动生成的方法存根

			if (intent.getAction().equals("com.fy.exit")) {

				handler.sendEmptyMessage(888);

			}
		}

	}

	@Override
	protected void onStop() {
		super.onStop();
		// 当Activity不可见的时候停止切换
//		ban.VpStop();

	}

	@Override
	protected void onPause() {
		super.onPause();
		ban.VpStop();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		 ban.startAd();
	}

	public void gethttp() {

		String url = "";
		// url = "http://php.weather.sina.com.cn/xml.php?city=" +
		// URLEncoder.encode("广州市","GB2312")
		// + "&password=DJOYnieT8234jlsK&day=0";
		url = "http://120.25.248.152:8989/weather?city="
				+ URLEncoder.encode("广州");
		System.out.println(url);
		RequestQueue queue = Volley.newRequestQueue(this);
		StringRequest request = new StringRequest(url, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				// TODO Auto-generated method stub
				System.out.println(response);

				try {
					JSONObject jsonObject = new JSONObject(response);
					if (jsonObject.getInt("errNum") == 0) {
						weatherBundle.putString("state", jsonObject
								.getJSONObject("retData").getString("weather"));
						weatherBundle.putString("temperature", jsonObject
								.getJSONObject("retData").getString("l_tmp")
								+ "~"
								+ jsonObject.getJSONObject("retData")
										.getString("h_tmp") + "℃");
						handler.sendEmptyMessage(111);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				System.out.println("天气解析异常");
			}
		});
		queue.add(request);

	}

	private int isServiceRunning() {
		ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		ArrayList<RunningServiceInfo> runningService = (ArrayList<RunningServiceInfo>) manager
				.getRunningServices(30);
		int k = 0;
		int m = 0;
		int f = 0;
		for (int i = 0; i < runningService.size(); i++) {
			String msg = "com.fy.service.MsgService";
			String time = "com.fy.service.TimeService";
			String service = runningService.get(i).service.getClassName()
					.toString();
			if (service.equals(time)) {
				m = 1;
				System.out.println("time");
			} else if (service.equals(msg)) {
				k = 2;
				System.out.println("msg");
			}
		}
		f = m + k;
		switch (f) {
		case 0:
			System.out.println("F: !MSG + !TIME");
			break;
		case 1:
			System.out.println("F: !MSG + TIME");
			break;
		case 2:
			System.out.println("F: MSG + !TIME");
			break;
		case 3:
			System.out.println("F: MSG + TIME");
			break;

		}

		return f;
	}

	private void getData() {
		//所有馆的
		String url = "http://data.iego.net:85/qyg/routejson.ashx?";
		OkHttpUtils
				.get()
				.url(url)
				.build()
				.execute(new StringCallback() {
					@Override
					public void onError(com.squareup.okhttp.Request request, Exception e) {

					}

					@Override
					public void onResponse(String response) {
						mData = response.toString();
//							gsonforResult(s);
						jsonArray(mData);
					}
				});
	}
	private void jsonArray(String s) {
		String danWei = sp.getString("DanWei", "");
		try {
			JSONArray array = new JSONArray(s);
			for(int i = 0;i<array.length();i++){
				JSONObject jsonObject = array.getJSONObject(i);//第一条数据
				if(danWei.equals(jsonObject.getString("route"))){
					mRoute = jsonObject.getString("server");
					PreferencesUtils.putString(getApplicationContext(),"server",mRoute);
				}
			}

//			PreferencesUtils.putString(getApplicationContext(),"server",server);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
