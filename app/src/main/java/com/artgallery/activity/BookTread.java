package com.artgallery.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
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
import android.widget.ScrollView;
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

public class BookTread extends BaseActivity implements OnClickListener {
	private String url;
	private String turl;
	private String kkurl;
	private String oldurl;
	private String newurl;

	private ArrayList<String> datas;
	private String[] typeflag;
	private int ss;

	private Boolean clickableBoolean = true;

	private Context context;
	private EditText suosuoText;
	private Button moreButton;
	private String mac;
	private Button ret;
	private Button back;
	private Button find;
	private int testType;
	private int page;
	private String idString;
	private String sBitmap;
	private String resp;
	
	private Button uppage;
	private Button downpage;
	private Button ttxt;
	private Button tpic;

	private final static int GETNULL = 000;
	private final static int GETDATE = 001;
	private final static int REF = 002;

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
		setContentView(R.layout.book_tread);
		Bundle bundle = this.getIntent().getExtras();
		idString = bundle.getString("id");
		context = this;

		
		ret = (Button) findViewById(R.id.bookt_back);
		back = (Button) findViewById(R.id.backt);
		find = (Button) findViewById(R.id.ss_btn);
		uppage = (Button) findViewById(R.id.uppage);
		downpage = (Button) findViewById(R.id.downpage);
		moreButton = (Button) findViewById(R.id.more_btn);
		suosuoText = (EditText) findViewById(R.id.suosuo_edit);
		ttxt = (Button) findViewById(R.id.test_txt);
		tpic = (Button) findViewById(R.id.test_pic);

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
		uppage.setOnClickListener(this);
		downpage.setOnClickListener(this);
		ttxt.setOnClickListener(this);
		tpic.setOnClickListener(this);
		suosuoText.setImeOptions(EditorInfo.IME_ACTION_DONE);
		
		testType = 1;
		ss = 0;
		page = 1;
		sp = getSharedPreferences("SP", MODE_PRIVATE);
		mac = sp.getString("mac", "null mac");
		turl = "http://data.iego.net:85/book/pageEpubjson.aspx?lib=classic&code="
				+ mac + "&id=" + idString + "&chapter=" + page;

		
		url = "http://data.iego.net:85/m/bookInfoJson.aspx?"
				+ "n=androiddp001&d=8a515e992833d2f1dcb8bec66a064255&"
				+ "lib=classic&id=" + idString + "&androidcode=" + mac;
		typeflag = new String[] { "文学", "哲学、宗教", "历史、地理", "医药、卫生", "军事",
				"政治、法律", "艺术", "" };

		gView = (GridView) findViewById(R.id.bookt_topgrid);
		gView.setAdapter(new TopGridAdapter(getApplicationContext(), 0,bundle.getInt("typepos", 0)));
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
		// TODO 自动生成的方法存根
		gethttp(url);
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
							message.what = BookTread.REF;
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
			case BookTread.REF:
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
				ImageView erweima = (ImageView) findViewById(R.id.erweima);

				booknameTextView.setText(datas.get(0));
				authorTextView.setText(datas.get(1));

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
				// Ref(turl);
				refpic(turl);
				break;
			case BookTread.GETNULL:
				Toast.makeText(BookTread.this, "搜索内容不存在", Toast.LENGTH_SHORT)
						.show();
				break;
			case BookTread.GETDATE:
				TextView ttxt = (TextView) findViewById(R.id.ttxt_c); // 文本内容
				ScrollView tsc = (ScrollView) findViewById(R.id.txt_sc);
				ttxt.setText(Html.fromHtml(resp));
				tsc.scrollTo(0, 0);
				break;

			}
			super.handleMessage(msg);
			clickableBoolean = true;
		}

	};

	public void onClick(View v) {
		// TODO 自动生成的方法存根

		String kString;
		if (clickableBoolean) {
			Intent back = new Intent();
			Bundle backBundle = new Bundle();
			switch (v.getId()) {
			case R.id.ss_btn:

				kString = suosuoText.getText().toString();
				if (kString.equals("")) {
					Toast.makeText(BookTread.this, "请输入内容再搜索",
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
				Builder builder = new AlertDialog.Builder(BookTread.this);
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

			case R.id.bookt_back:
				finish();
//				back = new Intent();
//				backBundle = new Bundle();
//				backBundle.putString("id", idString);
//				backBundle.putString("listurl", getIntent().getExtras().getString("listurl", "null"));
//				backBundle.putInt("typepos", getIntent().getExtras().getInt("typepos", 0));
//				back.putExtras(backBundle);
//				back.setClass(context, BookContent.class);
//				back.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				startActivity(back);
				break;
			case R.id.backt:
				finish();
//				back = new Intent();
//				backBundle = new Bundle();
//				backBundle.putString("id", idString);
//				backBundle.putString("listurl", getIntent().getExtras().getString("listurl", "null"));
//				backBundle.putInt("typepos", getIntent().getExtras().getInt("typepos", 0));
//				back.putExtras(backBundle);
//				back.setClass(context, BookContent.class);
//				back.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				startActivity(back);
				break;
			case R.id.uppage:

				if (testType == 0) {
					oldurl = "http://data.iego.net:85/book/pdf2img2.aspx?lib=classic&code="
							+ mac + "&id=" + idString + "&p=";
				} else {
					oldurl = "http://data.iego.net:85/book/pageEpubjson.aspx?lib=classic&code="
							+ mac + "&id=" + idString + "&chapter=";
				}
				if (page > 1) {
					page -= 1;
					newurl = oldurl + page;
					clickableBoolean = false;
					refpic(newurl);
				} else {
					Toast.makeText(BookTread.this, "这已经是首页了",
							Toast.LENGTH_SHORT).show();
				}

				break;
			case R.id.downpage:

				if (testType == 0) {
					oldurl = "http://data.iego.net:85/book/pdf2img2.aspx?lib=classic&code="
							+ mac + "&id=" + idString + "&p=";
				} else {
					oldurl = "http://data.iego.net:85/book/pageEpubjson.aspx?lib=classic&code="
							+ mac + "&id=" + idString + "&chapter=";
				}
				if (page < 30) {

					page += 1;
					newurl = oldurl + page;
					clickableBoolean = false;
					refpic(newurl);
				} else {
					Toast.makeText(BookTread.this, "这已经是试读的最后一页了",
							Toast.LENGTH_SHORT).show();
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
				break;
			case R.id.remain_ret:
				remain_layout.setVisibility(View.GONE);
				remain_hide.setVisibility(View.VISIBLE);
				finish();
				break;

			default:
				break;
			}
		}
	}

	private void refpic(String uu) {

		ImageView tpic = (ImageView) findViewById(R.id.tpic_c);// 图片内容
		ImageView ttitle = (ImageView) findViewById(R.id.ttitle);// 标题
		ScrollView tsc = (ScrollView) findViewById(R.id.txt_sc);

		// 文本模式

		ttitle.setBackgroundResource(R.drawable.txtmo);
		tpic.setVisibility(View.GONE);
		tsc.setVisibility(View.VISIBLE);
		RequestQueue queue = Volley.newRequestQueue(this);
		System.out.println("文本url:" + uu);
		StringRequest request = new StringRequest(uu, new Listener<String>() {

			@Override
			public void onResponse(String response) {
				// TODO Auto-generated method stub
				// System.out.println(response);
				resp = response;
				Message message = new Message();
				message.what = BookTread.GETDATE;
				handler.sendMessage(message);

			}

		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				Toast.makeText(context, "网络异常，请返回重试", Toast.LENGTH_SHORT).show();
			}
		});
		queue.add(request);
		clickableBoolean = true;

	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		unbindDrawables(gView);
		super.onDestroy();
	}
}
