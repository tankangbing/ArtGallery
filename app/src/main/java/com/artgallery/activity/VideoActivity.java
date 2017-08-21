package com.artgallery.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.artgallery.adapter.TopGridAdapter;
import com.artgallery.adapter.VideoAdapter;
import com.artgallery.application.BaseActivity;
import com.artgallery.data.RemenData;
import com.artgallery.tool.ParseJson;
import com.artgallery.wo.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class VideoActivity extends BaseActivity implements OnClickListener {
	private String url;
	private String kkurl;
	private RemenData datas;
	private EditText suosuoText;
	private Button moreButton;

	private String mac;

	private Button find;
	private Button remen;
	private Button zuixin;

	private Button ret;
	private Button startpage;
	private Button endpage;
	private Button uppage;
	private Button downpage;
	private String[] typeflag;
	private Context context;
	private Bundle bundle;
	private int page;
	private int countpage;
	private int count;

	private String flagurl = "";
	private String addurl = "";
	private String[] breakurl;
	private String newurl;
	private TextView allpage;
	private TextView howpage;
	private TextView thispage;
	private LinearLayout thepage;

	private Boolean clickableBoolean;
	private RelativeLayout remain_layout;
	private Button remain_hide;
	private Button remain_all;
	private Button remain_ret;

	private VideoAdapter videoAdapter;
	private TopGridAdapter a;
	private GridView gridView;
	private GridView gridView2;

	private final static int GETNULL = 000;
	private final static int GETDATE = 001;
	private final static int REF = 002;
	private SharedPreferences sp;
	int selectedItem = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_activity);
		context = this;

		find = (Button) findViewById(R.id.ss_btn_rm);
		moreButton = (Button) findViewById(R.id.more_btn_rmss);
		zuixin = (Button) findViewById(R.id.zuixin_btn_rm);
		remen = (Button) findViewById(R.id.remen_btn_rm);
		ret = (Button) findViewById(R.id.video_back);
		suosuoText = (EditText) findViewById(R.id.suosuo_edit_rm);
		startpage = (Button) findViewById(R.id.startpage);
		endpage = (Button) findViewById(R.id.endpage);
		uppage = (Button) findViewById(R.id.uppage);
		downpage = (Button) findViewById(R.id.downpage);
		thispage = (TextView) findViewById(R.id.thispage);
		howpage = (TextView) findViewById(R.id.howpage);
		allpage = (TextView) findViewById(R.id.allpage);
		thepage = (LinearLayout) findViewById(R.id.thebottom);
		gridView = (GridView) findViewById(R.id.video_grid);
		gridView2 = (GridView) findViewById(R.id.video_topgrid);

		// 悬浮按钮
		remain_layout = (RelativeLayout) findViewById(R.id.remain_layout);
		remain_hide = (Button) findViewById(R.id.remain_hide);
		remain_all = (Button) findViewById(R.id.remain_all);
		remain_ret = (Button) findViewById(R.id.remain_ret);
		remain_layout.setOnClickListener(this);
		remain_hide.setOnClickListener(this);
		remain_all.setOnClickListener(this);
		remain_ret.setOnClickListener(this);

		find.setOnClickListener(this);
		zuixin.setOnClickListener(this);
		remen.setOnClickListener(this);
		ret.setOnClickListener(this);
		moreButton.setOnClickListener(this);
		startpage.setOnClickListener(this);
		endpage.setOnClickListener(this);
		uppage.setOnClickListener(this);
		downpage.setOnClickListener(this);
		suosuoText.setImeOptions(EditorInfo.IME_ACTION_DONE);

		typeflag = new String[] { "美术类", "文学", "外语", "设计类", "安全教育", "音乐",
				"医学与健康", "励志人生" };
		sp = getSharedPreferences("SP", MODE_PRIVATE);
		mac = sp.getString("mac", "null mac");
		page = 1;
		url = "http://data.iego.net:88/m/lessonlistJson.aspx?"
				+ "n=androiddp001&d=8a515e992833d2f1dcb8bec66a064255"
				+ "&size=16&page=" + page + "&androidcode=" + mac;
		clickableBoolean = true;
		datas = new RemenData(getApplicationContext());
		bundle = getIntent().getExtras();
		a = new TopGridAdapter(context, 1, 0);
		gridView2.setAdapter(a);
		// 单击GridView元素的响应
		gridView2.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				try {
					addurl = url + "&cat="
							+ URLEncoder.encode(typeflag[position], "GB2312");
					//点到某个选项
					a.init(1, position);
					a.notifyDataSetChanged();
					gethttp(addurl, 1);

				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

		if (bundle == null) {
			try {
				addurl = url + "&cat=" + URLEncoder.encode("美术类", "GB2312");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			initData();
		} else {

			String listurl = bundle.getString("listurl", "null");

			if (!listurl.equals("null")) {
				addurl = listurl;
				initData();
			} else {
				resetData();
			}
		}

	}

	private void resetData() {
		// TODO Auto-generated method stub

	}

	private void initData() {
		// TODO 自动生成的方法存根
		gethttp(addurl, 0);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		if (clickableBoolean) {
			// datas.IMG_DESCRIPTIONS.clear();
			String kString;

			switch (v.getId()) {
			case R.id.video_back:
//				Intent intent2 = new Intent();
//				intent2.setClass(this, MainActivity.class);
//				intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				startActivity(intent2);
				finish();
				break;

			case R.id.zuixin_btn_rm:
				clickableBoolean = false;

				kkurl = addurl + "&order=dt";

				gethttp(kkurl, 1);
				break;
			case R.id.remen_btn_rm:
				clickableBoolean = false;

				kkurl = addurl + "&order=views";

				gethttp(kkurl, 1);
				break;

			case R.id.ss_btn_rm:

				kString = suosuoText.getText().toString();
				if (kString.equals("")) {
					Toast.makeText(VideoActivity.this, "请输入内容再搜索",
							Toast.LENGTH_SHORT).show();
				} else {

					clickableBoolean = false;
					try {
						kkurl = url + "&kwd="
								+ URLEncoder.encode(kString, "GB2312");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					// addurl = kkurl;
					gethttp(kkurl, 2);
				}
				break;

			case R.id.more_btn_rmss:

				final String[] strings = { "全部", "精选专题", "美术类", "设计类", "摄影与影视",
						"音乐", "戏剧艺术", "文学", "外语", "教育", "体育", "旅游", "家居生活",
						"IT与互联网", "游戏", "医学与健康", "数学与科学", "法律", "财务会计", "国际贸易",
						"市场营销", "经济和金融", "工商管理", "面试求职", "创业创新", "安全教育",
						"励志人生", "其它" };
				Builder builder = new AlertDialog.Builder(VideoActivity.this);
				builder.setTitle("请选择分类");
				builder.setSingleChoiceItems(strings, selectedItem,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								 selectedItem = which;
								if (which == 0) {
									strings[0] = "";
									gethttp(kkurl, 1);
									dialog.cancel();
								} else {
									try {
										kkurl = url
												+ "&cat="
												+ URLEncoder.encode(
														strings[which],
														"GB2312");
									} catch (UnsupportedEncodingException e) {
										e.printStackTrace();
									}
									addurl = kkurl;
									gethttp(kkurl, 1);
									dialog.cancel();
								}
								clickableBoolean = false;
							}

						});

				builder.setNegativeButton("取消", null);
				AlertDialog dialog = builder.create();
				dialog.show();

				break;

			case R.id.startpage:
				if (flagurl.equals("")) {
					flagurl = addurl;
				}
				if (page == 1) {

				} else {
					clickableBoolean = false;
					breakurl = flagurl.split("page=" + page);
					newurl = breakurl[0] + "page=" + "1" + breakurl[1];
					gethttp(newurl, 1);

				}
				break;
			case R.id.endpage:
				if (flagurl.equals("")) {
					flagurl = addurl;
				}
				if (page == countpage) {

				} else {
					clickableBoolean = false;
					breakurl = flagurl.split("page=" + page);
					newurl = breakurl[0] + "page=" + countpage + breakurl[1];
					gethttp(newurl, 1);
				}

				break;
			case R.id.uppage:
				if (flagurl.equals("")) {
					flagurl = addurl;
				}
				if (page > 1) {
					clickableBoolean = false;
					breakurl = flagurl.split("page=" + page);
					page -= 1;
					newurl = breakurl[0] + "page=" + page + breakurl[1];
					gethttp(newurl, 1);
				} else {
					Toast.makeText(VideoActivity.this, "这已经是首页了",
							Toast.LENGTH_SHORT).show();
				}

				System.out.println("newurl===" + newurl);
				break;
			case R.id.downpage:
				if (flagurl.equals("")) {
					flagurl = addurl;
				}
				if (page < countpage) {
					clickableBoolean = false;
					System.out.println(flagurl + "_____________");
					breakurl = flagurl.split("page=" + page);
					page += 1;
					newurl = breakurl[0] + "page=" + page + breakurl[1];
					gethttp(newurl, 1);
				} else {
					Toast.makeText(VideoActivity.this, "这已经是最后一页了",
							Toast.LENGTH_SHORT).show();
				}

				System.out.println("newurl===" + newurl);
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
//				Intent intent3 = new Intent();
//				intent3.setClass(this, MainActivity.class);
//				intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				startActivity(intent3);
				finish();
				break;
			}
		}

	}

	public void gethttp(final String httpurl, final int state) {
		/*
		 * 0 init 1 type 2 find 3 page
		 */
		RequestQueue queue = Volley.newRequestQueue(this);
		StringRequest request = new StringRequest(httpurl,
				new Listener<String>() {

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						System.out.println(response);

						if (response.indexOf("\"count\":\"0\"") == -1) {
								flagurl = httpurl;
							datas.IMG_DESCRIPTIONS.clear();
							try {
								JSONObject job = new JSONObject(response);
								count = job.getInt("count");
								countpage = job.getInt("pagecount");
								page = job.getInt("page");

								thispage.setText("第" + page + "页");
								howpage.setText(page + "/" + countpage + "页|");
								allpage.setText("共" + count + "条记录");

							} catch (JSONException e1) {
								// TODO 自动生成的 catch 块
								e1.printStackTrace();
							}

							ParseJson parseJson = new ParseJson();
							try {
								parseJson.parseRemenContent(
										datas.IMG_DESCRIPTIONS, response);
								// if (datas.IMG_DESCRIPTIONS.size() == 0) {
								// Message message = new Message();
								// message.what = VideoActivity.GETNULL;
								// handler.sendMessage(message);
								// } else {
								Message message = new Message();
								if (state == 0) {
									message.what = VideoActivity.GETDATE;// 初始化加载
								} else {
									message.what = VideoActivity.REF;// 不同分类加载
								}
								handler.sendMessage(message);
								// }
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} else {
							Message message = new Message();
							message.what = VideoActivity.GETNULL;
							handler.sendMessage(message);
						}

					}

				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						Toast.makeText(getApplicationContext(), "网络异常，请返回重试",
								Toast.LENGTH_SHORT).show();
					}
				});
		queue.add(request);

	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		unbindDrawables(gridView);
		unbindDrawables(gridView2);
		super.onDestroy();
	}

	public Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case VideoActivity.GETDATE:
				videoAdapter = new VideoAdapter(context, datas);
				gridView.setAdapter(videoAdapter);
				gridView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						System.out.println("???");
						final String idString = datas.IMG_DESCRIPTIONS
								.get(position).vid;
						if (!(idString == null || idString.length() <= 0)) {
							// TODO 自动生成的方法存根
							Bundle bundle = new Bundle();
							Intent intent = new Intent();
							bundle.putString("id", idString);
							bundle.putString("listurl", addurl);
							intent.putExtras(bundle);
							intent.setClass(context, VideoContent.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(intent);
						}
					}

				});
				clickableBoolean = true;
				break;

			case VideoActivity.GETNULL:
				flagurl = url;
				Builder builder = new AlertDialog.Builder(VideoActivity.this);
				builder.setTitle("搜索内容不存在");
				builder.setNegativeButton("确定", null);
				AlertDialog dialog = builder.create();
				dialog.show();
				clickableBoolean = true;

				break;
			case VideoActivity.REF:
			    if(videoAdapter!=null){
				videoAdapter.notifyDataSetChanged();
                }
				break;
			}
			super.handleMessage(msg);
			clickableBoolean = true;
		}

	};
}
