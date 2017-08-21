package com.artgallery.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.artgallery.application.BaseActivity;
import com.artgallery.wo.R;

public class DepartmentContent extends BaseActivity implements OnClickListener {
	private WebView webView;
	private View xCustomView;
	private ProgressDialog waitdialog = null;
	private CustomViewCallback xCustomViewCallback;
	private myWebChromeClient xwebchromeclient;
	private Button back;
	private RelativeLayout remain_layout;
	private Button remain_hide;
	private Button remain_all;
	private Button remain_ret;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.department_content);

		back = (Button) findViewById(R.id.de_webback);
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

		waitdialog = new ProgressDialog(this);
		waitdialog.setTitle("提示");
		waitdialog.setMessage("页面加载中...");
		waitdialog.setIndeterminate(true);
		waitdialog.setCancelable(true);
//		waitdialog.show();

		webView = (WebView) findViewById(R.id.net_webview);

		WebSettings ws = webView.getSettings();
		ws.setPluginState(PluginState.ON);
		
		ws.setBuiltInZoomControls(true);// 隐藏缩放按钮
		ws.setSupportZoom(false); 
		// ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);// 排版适应屏幕
		ws.setUseWideViewPort(true);// 可任意比例缩放 
		ws.setLoadWithOverviewMode(true);// setUseWideViewPort方法设置webview推荐使用的窗口。setLoadWithOverviewMode方法是设置webview加载的页面的模式。
		ws.setSavePassword(true);
		ws.setSaveFormData(true);// 保存表单数据
		ws.setJavaScriptCanOpenWindowsAutomatically(true);
		ws.setJavaScriptEnabled(true);
		ws.setDomStorageEnabled(true);
		ws.setSupportMultipleWindows(true);// 新加
		xwebchromeclient = new myWebChromeClient();
		webView.setWebChromeClient(xwebchromeclient);
		webView.setWebViewClient(new myWebViewClient());
//		Bundle bundle = this.getIntent().getExtras();
//		String url = bundle.getString("url");

		SharedPreferences sp;
		sp = getSharedPreferences("SP", MODE_PRIVATE);
		String url = sp.getString("IPaddress", "http://192.168.20.160:8000")
				+ getString(R.string.url_department);
		webView.loadUrl(url);
		
//		webView.loadUrl("http://192.168.20.160:8088/cirmSystem/vWeb/jsp/xyzx.jsp");

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
			xCustomView = view;
			xCustomViewCallback = callback;
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
				LayoutInflater inflater = LayoutInflater
						.from(DepartmentContent.this);
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

		if (isFinishing()) {
			webView.loadUrl("about:blank");
			setContentView(new FrameLayout(this));
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
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
				DepartmentContent.this.finish();
			}
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.de_webback:
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
			intent.setClass(this, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);

			break;
		case R.id.remain_ret:
			remain_layout.setVisibility(View.GONE);
			remain_hide.setVisibility(View.VISIBLE);
			finish();
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
