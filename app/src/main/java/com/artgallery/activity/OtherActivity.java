package com.artgallery.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.artgallery.application.BaseActivity;
import com.artgallery.wo.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class OtherActivity extends BaseActivity implements OnClickListener {

	public static final String UPDATE = "update";
	private GridView mOtherGrid;
	private Button mOtherBack;
	private SharedPreferences sp;
	//模拟数据
	private static final String[] TITLES = { "广州图书馆", "天河图书馆", "番禺图书馆", "黄埔图书馆", "增城图书馆", "海珠图书馆", "越秀图书馆", "从化图书馆"
			, "天河图书馆", "番禺图书馆", "黄埔图书馆", "增城图书馆", "海珠图书馆", "越秀图书馆", "从化图书馆"
			, "天河图书馆", "番禺图书馆", "黄埔图书馆", "增城图书馆", "海珠图书馆", "越秀图书馆", "从化图书馆"
			, "天河图书馆", "番禺图书馆", "黄埔图书馆", "增城图书馆", "海珠图书馆", "越秀图书馆", "从化图书馆"
			, "天河图书馆", "番禺图书馆", "黄埔图书馆", "增城图书馆", "海珠图书馆", "越秀图书馆", "从化图书馆"
			, "天河图书馆", "番禺图书馆", "黄埔图书馆", "增城图书馆", "海珠图书馆", "越秀图书馆", "从化图书馆"};
	private List mName;
	private List mUrl;
	private String mData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView();
		initData();
		initEvent();
	}



	private void initView() {
		setContentView(R.layout.other_activity);
		mOtherGrid = (GridView) findViewById(R.id.other_grid);
		mOtherBack = (Button) findViewById(R.id.other_back);

		mOtherBack.setOnClickListener(this);
	}

	private void initData() {
		sp = getSharedPreferences("SP", MODE_PRIVATE);
		Bundle bundle = this.getIntent().getExtras();
		if (bundle == null) {
			Toast.makeText(getApplicationContext(), "数据加载失败，请返回重试", Toast.LENGTH_SHORT).show();
		}else {
			mData = bundle.getString("data");
		}
		//判断非空时才加载数据，
		if(mData!=null){
            jsonArray(mData);
		    mOtherGrid.setAdapter(new OtherAdapter());
        }
	}
	private void initEvent() {
		mOtherGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//				Toast.makeText(getApplicationContext(),position+"",Toast.LENGTH_SHORT).show();
				String yuming = (String) mUrl.get(position);
				Intent intent = new Intent();
				intent.putExtra("yuming",yuming);
				intent.setClass(OtherActivity.this, BenGuanZiYuanActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.setAction(UPDATE);// 用于区分是他馆文化传过去的
				startActivity(intent);
				finish();
			}
		});
	}
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.other_back:
				finish();
				break;
		}
	}
	class OtherAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mName.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(getApplicationContext(), R.layout.view_other, null);
			}
			TextView tvItemTitle = (TextView) convertView.findViewById(R.id.view_other_text);

			// 设置数据
			String str = (String) mName.get(position);
			tvItemTitle.setText(str);
			return convertView;
		}

	}

	private void jsonArray(String s) {
		mName = new ArrayList<String>();
		mUrl = new ArrayList<String>();
		String danWei = sp.getString("DanWei", "");
		try {
			JSONArray array = new JSONArray(s);
			for(int i = 0;i<array.length();i++){
				JSONObject jsonObject = array.getJSONObject(i);//第一条数据
				String str =  jsonObject.getString("route");
				//判断不加载登录的那个馆
				if(danWei.equals(str)){

				}else {
					mUrl.add(jsonObject.getString("server"));
					mName.add(str);
				}
			}

//			PreferencesUtils.putString(getApplicationContext(),"server",server);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
