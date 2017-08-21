package com.artgallery.activity;

import com.artgallery.application.BaseActivity;
import com.artgallery.wo.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HelpActivity extends BaseActivity implements OnClickListener {

	TextView serializable_code;
	RelativeLayout back;
	TextView title_text;
	TextView setting;
	private SharedPreferences sp;
	private String mac;
	private RelativeLayout remain_layout;
	private Button remain_hide;
	private Button remain_all;
	private Button remain_ret;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);
		sp = getSharedPreferences("SP", MODE_PRIVATE);
		mac = sp.getString("mac", "null mac");

		serializable_code = (TextView) findViewById(R.id.serializable_code);
		title_text = (TextView) findViewById(R.id.title_text);
		setting = (TextView) findViewById(R.id.setting);
		back = (RelativeLayout) findViewById(R.id.back);

		title_text.setText("使用帮助");
		serializable_code.setText("6.本机序列码：" + mac);
		back.setOnClickListener(this);
		setting.setOnClickListener(this);

		remain_layout = (RelativeLayout) findViewById(R.id.remain_layout);
		remain_hide = (Button) findViewById(R.id.remain_hide);
		remain_all = (Button) findViewById(R.id.remain_all);
		remain_ret = (Button) findViewById(R.id.remain_ret);
		remain_layout.setOnClickListener(this);
		remain_hide.setOnClickListener(this);
		remain_all.setOnClickListener(this);
		remain_ret.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.back:
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
			case R.id.remain_ret:
				remain_layout.setVisibility(View.GONE);
				remain_hide.setVisibility(View.VISIBLE);
				finish();
				break;
			case R.id.remain_all:
				remain_layout.setVisibility(View.GONE);
				remain_hide.setVisibility(View.VISIBLE);
				Intent intent = new Intent();
				intent.setClass(this, MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				break;
			case R.id.setting:
				CharSequence[] items = {"切换账号", "退出程序", "取消"};
				new AlertDialog.Builder(HelpActivity.this).setTitle("")
						.setItems(items, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								LayoutInflater inflater = getLayoutInflater();
								View layout;
								final EditText ip;
								final EditText psw;
								switch (which) {
									case 0:
										//改掉切换网关为切换账号
//										layout = inflater
//												.inflate(
//														R.layout.ipset,
//														(ViewGroup) findViewById(R.id.ipdialog));
//										ip = (EditText) layout.findViewById(R.id.ipadd);
//										psw = (EditText) layout
//												.findViewById(R.id.ippassword);
//										ip.setVisibility(View.VISIBLE);
//										Dialog ipdialog = new AlertDialog.Builder(
//												HelpActivity.this)
//												.setTitle("请设置网关地址")
//												.setView(layout)
//												.setPositiveButton(
//														"确定",
//														new DialogInterface.OnClickListener() {
//															public void onClick(
//																	DialogInterface dialog,
//																	int whichButton) {
//
//																if (psw.getText()
//																		.toString()
//																		.equals("ipsetting")) {
//
//																	Editor editor = sp.edit();
//																	editor.putString("IPaddress", ip.getText().toString());
//																	editor.commit();
//																	editor = null;
//																	Toast.makeText(
//																			getApplicationContext(),
//																			"设置成功",
//																			Toast.LENGTH_SHORT)
//																			.show();
//																} else {
//																	Toast.makeText(
//																			getApplicationContext(),
//																			"密码错误",
//																			Toast.LENGTH_SHORT)
//																			.show();
//																}
//															}
//														})// 设置确定按钮
//												.setNegativeButton(
//														"取消",
//														new DialogInterface.OnClickListener() {
//															public void onClick(
//																	DialogInterface dialog,
//																	int whichButton) {
//															}
//														})// 设置取消按钮
//												.create();
//										ipdialog.show();
										Bundle bundle = new Bundle();
										bundle.putBoolean("swapUser", true);
										Intent intent = new Intent();
										intent.setClass(HelpActivity.this, LoginActivity.class);
										intent.putExtras(bundle);
										startActivity(intent);
										finish();
										break;
									case 1:
										layout = inflater
												.inflate(
														R.layout.ipset,
														(ViewGroup) findViewById(R.id.ipdialog));
										ip = (EditText) layout.findViewById(R.id.ipadd);
										psw = (EditText) layout
												.findViewById(R.id.ippassword);
										ip.setVisibility(View.GONE);
										Dialog exitdialog = new AlertDialog.Builder(
												HelpActivity.this)
												.setTitle("请输入密码")
												.setView(layout)
												.setPositiveButton(
														"确定",
														new DialogInterface.OnClickListener() {
															public void onClick(
																	DialogInterface dialog,
																	int whichButton) {
																if (psw.getText()
																		.toString()
																		.equals("fyzyp")) {
																	//// 使虚拟机停止运行并退出程序
																	Intent exitIntent = new Intent(Intent.ACTION_MAIN);
																	exitIntent.addCategory(Intent.CATEGORY_HOME);
																	startActivity(exitIntent);
																	System.exit(0);
																	finish();
																} else {
																	Toast.makeText(
																			getApplicationContext(),
																			"密码错误",
																			Toast.LENGTH_SHORT)
																			.show();
																}
															}
														})// 设置确定按钮
												.setNegativeButton(
														"取消",
														new DialogInterface.OnClickListener() {
															public void onClick(
																	DialogInterface dialog,
																	int whichButton) {
															}
														})// 设置取消按钮
												.create();
										exitdialog.show();
										break;
									case 2:
										break;
								}
							}
						}).create().show();
				break;
		}
	}

}
