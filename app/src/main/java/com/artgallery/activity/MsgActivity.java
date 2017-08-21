package com.artgallery.activity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

import com.artgallery.tool.Banner;
import com.artgallery.tool.EmergencyDBHelper;
import com.artgallery.tool.FullscreenDBHelper;
import com.artgallery.tool.MyTools;
import com.artgallery.wo.R;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

public class MsgActivity extends Activity {
	private String model = "";
	private int keepMod = 0;
	private FullscreenDBHelper fullscreenDBHelper;
	private int duration = 0;
	private String url1;
	private ArrayList<String> imgurl1;
	private ArrayList<String> imgurl2;
	private ArrayList<String> imgurl4;
	private ArrayList<String> linkurl4;
	private String videourl;
	private String url3;
	private String url5;
	private int progress = 0;
	private WebView webView;
	private VideoView myVideoView;
	private boolean Foregroundflag = true;
	private View dot0;
	private View dot1;
	private View dot2;
	private View dot3;
	private View dot4;
	private List<View> dots1; // 图片标题正文的那些点
	private List<View> dots2; // 图片标题正文的那些点
	private List<View> dots3; // 图片标题正文的那些点
	private ViewPager adViewPager;
	private ViewPager adViewPager2;
	private ViewPager adViewPager3;
	private Banner ban = new Banner();
	private Banner ban2 = new Banner();
	private Banner ban3 = new Banner();
	private Context context;
	private SharedPreferences sp;
	private Bundle jinjiBundle;
	private CloseMSGReceiver receiver;
	private JinjiMSGReceiver jinreceiver;

	private String econtent;
	boolean flag = false;
	int material_id = 0;

	private View xCustomView;
	private CustomViewCallback xCustomViewCallback;
	
	private Button msgret;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.msg_init);
		ActionBar bar = getActionBar();
		bar.hide();
		context = this;
		imgurl1 = new ArrayList<String>();
		imgurl2 = new ArrayList<String>();
		imgurl4 = new ArrayList<String>();
		linkurl4 = new ArrayList<String>();
		fullscreenDBHelper = new FullscreenDBHelper(getApplicationContext());
		sp = getSharedPreferences("SP", MODE_PRIVATE);
		jinjiBundle = getIntent().getExtras();

		receiver = new CloseMSGReceiver();
		jinreceiver = new JinjiMSGReceiver();
		IntentFilter filter = new IntentFilter("com.fy.closefull");
		IntentFilter filter2 = new IntentFilter("com.fy.jinji");
		registerReceiver(receiver, filter);
		registerReceiver(jinreceiver, filter2);

		Editor a = sp.edit();
		a.putBoolean("fullChange", false);
		a.commit();
		
		if (jinjiBundle == null) {
			task();
		} else {
			jinji();
		}
	}

	private void jinji() {
		Log.e("MsgActivity","jinji()执行");
		flag = false;
		Editor editore = sp.edit();
		editore.putBoolean("jinji", false);// 跳转紧急权限
		editore.putBoolean("msgjinji", true);//是否在紧急里
		editore.commit();
		setContentView(R.layout.msgsup);
		EmergencyDBHelper emergencyDBHelper = new EmergencyDBHelper(
				getApplicationContext());
		Cursor cursor2 = emergencyDBHelper.select(null, null, null);

		if (cursor2.moveToNext()) {
			econtent = cursor2.getString(cursor2.getColumnIndex("content"));
			TextView a = (TextView) findViewById(R.id.msg_sup);
			a.setText(econtent);
		} else {
		}
		cursor2.close();
	}

	private void task() {
		Log.e("MsgActivity","task()执行");
		Cursor cursor3 = fullscreenDBHelper.select(null, "material_id = ? ",
				new String[] { material_id + "" });

		if (cursor3.moveToNext()) {
			model = cursor3.getString(cursor3.getColumnIndex("model"));
			duration = Integer.parseInt(cursor3.getString(cursor3
					.getColumnIndex("duration")));
			cursor3.moveToPrevious();
		} else {
			timehandler.sendEmptyMessage(2);
		}

		// 拿所有的素材
		while (cursor3.moveToNext()) {
			flag = true;

			if (model.equals("1")) {

				url1 = cursor3.getString(cursor3.getColumnIndex("src"));

			} else if (model.equals("2")) {

				switch (cursor3.getInt(cursor3.getColumnIndex("areaid"))) {

				case 1:
					imgurl1.add(cursor3.getString(cursor3.getColumnIndex("src")));
					break;
				case 2:
					imgurl2.add(cursor3.getString(cursor3.getColumnIndex("src")));
					break;
				case 0:
					videourl = cursor3.getString(cursor3.getColumnIndex("src"));
				
					// 链接处理
					String downloadPath = "";
					if (MyTools.getExtSDCardPaths().size() > 0) {
						downloadPath = MyTools.getExtSDCardPaths().get(0)
								+ "/Android/data/com.fy.wo/msgdownload/";
					} else {
						downloadPath = Environment
								.getExternalStorageDirectory()
								.getAbsolutePath()
								+ "/Android/data/com.fy.wo/msgdownload/";
					}
					String[] ssss;
					ssss = videourl.split("/");
					videourl = downloadPath + ssss[ssss.length - 1];
					break;
				}

			} else if (model.equals("3")) {

				url3 = cursor3.getString(cursor3.getColumnIndex("src"));

			} else if (model.equals("4")) {

				imgurl4.add(cursor3.getString(cursor3.getColumnIndex("src")));
				linkurl4.add(cursor3.getString(cursor3.getColumnIndex("link")));

			} else if (model.equals("5")) {

				url5 = cursor3.getString(cursor3.getColumnIndex("src"));

			}

		}

		cursor3.close();

		if (flag) {
			// 根据素材显示控件
			
			if (model.equals("1")) {
				// 加载html
				setContentView(R.layout.msg_f1);
				msgret = (Button) findViewById(R.id.msgret);

				Log.e("MsgActivity","模板1执行");
				webView = (WebView) findViewById(R.id.msg_f1_web);
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						webset(webView, url1);
					}
				});
//				webset(webView, url1);
				keepMod = 1;

			} else if (model.equals("2")) {
				// 两个banner 一个视频
				setContentView(R.layout.msg_f2);
				msgret = (Button) findViewById(R.id.msgret);
				Log.e("MsgActivity","模板2执行");
				dots1 = new ArrayList<View>();
				dots2 = new ArrayList<View>();
				dot0 = findViewById(R.id.v1_dot0);
				dot1 = findViewById(R.id.v1_dot1);
				dot2 = findViewById(R.id.v1_dot2);
				dot3 = findViewById(R.id.v1_dot3);
				dot4 = findViewById(R.id.v1_dot4);
				dots1.add(dot0);
				dots1.add(dot1);
				dots1.add(dot2);
				dots1.add(dot3);
				dots1.add(dot4);
				dot0 = findViewById(R.id.v2_dot0);
				dot1 = findViewById(R.id.v2_dot1);
				dot2 = findViewById(R.id.v2_dot2);
				dot3 = findViewById(R.id.v2_dot3);
				dot4 = findViewById(R.id.v2_dot4);
				dots2.add(dot0);
				dots2.add(dot1);
				dots2.add(dot2);
				dots2.add(dot3);
				dots2.add(dot4);

				adViewPager = (ViewPager) findViewById(R.id.msg_vp1);
				ban.banner(getApplicationContext(), imgurl1, adViewPager);
				ban.startAd();
				adViewPager2 = (ViewPager) findViewById(R.id.msg_vp2);
				ban2.banner(getApplicationContext(), imgurl2, adViewPager2);
				ban2.startAd();
				// webView = (WebView) findViewById(R.id.msg_f2_video);
				// webset(webView, videourl);
				// webset(webView, video);
				videoset(videourl);

				System.out.println("videoLoad+" + videourl);
				keepMod = 2;
			} else if (model.equals("3")) {
				// 加载html
				setContentView(R.layout.msg_f3);
				msgret = (Button) findViewById(R.id.msgret);
				Log.e("MsgActivity","模板3执行");
				webView = (WebView) findViewById(R.id.msg_f3_web);
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						webset(webView, url3);
					}
				});
				keepMod = 3;
			} else if (model.equals("4")) {
				// 一个banner
				setContentView(R.layout.msg_f4);
				msgret = (Button) findViewById(R.id.msgret);
				Log.e("MsgActivity","模板4执行");
				dots3 = new ArrayList<View>();
				dot0 = findViewById(R.id.v3_dot0);
				dot1 = findViewById(R.id.v3_dot1);
				dot2 = findViewById(R.id.v3_dot2);
				dot3 = findViewById(R.id.v3_dot3);
				dot4 = findViewById(R.id.v3_dot4);
				dots3.add(dot0);
				dots3.add(dot1);
				dots3.add(dot2);
				dots3.add(dot3);
				dots3.add(dot4);
				adViewPager3 = (ViewPager) findViewById(R.id.msg_vp3);
				ban3.banner(getApplicationContext(), imgurl4, adViewPager3);
				ban3.startAd();
				keepMod = 4;
			} else if (model.equals("5")) {
				// 加载html
				setContentView(R.layout.msg_f53);
				msgret = (Button) findViewById(R.id.msgret);
				Log.e("MsgActivity","模板5执行");
				webView = (WebView) findViewById(R.id.msg_f5_web);
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						webset(webView, url5);
					}
				});

				keepMod = 5;
			}

			// 根据duration延迟
			msgret.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					finish();
					Intent intent2 = new Intent();
					intent2.setClass(MsgActivity.this, MainActivity.class);
					intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent2);
				}
			});
			
			timehandler.sendEmptyMessage(1);

			// 下一个任务

		}
	}

	private void videoset(String videopath) {
		final String aa = videopath;
		// TODO Auto-generated method stub
		myVideoView = (VideoView) findViewById(R.id.msg_f2_videov);
		// videopath =
		// Environment.getExternalStorageDirectory().getAbsolutePath()
		// + "/ZHDJ/" + videoflag + ".mp4";

		// videopath = "android.resource://" + getPackageName() + "/"
		// + R.raw.help_avi;

		myVideoView.setVideoPath(videopath);
		myVideoView.requestFocus();

		// mMediaController = new MediaController(this);
		// myVideoView.setMediaController(mMediaController);
		myVideoView.start();
		myVideoView
				.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

					@Override
					public void onCompletion(MediaPlayer mp) {
						// 视频播放完后 循环播放
						// videopath = Environment.getExternalStorageDirectory()
						// .getAbsolutePath()
						// + "/ZHDJ/"
						// + videoflag
						// + ".mp4";

						myVideoView.setVideoPath(aa);
						myVideoView.start();

					}
				});

	}

	private void webset(final WebView web, String url) {
		web.resumeTimers();
		WebSettings ws = web.getSettings();
		web.clearCache(true);
		ws.setBuiltInZoomControls(true);// 隐藏缩放按钮
		ws.setSupportZoom(false);
		ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);// 排版适应屏幕
		ws.setUseWideViewPort(true);//
		ws.setLoadWithOverviewMode(true);
		// ws.setSavePassword(true);
		ws.setSaveFormData(true);// 保存表单数据
		ws.setJavaScriptEnabled(true);// js
		ws.setDomStorageEnabled(true);
		// ws.setSupportMultipleWindows(true);// 新加
		// ws.setPluginState(WebSettings.PluginState.ON);
		ws.setCacheMode(WebSettings.LOAD_NO_CACHE);
		web.setWebChromeClient(new myWebChromeClient());
		 ws.setRenderPriority(WebSettings.RenderPriority.HIGH);
		web.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
//				Toast.makeText(MsgActivity.this,"click",Toast.LENGTH_SHORT).show();
			}
		});
		web.setWebViewClient(new WebViewClient(){
			@Override
			public void onPageFinished(WebView view, String url) {
				web.performClick();
				super.onPageFinished(view, url);
			}
		});
//		web.setWebViewClient(new WebViewClient() {
//			@Override
//			public boolean shouldOverrideUrlLoading(WebView view, String url) {
//				view.loadUrl(url);
//				return false;
//			}
////
//			// autoplay when finished loading via javascript injection
//			@Override
//			public void onPageFinished(WebView view, String url) {
//				// view.loadUrl("javascript:(function() { var videos = document.getElementsByTagName('video'); for(var i=0;i<videos.length;i++){videos[i].play();}})()");
//				System.out.println("web load ok");
//				// view.performClick();// 控件模拟点击
//			}
//		});
		web.loadUrl(url);

	}

	public class myWebChromeClient extends WebChromeClient {
		private View xprogressvideo;

		// 播放网络视频时全屏会被调用的方法
		@Override
		public void onShowCustomView(View view, CustomViewCallback callback) {
		}

		// 视频播放退出全屏会被调用的
		@Override
		public void onHideCustomView() {
			if (xCustomView == null)// 不是全屏播放状态
				return;

			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			xCustomView.setVisibility(View.GONE);
			xCustomView = null;
			xCustomViewCallback.onCustomViewHidden();
			webView.setVisibility(View.VISIBLE);
		}

		// 视频加载时进程loading
		@Override
		public View getVideoLoadingProgressView() {
			if (xprogressvideo == null) {
				LayoutInflater inflater = LayoutInflater.from(MsgActivity.this);
				xprogressvideo = inflater.inflate(
						R.layout.video_loading_progress, null);
			}
			return xprogressvideo;
		}
	}

	private Handler timehandler = new Handler() {

		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {

			case 1:
				progress++;
				if (progress < duration) {
					Message msg2 = timehandler.obtainMessage();
					msg2.what = 1;
					timehandler.sendMessageDelayed(msg2, 1000);
				} else {
					// 达成计时
					
					progress = 0;
					flag = false;
					material_id++;
					msgfinish();
					imgurl1.clear();
					imgurl2.clear();
					imgurl4.clear();
					linkurl4.clear();
					if(sp.getBoolean("fullChange", false)){
						Intent intent = new Intent();
						intent.setClass(getApplicationContext(),
								MainActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);
					}else {
						task();
					}
					
					
				}
				break;
			case 2:
				flag = false;
				material_id = 0;
				progress = 0;
				imgurl1.clear();
				imgurl2.clear();
				imgurl4.clear();
				linkurl4.clear();
				task();
				break;

			}

		};
	};

	@Override
	protected void onDestroy() {

		super.onDestroy();
		msgfinish();
		ban.releaseImageViewResouce();
		ban2.releaseImageViewResouce();
		ban3.releaseImageViewResouce();
		ban.VpStop();
		ban2.VpStop();
		ban3.VpStop();
		timehandler.removeMessages(1);
				unregisterReceiver(receiver);
		unregisterReceiver(jinreceiver);
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		msgfinish();
//		unregisterReceiver(receiver);
//		unregisterReceiver(jinreceiver);
		System.out.println("MsgFinish");
		flag = false;
		timehandler.removeMessages(1);

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		msgfinish();
		flag = false;
		// ViewGroup view = (ViewGroup) getWindow().getDecorView();
		// view.removeAllViews();
		System.out.println("stop");

		if (!isAppOnForeground() && Foregroundflag) {
			// app 进入后台
			System.out.println("进入后台");
			finish();

			ActivityManager manager = (ActivityManager) getApplicationContext()
					.getSystemService(Context.ACTIVITY_SERVICE);
			List<RunningTaskInfo> task_info = manager.getRunningTasks(20);

			String className = "";
			for (int i = 0; i < task_info.size(); i++) {
				if ("com.fy.wo".equals(task_info.get(i).topActivity
						.getPackageName())) {
					System.out.println("后台  "
							+ task_info.get(i).topActivity.getClassName());

					className = "com.fy.activity.MainActivity";

					// 这里是指从后台返回到前台 前两个的是关键
					Intent intent = new Intent();
					try {
						intent.setClass(getApplicationContext(),
								Class.forName(className));
						startActivity(intent);
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		}
	}

	private void msgfinish() {
		flag = false;
		if (keepMod == 1 || keepMod == 3 || keepMod == 5) {
			System.out.println("ban stop  moban1 3 5");
		} else if (keepMod == 2) {
			ban.VpStop();
			ban2.VpStop();
			// webset(webView, "about:blank");
			myVideoView.resume();// 暂停播放
			System.out.println("ban stop  moban2");
		} else if (keepMod == 4) {
			ban3.VpStop();
			System.out.println("ban stop  moban4");
		} else {

		}
	}

	/**
	 * 程序是否在前台运行
	 */
	public boolean isAppOnForeground() {
		// Returns a list of application processes that are running on the
		// device

		ActivityManager activityManager = (ActivityManager) getApplicationContext()
				.getSystemService(Context.ACTIVITY_SERVICE);
		String packageName = getApplicationContext().getPackageName();

		List<RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		if (appProcesses == null)
			return false;

		for (RunningAppProcessInfo appProcess : appProcesses) {
			// The name of the process that this object is associated with.
			if (appProcess.processName.equals(packageName)
					&& appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				return true;
			}
		}

		return false;
	}

	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	protected void onPause() {
		super.onPause();
		System.out.println("MsgOnPause");
		if (webView != null){
			webView.pauseTimers();
		}
		flag = false;
		MobclickAgent.onPause(this);
	}

	public boolean isForegroundflag() {
		return Foregroundflag;
	}

	public void setForegroundflag(boolean foregroundflag) {
		Foregroundflag = foregroundflag;
	}

	// 退出全屏接收器
	public class CloseMSGReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			finish();
			Intent intent2 = new Intent();
			intent2.setClass(MsgActivity.this, MainActivity.class);
			intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent2);

		}

	}

	// 紧急接收器
	public class JinjiMSGReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.e("MsgActivity","JinjiMSGReceiver执行");
			msgfinish();
			jinji();
		}

	}

}
