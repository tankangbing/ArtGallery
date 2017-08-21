package com.artgallery.activity;

import java.net.URLEncoder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.artgallery.adapter.KCBAdapter;
import com.artgallery.application.BaseActivity;
import com.artgallery.data.KCBData;
import com.artgallery.tool.ParseJson;
import com.artgallery.wo.R;

public class KCBListActivity extends BaseActivity implements OnClickListener{
	
	private ListView am_list;
	private ListView pm_list;
	private Button back;
	private RelativeLayout remain_layout;
	private Button remain_hide;
	private Button remain_all;
	private Button remain_ret;
	
	private KCBData kcbData;
	
	private SharedPreferences sp;
	private Context context;
	
	private String gradeName;
	private String className;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.kcb_list_activity);
		am_list = (ListView) findViewById(R.id.am_list);
		pm_list = (ListView) findViewById(R.id.pm_list);
		back = (Button) findViewById(R.id.kcb_backbtn);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		remain_layout = (RelativeLayout) findViewById(R.id.remain_layout);
		remain_hide = (Button) findViewById(R.id.remain_hide);
		remain_all = (Button) findViewById(R.id.remain_all);
		remain_ret = (Button) findViewById(R.id.remain_ret);
		remain_layout.setOnClickListener(this);
		remain_hide.setOnClickListener(this);
		remain_all.setOnClickListener(this);
		remain_ret.setOnClickListener(this);
		
		kcbData = new KCBData();
		
		sp = getSharedPreferences("SP", MODE_PRIVATE);
		context = this;
		
		Bundle bundle = getIntent().getExtras();
		gradeName = bundle.getString("select_grade");
		className = bundle.getString("select_class");
		
		try {
			getData();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void getData() throws Exception {
		String url = sp.getString("IPaddress", "http://192.168.30.160:8000")
				+ "/cirmSystem/curriculum/findCurriculums_client?param.rows=999&param.page=1&param.sidx=createTime&param.sord=asc"
				+ "&grade=" + URLEncoder.encode(gradeName, "UTF-8") + "&classes=" + URLEncoder.encode(className, "UTF-8");
		System.out.println("课程表=" + url);
		RequestQueue queue = Volley.newRequestQueue(this);
		StringRequest request = new StringRequest(url, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				// TODO Auto-generated method stub
				System.out.println(response);
				ParseJson parseJson = new ParseJson();
				try {
					parseJson.parseKCB(response, kcbData.Monday_am, kcbData.Monday_pm,
							kcbData.Tuesday_am, kcbData.Tuesday_pm,
							kcbData.Wednesday_am, kcbData.Wednesday_pm,
							kcbData.Thursday_am, kcbData.Thursday_pm,
							kcbData.Friday_am, kcbData.Friday_pm);
					setData();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "网络异常，请返回重试", Toast.LENGTH_SHORT).show();
			}
		});
		queue.add(request);
	}
	
	private void setData() {
		KCBAdapter am_adapter = new KCBAdapter(context, kcbData.Monday_am, kcbData.Tuesday_am
				, kcbData.Wednesday_am, kcbData.Thursday_am, kcbData.Friday_am);
		am_list.setAdapter(am_adapter);
		KCBAdapter pm_adapter = new KCBAdapter(context, kcbData.Monday_pm, kcbData.Tuesday_pm
				, kcbData.Wednesday_pm, kcbData.Thursday_pm, kcbData.Friday_pm);
		pm_list.setAdapter(pm_adapter);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.remain_layout:
			remain_layout.setVisibility(View.GONE);
			remain_hide.setVisibility(View.VISIBLE);
			break;
		case R.id.remain_hide:
			remain_layout.setVisibility(View.VISIBLE);
			remain_hide.setVisibility(View.GONE);
			break;
		case R.id.remain_ret:
			remain_layout.setVisibility(View.GONE);
			remain_hide.setVisibility(View.VISIBLE);
			finish();
			break;
		case R.id.remain_all:
			remain_layout.setVisibility(View.GONE);
			remain_hide.setVisibility(View.VISIBLE);
			intent.setClass(this, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		unbindDrawables(am_list);
		unbindDrawables(pm_list);
		super.onDestroy();
	}
}
