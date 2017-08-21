package com.artgallery.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.artgallery.application.BaseActivity;
import com.artgallery.wo.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class KCBActivity extends BaseActivity implements OnClickListener{
	
	private Spinner spinner_grade;
	private Spinner spinner_class;
	private Button back;
	private Button queding;
	private RelativeLayout remain_layout;
	private Button remain_hide;
	private Button remain_all;
	private Button remain_ret;
	
	private List<String> data_grade;
	private List<String> data_gradeID;
	private List<String> data_class;
	
	private ArrayAdapter<String> grade_adapter;
	private ArrayAdapter<String> class_adapter;
	
	private SharedPreferences sp;
	private Context context;
	
	private String select_grade;
	private String select_class;
	private String gradeID;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.kcb_activity);
		
		spinner_grade = (Spinner) findViewById(R.id.spinner_grade);
		spinner_class = (Spinner) findViewById(R.id.spinner_class);
		back = (Button) findViewById(R.id.kcb_backbtn);
		queding = (Button) findViewById(R.id.kcb_queding);
		back.setOnClickListener(this);
		queding.setOnClickListener(this);
		
		data_grade = new ArrayList<String>();
		data_class = new ArrayList<String>();
		data_gradeID = new ArrayList<String>();
		select_grade = "";
		select_class = "";
		gradeID = "";
		
		remain_layout = (RelativeLayout) findViewById(R.id.remain_layout);
		remain_hide = (Button) findViewById(R.id.remain_hide);
		remain_all = (Button) findViewById(R.id.remain_all);
		remain_ret = (Button) findViewById(R.id.remain_ret);
		remain_layout.setOnClickListener(this);
		remain_hide.setOnClickListener(this);
		remain_all.setOnClickListener(this);
		remain_ret.setOnClickListener(this);
		
		sp = getSharedPreferences("SP", MODE_PRIVATE);
		context = this;
		getGradeData();
	}
	
	private void getGradeData(){
		String url = sp.getString("IPaddress", "http://192.168.30.160:8000")
				+ getString(R.string.url_kcb_grade);
		RequestQueue queue = Volley.newRequestQueue(this);
		StringRequest request = new StringRequest(url, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				// TODO Auto-generated method stub
				System.out.println(response);
				try {
					JSONObject jsonObject = new JSONObject(response);
					JSONArray jsonArray = jsonObject.getJSONArray("rows");
					for (int i = 0; i < jsonArray.length(); i++) {
						data_grade.add(jsonArray.getJSONObject(i).getString("gradeName"));
						data_gradeID.add(jsonArray.getJSONObject(i).getString("id"));
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				//适配器
				grade_adapter= new ArrayAdapter<String>(context, R.layout.kcb_grade_item, data_grade);
		        //设置样式
				grade_adapter.setDropDownViewResource(R.layout.kcb_grade_item);
		        //加载适配器
				spinner_grade.setAdapter(grade_adapter);
				spinner_grade.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent, View view, 
		                    int pos, long id) {
						// TODO Auto-generated method stub
						select_grade = data_grade.get(pos);
						gradeID = data_gradeID.get(pos);
						getClassData();
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
						
					}
				});
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
	
	private void getClassData() {
		String url = sp.getString("IPaddress", "http://192.168.30.160:8000")
				+ getString(R.string.url_kcb_class) + "GradeId=" + gradeID;
		RequestQueue queue = Volley.newRequestQueue(this);
		StringRequest request = new StringRequest(url, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				// TODO Auto-generated method stub
				System.out.println(response);
				data_class.clear();
				try {
					JSONObject jsonObject = new JSONObject(response);
					JSONArray jsonArray = jsonObject.getJSONArray("rows");
					for (int i = 0; i < jsonArray.length(); i++) {
						data_class.add(jsonArray.getJSONObject(i).getString("gradeName"));
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				//适配器
				grade_adapter= new ArrayAdapter<String>(context, R.layout.kcb_grade_item, data_class);
		        //设置样式
				grade_adapter.setDropDownViewResource(R.layout.kcb_grade_item);
		        //加载适配器
				spinner_class.setAdapter(grade_adapter);
				spinner_class.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent, View view, 
		                    int pos, long id) {
						// TODO Auto-generated method stub
						select_class = data_class.get(pos);
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
						
					}
				});
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.kcb_backbtn:
//			Intent intent4 = new Intent();
//			intent4.setClass(this, MainActivity.class);
//			intent4.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(intent4);
			finish();
			break;

		case R.id.kcb_queding:
			System.out.println(select_grade + "      " + select_class);
			if (select_class != ""&&!select_grade.contains("请选择年级")) {
				Bundle bundle = new Bundle();
				bundle.putString("select_grade", select_grade);
				bundle.putString("select_class", select_class);
				intent.putExtras(bundle);
				intent.setClass(this, KCBListActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}else{
				Toast.makeText(this,"请选择年级",Toast.LENGTH_SHORT).show();
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
		case R.id.remain_ret:
			remain_layout.setVisibility(View.GONE);
			remain_hide.setVisibility(View.VISIBLE);
//			intent.setClass(this, MainActivity.class);
//			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(intent);
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
}
