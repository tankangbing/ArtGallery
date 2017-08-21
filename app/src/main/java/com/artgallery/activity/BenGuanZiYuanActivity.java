package com.artgallery.activity;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.artgallery.application.BaseActivity;
import com.artgallery.wo.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class BenGuanZiYuanActivity extends BaseActivity implements View.OnClickListener{

	private Context context;
	private WebView mWebView;
	private WebSettings mWebSettings;
	private RelativeLayout remain_layout;
	private Button remain_hide;
	private Button remain_all;
	private Button remain_ret;
	private Boolean checkflag;
	private SharedPreferences sp;
	private String mYu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView();
		initData();
	}

	private void initView() {
		setContentView(R.layout.activity_benguanziyuan);
		mWebView = (WebView)findViewById(R.id.pingbi_web);
		remain_layout = (RelativeLayout) findViewById(R.id.remain_layout);
		remain_hide = (Button) findViewById(R.id.remain_hide);
		remain_all = (Button) findViewById(R.id.remain_all);
		remain_ret = (Button) findViewById(R.id.remain_ret);
		checkflag = true;
		remain_hide.setOnClickListener(this);
		remain_all.setOnClickListener(this);
		remain_ret.setOnClickListener(this);

		mWebView.clearCache(true);
	}

	private void initData() {
		context = this;
		this.sp = context.getSharedPreferences("SP", Context.MODE_PRIVATE);
		String action = getIntent().getAction();
		if (OtherActivity.UPDATE.equals(action)) {//他馆传过来的
			Bundle bundle = this.getIntent().getExtras();
			if (bundle == null) {
				Toast.makeText(getApplicationContext(), "数据加载失败，请返回重试", Toast.LENGTH_SHORT).show();
			}else {
				mYu = bundle.getString("yuming","");
			}
		}else if(MainActivity.INIT.equals(action)){//首次加载
			Bundle bundle = this.getIntent().getExtras();
			if (bundle == null) {
				Toast.makeText(getApplicationContext(), "数据加载失败，请返回重试", Toast.LENGTH_SHORT).show();
			}else {
				mYu = bundle.getString("roure","");
			}
		}

//		ActionBar actionBar = getActionBar();
//		actionBar.hide();

		mWebSettings = mWebView.getSettings();

//如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
		mWebSettings.setJavaScriptEnabled(true);

//设置自适应屏幕，两者合用
		mWebSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
		mWebSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

//缩放操作
		mWebSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
		mWebSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
		mWebSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

//其他细节操作
		mWebSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
		mWebSettings.setAllowFileAccess(true); //设置可以访问文件
		mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
		mWebSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
		mWebSettings.setDefaultTextEncodingName("utf-8");//设置编码格式



		//单位
		String userName = null;

		try {
			//URLEncoder.encode(sp.getString("DanWei", "") 是用中文转为字符串，
			userName = URLEncoder.encode(sp.getString("DanWei", ""), "UTF-8");

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String httpUrl = mYu + "/FrontDisplay/Index/HomePage?bName="+userName+"&module=bgwh&f=all";

//		String httpUrl = "http://116.22.53.92:8888/FrontDisplay/Index/HomePage?bName=%u4E2D%u5FC3%u9986&module=bgwh&f=all";
		mWebView.loadUrl(httpUrl);
		mWebView.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
			mWebView.goBack(); //goBack()表示返回WebView的上一页面
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	protected void onDestroy() {
		if (mWebView != null) {
			mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
			mWebView.clearHistory();

			((ViewGroup) mWebView.getParent()).removeView(mWebView);
			mWebView.destroy();
			mWebView = null;
		}
		super.onDestroy();
	}
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
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
				mWebView.goBack();//返回上一级
				if (checkflag) {
					checkflag = false;
//                    finish();
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
