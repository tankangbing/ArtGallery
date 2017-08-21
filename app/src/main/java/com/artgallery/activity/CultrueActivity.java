package com.artgallery.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.artgallery.application.BaseActivity;
import com.artgallery.wo.R;

public class CultrueActivity extends BaseActivity implements OnClickListener {

	private WebView webView;
	private FrameLayout video_fullView;// 全屏时视频加载view
	private View xCustomView;
	private ProgressDialog waitdialog = null;
	private CustomViewCallback xCustomViewCallback;
	private myWebChromeClient xwebchromeclient;
	private RelativeLayout back;
	private RelativeLayout remain_layout;
	private Button remain_hide;
	private Button remain_all;
	private Button remain_ret;
	private String url;
	private Context context;
	private Boolean checkflag;
	private LinearLayout title_bar;
	private TextView title_text;
	private SharedPreferences sp;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.cultrue_activity);
		context = this;
		sp = getSharedPreferences("SP", MODE_PRIVATE);
//		Intent intent = new Intent();
//		intent = getIntent();
		video_fullView = new FrameLayout(getApplicationContext());
		url = "";
		Bundle bundle = this.getIntent().getExtras();
		if (bundle == null) {
			Toast.makeText(context, "数据加载失败，请返回重试", Toast.LENGTH_SHORT).show();
		}else {
			url = bundle.getString("url");
		}
		//==================7-26修改========================================
		title_text = (TextView)findViewById(R.id.title_text);
		//===========================================================
		// 悬浮按钮
		remain_layout = (RelativeLayout) findViewById(R.id.remain_layout);
		remain_hide = (Button) findViewById(R.id.remain_hide);
		remain_all = (Button) findViewById(R.id.remain_all);
		remain_ret = (Button) findViewById(R.id.remain_ret);
		back = (RelativeLayout) findViewById(R.id.back);
		back.setOnClickListener(this);
		remain_layout.setOnClickListener(this);
		remain_hide.setOnClickListener(this);
		remain_all.setOnClickListener(this);
		remain_ret.setOnClickListener(this);
		checkflag = true;
		waitdialog = new ProgressDialog(this);
		waitdialog.setTitle("提示");
		waitdialog.setMessage("页面加载中...");
		waitdialog.setIndeterminate(true);
		waitdialog.setCancelable(true);
		waitdialog.show();
		title_bar = (LinearLayout) findViewById(R.id.title_bar);
		webView = (WebView) findViewById(R.id.net_webview);

		WebSettings ws = webView.getSettings();
		ws.setBuiltInZoomControls(true);// 隐藏缩放按钮
		ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);// 排版适应屏幕

		ws.setUseWideViewPort(true);
		ws.setSupportZoom(false); // 不可任意比例缩放

		ws.setLoadWithOverviewMode(true);// setUseWideViewPort方法设置webview推荐使用的窗口。setLoadWithOverviewMode方法是设置webview加载的页面的模式。

		ws.setSavePassword(true);
		ws.setSaveFormData(true);// 保存表单数据
		ws.setJavaScriptEnabled(true);// js
		ws.setDomStorageEnabled(true);
		ws.setSupportMultipleWindows(true);// 新加
		xwebchromeclient = new myWebChromeClient();
		webView.setWebChromeClient(xwebchromeclient);
		webView.setWebViewClient(new myWebViewClient());
		// String url = Environment
		// .getExternalStorageDirectory().getAbsolutePath()
		// + "/xy/index.html";
		// webView.loadUrl("file:///mnt/sdcard/xy/xy/index.html");
//		System.out.println(url);
//		url = "http://p.bokecc.com/playvideo.bo?uid=3BF7C9738A6F67BC&playerid=93EA7068E8E68F87&playertype=2&autoStart=true&vid=32132ECC89C9AC689C33DC5901307461";

		String url = "";

		String type = getIntent().getStringExtra("type");
		if (type.equals("newbook")) {
			url = getIntent().getStringExtra("url");
			title_bar.setVisibility(View.VISIBLE);
			title_text.setText("新书推荐");
			back.setOnClickListener(this);
		}else if (type.equals("childbook")) {
			url = getIntent().getStringExtra("url");
			title_bar.setVisibility(View.VISIBLE);
			title_text.setText("童书推荐");
			back.setOnClickListener(this);
		}else if (type.equals("newspaper")){
			url = getIntent().getStringExtra("url");
			title_bar.setVisibility(View.VISIBLE);
			title_text.setText("电子报纸");
			back.setOnClickListener(this);
		}else if (type.equals("famouspeople")){
			url = sp.getString("IPaddress", "http://192.168.1.99:9110")
					+ getString(R.string.url_famouspeople);
			title_bar.setVisibility(View.GONE);
		}else if (type.equals("bookbuy")){
			url = sp.getString("IPaddress", "http://192.168.1.99:9110")
					+ getString(R.string.url_bookbuy);
			title_bar.setVisibility(View.GONE);
		}else if (type.equals("features")){
			url = sp.getString("IPaddress", "http://192.168.1.99:9110")
					+ getString(R.string.url_features);
			title_bar.setVisibility(View.GONE);
		}
		webView.loadUrl(url);

		webView.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) { // 表示按返回键时的操作
						webView.goBack(); // 后退

						// webview.goForward();//前进
						return true; // 已处理
					}
				}
				return false;
			}
		});
	}

	public class myWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return false;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			waitdialog.dismiss();
		}
	}

	public class myWebChromeClient extends WebChromeClient {
		private View xprogressvideo;

		// 播放网络视频时全屏会被调用的方法
		@Override
		public void onShowCustomView(View view, CustomViewCallback callback) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			webView.setVisibility(View.INVISIBLE);
			// 如果一个视图已经存在，那么立刻终止并新建一个
			if (xCustomView != null) {
				callback.onCustomViewHidden();
				return;
			}
			video_fullView.addView(view);
			xCustomView = view;
			xCustomViewCallback = callback;
			video_fullView.setVisibility(View.VISIBLE);
		}

		// 视频播放退出全屏会被调用的
		@Override
		public void onHideCustomView() {
			if (xCustomView == null)// 不是全屏播放状态
				return;
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			xCustomView.setVisibility(View.GONE);
			video_fullView.removeView(xCustomView);
			xCustomView = null;
			video_fullView.setVisibility(View.GONE);
			xCustomViewCallback.onCustomViewHidden();
			webView.setVisibility(View.VISIBLE);
		}

		// 视频加载时进程loading
		@Override
		public View getVideoLoadingProgressView() {
			if (xprogressvideo == null) {
				LayoutInflater inflater = LayoutInflater.from(CultrueActivity.this);
				xprogressvideo = inflater.inflate(
						R.layout.video_loading_progress, null);
			}
			return xprogressvideo;
		}
	}

	/**
	 * 判断是否是全屏
	 *
	 * @return
	 */
	public boolean inCustomView() {
		return (xCustomView != null);
	}

	/**
	 * 全屏时按返加键执行退出全屏方法
	 */
	public void hideCustomView() {
		xwebchromeclient.onHideCustomView();
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	@Override
	protected void onResume() {
		super.onResume();
		super.onResume();
		webView.onResume();
		webView.resumeTimers();

		/**
		 * 设置为横屏
		 */
		if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		webView.onPause();
		webView.pauseTimers();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// video_fullView.removeAllViews();
		webView.clearCache(true);
		webView.stopLoading();
		webView.setWebChromeClient(null);
		webView.setWebViewClient(null);
		webView.destroy();
		webView = null;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (inCustomView()) {
				// webViewDetails.loadUrl("about:blank");
				hideCustomView();
				return true;
			} else {
				webView.loadUrl("about:blank");
				CultrueActivity.this.finish();
			}
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
			case R.id.back:
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
				if (checkflag) {
					checkflag = false;
					finish();
				}
				break;
			case R.id.remain_ret:
				remain_layout.setVisibility(View.GONE);
				remain_hide.setVisibility(View.VISIBLE);
				if (checkflag) {
					checkflag = false;
					finish();
				}
				break;
		}
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		ViewGroup view = (ViewGroup) getWindow().getDecorView();
		view.removeAllViews();
		super.finish();
	}
}
