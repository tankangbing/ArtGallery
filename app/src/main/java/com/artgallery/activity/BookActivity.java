package com.artgallery.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
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
import com.artgallery.adapter.BookGirdAdapter;
import com.artgallery.adapter.TopGridAdapter;
import com.artgallery.application.BaseActivity;
import com.artgallery.data.ClassicBookListData;
import com.artgallery.tool.ParseJson;
import com.artgallery.wo.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class BookActivity extends BaseActivity implements OnClickListener {

	private Context context;
	private final static int GETNULL = 000;
	private final static int GETDATE = 001;
	private final static int REF = 002;
	private EditText suosuoText;
	private Button moreButton;
	private String mac;
	private TextView allpage;
	private TextView howpage;
	private TextView thispage;
	private LinearLayout thepage;
	private Button startpage;
	private Button endpage;
	private Button uppage;
	private Button downpage;
	private Button find;
	private Button ret;
	private int page;
	private int countpage;
	private int count;
	private ClassicBookListData datas;
	private BookGirdAdapter imageAdapter;
	private Bundle bundle;
	private String[] typeflag;
	private String url;
	private String nowUrl;
	private String kkurl;
	private String[] breakurl;
	private String newurl;
	private GridView gridView;
	private GridView gridView2;
	private RelativeLayout remain_layout;
	private Button remain_hide;
	private Button remain_all;
	private Button remain_ret;
	private Boolean click;
	private TopGridAdapter a;
	private SharedPreferences sp;
	private int typepos = 0;
	int selectedItem = 0;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.book_activity);
		context = this;
		datas = new ClassicBookListData();
		bundle = getIntent().getExtras();

		ret = (Button) findViewById(R.id.book_back);
		suosuoText = (EditText) findViewById(R.id.suosuo_edit);
		find = (Button) findViewById(R.id.ss_btn);
		moreButton = (Button) findViewById(R.id.more_btn);
		thispage = (TextView) findViewById(R.id.thispage);
		howpage = (TextView) findViewById(R.id.howpage);
		allpage = (TextView) findViewById(R.id.allpage);
		thepage = (LinearLayout) findViewById(R.id.thebottom);
		startpage = (Button) findViewById(R.id.startpage);
		endpage = (Button) findViewById(R.id.endpage);
		uppage = (Button) findViewById(R.id.uppage);
		downpage = (Button) findViewById(R.id.downpage);
		gridView = (GridView) findViewById(R.id.book_grid);
		gridView2 = (GridView) findViewById(R.id.book_topgrid);

		startpage.setOnClickListener(this);
		endpage.setOnClickListener(this);
		uppage.setOnClickListener(this);
		downpage.setOnClickListener(this);
		find.setOnClickListener(this);
		ret.setOnClickListener(this);
		moreButton.setOnClickListener(this);
		suosuoText.setImeOptions(EditorInfo.IME_ACTION_DONE);
		
		// 标题
		RelativeLayout title = (RelativeLayout) findViewById(R.id.book_act_title);
		setbg(title, R.drawable.yuedujingdian_01);

		// 悬浮按钮
		remain_layout = (RelativeLayout) findViewById(R.id.remain_layout);
		remain_hide = (Button) findViewById(R.id.remain_hide);
		remain_all = (Button) findViewById(R.id.remain_all);
		remain_ret = (Button) findViewById(R.id.remain_ret);
		remain_layout.setOnClickListener(this);
		remain_hide.setOnClickListener(this);
		remain_all.setOnClickListener(this);
		remain_ret.setOnClickListener(this);
		sp = getSharedPreferences("SP", MODE_PRIVATE);
		mac = sp.getString("mac", "null mac");
		page = 1;
		url = "http://data.iego.net:85/m/booklist1Json.aspx?"
				+ "n=androiddp001&d=8a515e992833d2f1dcb8bec66a064255&"
				+ "lib=classic&size=12&page=" + page + "&androidcode=" + mac;
		typeflag = new String[] { "文学", "哲学、宗教", "历史、地理", "马克思主义、列宁主义、毛泽东思想、邓小平理论", "数理科学和化学",
				"政治、法律", "艺术", "" };
		if (bundle == null) {
			a = new TopGridAdapter(context, 0, 0);
		}else{
			a = new TopGridAdapter(context, 0, bundle.getInt("typepos", 0));
		}
		gridView2.setAdapter(a);
		// 单击GridView元素的响应
		gridView2.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				try {
					kkurl = url + "&class1="
							+ URLEncoder.encode(typeflag[position], "UTF-8");
					typepos = position;
					a.init(0, position);
					a.notifyDataSetChanged();

					gethttp(kkurl, 1);

				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		click = true;

		if (bundle == null) {
			nowUrl = url;
			initData();
		} else {
			String listurl = bundle.getString("listurl", "null");
			if (!listurl.equals("null")) {
				nowUrl = listurl;
				System.out.println("listurl=" + listurl);
				initData();
			}else{
				resetData();
			}
		}
	}

	private void resetData() {
		// TODO Auto-generated method stub
		String kString = getIntent().getExtras().getString("kString");
		if (getIntent().getExtras().getInt("kbool") == 1) {
			try {
				nowUrl = url + "&kwd=" + URLEncoder.encode(kString, "UTF-8");
				System.out.println("搜索url= " + nowUrl);
			} catch (UnsupportedEncodingException e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			}
		} else {
			try {
				nowUrl = url + "&class1=" + URLEncoder.encode(kString, "UTF-8");
				System.out.println("分类url= " + nowUrl);
			} catch (UnsupportedEncodingException e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			}
		}
		gethttp(nowUrl, 0);
	}

	private void initData() {
		gethttp(nowUrl, 0);

	}

	public Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case BookActivity.GETDATE:
				// 录入数据
				thispage.setText("第" + page + "页");
				howpage.setText(page + "/" + countpage + "页|");
				allpage.setText("共" + count + "条记录");
				thepage.setVisibility(View.VISIBLE);
				imageAdapter = new BookGirdAdapter(context, datas);
				gridView.setAdapter(imageAdapter);
				// 单击GridView元素的响应
				gridView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {

						final String idString = datas.IMG_DESCRIPTIONS
								.get(position).bookid;
						System.out.println(position);

						if (!(idString == null || idString.length() <= 0)) {
							// TODO 自动生成的方法存根
							Bundle bundle = new Bundle();
							Intent intent = new Intent();
							bundle.putString("id", idString);
							bundle.putString("listurl", nowUrl);
							bundle.putInt("typepos", typepos);
							intent.putExtras(bundle);
							intent.setClass(context, BookContent.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(intent);
						}
					}
				});
				break;
			case BookActivity.GETNULL:
				Builder builder = new AlertDialog.Builder(BookActivity.this);
				builder.setTitle("搜索内容不存在");
				builder.setNegativeButton("确定", null);
				AlertDialog dialog = builder.create();
				dialog.show();
				nowUrl = url;
				break;
			case BookActivity.REF:
				// 刷新数据
				thispage.setText("第" + page + "页");
				howpage.setText(page + "/" + countpage + "页|");
				allpage.setText("共" + count + "条记录");
				thepage.setVisibility(View.VISIBLE);
				if (imageAdapter != null) {
					imageAdapter.notifyDataSetChanged();
				}else{
					imageAdapter = new BookGirdAdapter(context, datas);
					gridView.setAdapter(imageAdapter);
					// 单击GridView元素的响应
					gridView.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent, View view,
								int position, long id) {

							final String idString = datas.IMG_DESCRIPTIONS
									.get(position).bookid;
							System.out.println(position);

							if (!(idString == null || idString.length() <= 0)) {
								// TODO 自动生成的方法存根
								Bundle bundle = new Bundle();
								Intent intent = new Intent();
								bundle.putString("id", idString);
								bundle.putString("listurl", nowUrl);
								bundle.putInt("typepos", typepos);
								intent.putExtras(bundle);
								intent.setClass(context, BookContent.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(intent);
							}
						}
					});
				}
				click = true;
				break;
			}
			super.handleMessage(msg);
			// clickableBoolean = true;
		}

	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		String kString = "";
		if (click) {
			switch (v.getId()) {
			case R.id.ss_btn:
				kString = suosuoText.getText().toString();
				if (kString.equals("")) {
					Toast.makeText(BookActivity.this, "请输入内容再搜索",
							Toast.LENGTH_SHORT).show();
				} else {
					click = false;
					try {
						kkurl = url + "&kwd="
								+ URLEncoder.encode(kString, "UTF-8");
					} catch (UnsupportedEncodingException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
					gethttp(kkurl, 2);

				}
				break;
			case R.id.more_btn:
				final String[] a = { "全部", "经济", "军事", "历史、地理",
						"马克思主义、列宁主义、毛泽东思想、邓小平理论", "农业科学", "社会科学总论", "数理科学和化学",
						"天文学、地球科学", "文学", "医药、卫生", "艺术", "哲学、宗教", "政治、法律",
						"综合性图书" };
				Builder builder = new AlertDialog.Builder(BookActivity.this);
				builder.setTitle("请选择分类");
				builder.setSingleChoiceItems(a, selectedItem,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO 自动生成的方法存根
								selectedItem=which;
								click = false;
								if (which == 0) {
									a[0] = "";
									gethttp(url, 1);
									dialog.cancel();
								} else {
									try {
										kkurl = url
												+ "&class1="
												+ URLEncoder.encode(a[which],
														"UTF-8");
									} catch (UnsupportedEncodingException e) {
										// TODO 自动生成的 catch 块
										e.printStackTrace();
									}
									gethttp(kkurl, 1);
									dialog.cancel();
								}
							}
						});

				builder.setNegativeButton("取消", null);
				AlertDialog dialog = builder.create();
				dialog.show();

				break;
			case R.id.book_back:
				finish();
				click = false;
				break;
			case R.id.startpage:
				if (page == 1) {
				} else {
					click = false;
					breakurl = nowUrl.split("page=" + page);
					newurl = breakurl[0] + "page=" + "1" + breakurl[1];
					gethttp(newurl, 3);
				}
				break;
			case R.id.endpage:
				if (page == countpage) {
				} else {
					click = false;
					breakurl = nowUrl.split("page=" + page);
					newurl = breakurl[0] + "page=" + countpage + breakurl[1];
					gethttp(newurl, 3);
				}

				break;
			case R.id.uppage:
				if (page > 1) {
					click = false;
					breakurl = nowUrl.split("page=" + page);
					page -= 1;
					newurl = breakurl[0] + "page=" + page + breakurl[1];
					gethttp(newurl, 3);
				} else {
					Toast.makeText(BookActivity.this, "这已经是首页了",
							Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.downpage:
				if (page < countpage) {
					click = false;
					breakurl = nowUrl.split("page=" + page);
					page += 1;
					newurl = breakurl[0] + "page=" + page + breakurl[1];
					gethttp(newurl, 3);
				} else {
					Toast.makeText(BookActivity.this, "这已经是最后一页了",
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
				click = false;
				remain_layout.setVisibility(View.GONE);
				remain_hide.setVisibility(View.VISIBLE);
				Intent intent = new Intent();
				intent.setClass(this, MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				break;
			case R.id.remain_ret:
				click = false;
				remain_layout.setVisibility(View.GONE);
				remain_hide.setVisibility(View.VISIBLE);
				finish();
				break;
			}
		}
	}

	public void gethttp(final String httpurl, final int state) {
		/*
		 * 0 init 
		 * 1 type 
		 * 2 find 
		 * 3 page
		 */
		RequestQueue queue = Volley.newRequestQueue(this);
		StringRequest request = new StringRequest(httpurl,
				new Listener<String>() {

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						System.out.println(response);
						
						if (response.indexOf("\"count\":\"0\"") == -1) {
							datas.IMG_DESCRIPTIONS.clear();
							nowUrl = httpurl;
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
								parseJson.parseClassicBookContent(
										datas.IMG_DESCRIPTIONS, response);
//								if (datas.IMG_DESCRIPTIONS.size() == 0) {
//									Message message = new Message();
//									message.what = BookActivity.GETNULL;
//									handler.sendMessage(message);
//								} else {
									Message message = new Message();
									if (state == 0) {
										message.what = BookActivity.GETDATE;
									} else {
										message.what = BookActivity.REF;
									}
									handler.sendMessage(message);
//								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}else {
							Message message = new Message();
							message.what = BookActivity.GETNULL;
							handler.sendMessage(message);
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

	private void setbg(View v, int Rid) {
		Bitmap bm = BitmapFactory.decodeResource(this.getResources(), Rid);
		BitmapDrawable bd = new BitmapDrawable(this.getResources(), bm);
		v.setBackgroundDrawable(bd);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		unbindDrawables(gridView);
		unbindDrawables(gridView2);
		super.onDestroy();
	}
}
