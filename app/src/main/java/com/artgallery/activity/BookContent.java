package com.artgallery.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import android.graphics.Bitmap;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.artgallery.adapter.TopGridAdapter;
import com.artgallery.application.BaseActivity;
import com.artgallery.tool.ParseJson;
import com.artgallery.wo.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class BookContent extends BaseActivity implements OnClickListener {

	private Button ret;
	private Button back;
	private Button find;
	private Button tpic;
	private Context context;
	private EditText suosuoText;
	private Button moreButton;
	private String mac;

	
	private String url;
	private String kkurl;
	private String[] typeflag;

	private final static int GETNULL = 000;
	private final static int GETDATE = 001;
	private final static int REF = 002;

	private int ss;
	private String idString;
	private String sBitmap;
	private ArrayList<String> datas;
	private Boolean clickableBoolean = true;

	private RelativeLayout remain_layout;
	private Button remain_hide;
	private Button remain_all;
	private Button remain_ret;
	private GridView gView;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.book_content);

		Bundle bundle = this.getIntent().getExtras();
		idString = bundle.getString("id");
		context = this;
		gView = (GridView) findViewById(R.id.bookc_topgrid);
		ret = (Button) findViewById(R.id.bookc_ret);
		back = (Button) findViewById(R.id.bookc_back);
		find = (Button) findViewById(R.id.ss_btn);
		tpic = (Button) findViewById(R.id.test_pic);
		moreButton = (Button) findViewById(R.id.more_btn);
		suosuoText = (EditText) findViewById(R.id.suosuo_edit);

		// 悬浮按钮
		remain_layout = (RelativeLayout) findViewById(R.id.remain_layout);
		remain_hide = (Button) findViewById(R.id.remain_hide);
		remain_all = (Button) findViewById(R.id.remain_all);
		remain_ret = (Button) findViewById(R.id.remain_ret);
		remain_layout.setOnClickListener(this);
		remain_hide.setOnClickListener(this);
		remain_all.setOnClickListener(this);
		remain_ret.setOnClickListener(this);

		ret.setOnClickListener(this);
		back.setOnClickListener(this);
		find.setOnClickListener(this);
		moreButton.setOnClickListener(this);
		tpic.setOnClickListener(this);
		suosuoText.setImeOptions(EditorInfo.IME_ACTION_DONE);

		ss = 0;
		sp = getSharedPreferences("SP", MODE_PRIVATE);
		mac = sp.getString("mac", "null mac");
		url = "http://data.iego.net:85/m/bookInfoJson.aspx?"
				+ "n=androiddp001&d=8a515e992833d2f1dcb8bec66a064255&"
				+ "lib=classic&id=" + idString + "&androidcode=" + mac;
		
		
		
		System.out.println(url);
		typeflag = new String[] { "文学", "哲学、宗教", "历史、地理", "医药、卫生", "军事",
				"政治、法律", "艺术", "" };
		

		gView.setAdapter(new TopGridAdapter(getApplicationContext(),0,bundle.getInt("typepos", 0)));
		// 单击GridView元素的响应
		gView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Ref(typeflag[position]);

			}
		});
		

		init();
	}

	private void init() {
		// TODO Auto-generated method stub

		gethttp(url);
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		if (clickableBoolean) {
			String kString;
			Intent back = new Intent();
			Bundle backBundle = new Bundle();
			switch (v.getId()) {
			
			case R.id.ss_btn:

				kString = suosuoText.getText().toString();
				if (kString.equals("")) {
					Toast.makeText(BookContent.this, "请输入内容再搜索",
							Toast.LENGTH_SHORT).show();
				} else {
					clickableBoolean = false;
					ss = 1;
					Ref(kString);
				}
				break;
			case R.id.more_btn:
				final String[] strings = { "全部", "经济", "军事", "历史、地理",
						"马克思主义、列宁主义、毛泽东思想、邓小平理论", "农业科学", "社会科学总论", "数理科学和化学",
						"天文学、地球科学", "文学", "医药、卫生", "艺术", "哲学、宗教", "政治、法律",
						"综合性图书" };
				
				Builder builder = new AlertDialog.Builder(
						BookContent.this);
				builder.setTitle("请选择分类");
				builder.setSingleChoiceItems(strings, 0,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO 自动生成的方法存根
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

			case R.id.test_pic:
				totest();// 0图1字
				break;

			case R.id.bookc_back:
				finish();
//				back = new Intent();
//				backBundle = new Bundle();
//				backBundle.putString("listurl", getIntent().getExtras().getString("listurl", "null"));
//				backBundle.putInt("typepos", getIntent().getExtras().getInt("typepos", 0));
//				back.putExtras(backBundle);
//				back.setClass(context, BookActivity.class);
//				back.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				startActivity(back);
				break;
			case R.id.bookc_ret:
				finish();
//				back = new Intent();
//				backBundle = new Bundle();
//				backBundle.putString("listurl", getIntent().getExtras().getString("listurl", "null"));
//				backBundle.putInt("typepos", getIntent().getExtras().getInt("typepos", 0));
//				back.putExtras(backBundle);
//				back.setClass(context, BookActivity.class);
//				back.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				startActivity(back);
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
				finish();
				break;
				}
		}
		

	}

	private void Ref(String kString) {
		// TODO Auto-generated method stub
		Bundle bundle = new Bundle();
		Intent intent = new Intent();
		bundle.putString("kString", kString);
		bundle.putInt("kbool", ss);
		 
		intent.putExtras(bundle);
		intent.setClass(context, BookActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	public void gethttp(String httpurl) {
		RequestQueue queue = Volley.newRequestQueue(this);
		StringRequest request = new StringRequest(httpurl,
				new Listener<String>() {

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						System.out.println(response);
						ParseJson parseJson = new ParseJson();
						try {
							datas = parseJson.parseBookContent(response);
							Message message = new Message();
							message.what = BookContent.REF;
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
						Toast.makeText(context, "网络异常，请返回重试", Toast.LENGTH_SHORT).show();
					}
				});
		queue.add(request);
	}

	public Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case BookContent.REF:
				ImageLoader imageLoader = ImageLoader.getInstance();
				DisplayImageOptions options; // DisplayImageOptions是用于设置图片显示的类
				options = new DisplayImageOptions.Builder().cacheInMemory(true)
						.cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565)
						.build();
				System.out.println("接受handleMessage");
				// 录入数据

				ImageView view = (ImageView) findViewById(R.id.content_img);
				TextView booknameTextView = (TextView) findViewById(R.id.content_bookname);
				TextView authorTextView = (TextView) findViewById(R.id.content_author);
				TextView introTextView = (TextView) findViewById(R.id.content_intro);
				TextView dirTextView = (TextView) findViewById(R.id.content_dir);

				ImageView erweima = (ImageView) findViewById(R.id.erweima);

				booknameTextView.setText(datas.get(0));
				authorTextView.setText(datas.get(1));
				// 简介
				introTextView.setText(datas.get(5));
				// 目录
				dirTextView.setText(Html.fromHtml(datas.get(6)));

				imageLoader.displayImage(datas.get(3), view, options);

				try {
					sBitmap = "http://data.iego.net:85/qrcode/qrimg.aspx?str=BOOK|"
							+ mac
							+ "|classic*"
							+ idString
							+ "|"
							+ URLEncoder.encode(datas.get(0), "UTF-8");

				} catch (UnsupportedEncodingException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}

				imageLoader.displayImage(sBitmap, erweima, options);

				// 判断是否有文本试读
				if (datas.get(9).equals("1")) {
					tpic.setVisibility(view.VISIBLE);

				} else {
					tpic.setVisibility(view.GONE);
				}

				break;
			case BookContent.GETNULL:
				Toast.makeText(BookContent.this, "搜索内容不存在", Toast.LENGTH_SHORT)
						.show();
				break;

			}
			super.handleMessage(msg);
			clickableBoolean = true;
		}

	};
	private void totest() {
		Bundle bundle = new Bundle();
		Intent intent = new Intent();
		bundle.putString("id", idString);
		bundle.putString("listurl", getIntent().getExtras().getString("listurl", "null"));
		bundle.putInt("typepos", getIntent().getExtras().getInt("typepos", 0));
		intent.putExtras(bundle);
		intent.setClass(context, BookTread.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);

	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		unbindDrawables(gView);
		super.onDestroy();
	}
}
