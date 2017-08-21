package com.artgallery.activity;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.artgallery.application.BaseActivity;
import com.artgallery.data.LastReadDBHelper;
import com.artgallery.tool.ParseJson;
import com.artgallery.tool.TANetWorkUtil;
import com.artgallery.wo.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends BaseActivity implements OnClickListener {

	private EditText userName;
	private EditText password;
	private EditText danwei;
	private Button loginButton;
	private TextView forgetPassword;
	private TelephonyManager tm;
	private Context context;
	public final static int success = 111;
	public final static int fail = 222;
	private ProgressDialog dialog;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		context = LoginActivity.this;
		loginButton = (Button) findViewById(R.id.btn_Login);
		loginButton.setOnClickListener(this);
		ActionBar actionBar = getActionBar();
		actionBar.hide();
//		forgetPassword = (TextView) findViewById(R.id.forgetPassWord);
//		forgetPassword.setOnClickListener(this);
		TextView daohanlan = (TextView) findViewById(R.id.daohanglan);
        daohanlan.setText("登录");
		tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		userName = (EditText) findViewById(R.id.UserName);
		password = (EditText) findViewById(R.id.PassWord);
		danwei = (EditText) findViewById(R.id.danwei);
		danwei.setSingleLine(true); 
		danwei.setImeOptions(EditorInfo.IME_ACTION_NEXT);
		userName.setSingleLine(true); 
		userName.setImeOptions(EditorInfo.IME_ACTION_NEXT);
		
		dialog = new ProgressDialog(context);
		
		
//		if(!isApkInstall(context, "com.reader2014.Turnmain3")){
//			if(copyApkFromAssets(this, "reader2014.apk", Environment.getExternalStorageDirectory().getAbsolutePath()+"/reader2014.apk")){  
//	            
//	        }  
//		}
	
		sp = getSharedPreferences("SP", MODE_PRIVATE);
		Bundle bundle = getIntent().getExtras();
		System.out.println("bundle=" + bundle);
		if (bundle != null) {
			boolean swapUser = bundle.getBoolean("swapUser", false);
			if (swapUser) {
				userName.setText("");
				password.setText("");
				danwei.setText("");
			}else {
				AutoLogin();
			}
		}else {
			AutoLogin();
		}
		
	}
	
	private void AutoLogin() {
		String DengLu = sp.getString("DengLu", "no");
		userName.setText(sp.getString("UserName", ""));
		password.setText(sp.getString("SFZ", ""));
		danwei.setText(sp.getString("DanWei", ""));
		if (DengLu.equals("yes")) {
			System.out.println("自动登录");
			
			if(TANetWorkUtil.isNetworkAvailable(context)){
				System.out.println("有网络");
				onClick(loginButton);
			}else {
				System.out.println("没网络");
				Toast.makeText(context, "无法进行网络认证，请检查网络后重试", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	public Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {	
			switch (msg.what) {
			case success:
				
				dialog.dismiss();
				String name = userName.getText().toString();
				String pw = password.getText().toString();
				String DanWei = danwei.getText().toString();
				SharedPreferences sp = getSharedPreferences("SP",
						MODE_PRIVATE);
				Editor editor = sp.edit();
				String key = MakeKey();
				editor.putString("UserName", name);
				editor.putString("SFZ", pw);
				editor.putString("DanWei", DanWei);
				editor.putString("KEY", key);
				editor.putString("DengLu", "yes");
				editor.commit();
				
				LastReadDBHelper lastReadDBHelper = new LastReadDBHelper(context);
				Cursor cursor = lastReadDBHelper.select(null, "last_id=? and username=?", new String[]{"1", sp.getString("UserName", "")});
				if(cursor.moveToNext()){
					System.out.println("有数据");
				}else{
					System.out.println("添加三条数据");
					lastReadDBHelper.insert(1, "", "", "", sp.getString("UserName", ""));
					lastReadDBHelper.insert(2, "", "", "", sp.getString("UserName", ""));
					lastReadDBHelper.insert(3, "", "", "", sp.getString("UserName", ""));
				}
				
//				String path = "";
//				if(MyTools.getExtSDCardPaths().size() > 0){
//					path = MyTools.getExtSDCardPaths().get(0) + "/Android/data/com.wr.reader2/files/BOOKS/" + sp.getString("UserName", "") + "/";
//				}else {
//					path = Environment.getExternalStorageDirectory().getAbsolutePath()+ "/Android/data/com.wr.reader2/files/BOOKS/" + sp.getString("UserName", "") + "/";
//				}
//				DownloadManager downloadManager = DownloadManager.getDownloadManager(path);
//				if(downloadManager != null){
//					downloadManager.close();
//					System.out.println("DownloadManager close");
//				}
				
				Toast.makeText(context, "登录成功,欢迎回来", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent();
				intent.setClass(context, MainActivity.class);
				startActivity(intent);
				finish();
				break;
			case fail:
				Toast.makeText(context, "资料错误", Toast.LENGTH_SHORT)
				.show();
				dialog.dismiss();
				break;
			}
			loginButton.setClickable(true);
		}
	};

	private boolean copyApkFromAssets(Context context,
			String fileName, String path) {
		// TODO Auto-generated method stub
		boolean copyIsFinish = false;  
        try {  
            InputStream is = context.getAssets().open(fileName);  
            File file = new File(path);  
            file.createNewFile();  
            FileOutputStream fos = new FileOutputStream(file);  
            byte[] temp = new byte[1024];  
            int i = 0;  
            while ((i = is.read(temp)) > 0) {  
                fos.write(temp, 0, i);  
            }  
            fos.close();  
            is.close();  
            copyIsFinish = true;  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return copyIsFinish;  
	}

	private boolean isApkInstall(Context context, String packageName) {
		// TODO Auto-generated method stub
		 if (packageName == null || "".equals(packageName))
	            return false;
	        android.content.pm.ApplicationInfo info = null;
	        try {
	            info = context.getPackageManager().getApplicationInfo(packageName, 0);
	            return info != null;
	        } catch (NameNotFoundException e) {
	            return false;
	        }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_Login:
			if(TANetWorkUtil.isNetworkAvailable(context)){
				loginButton.setClickable(false);
				String name = userName.getText().toString();
				String pw = password.getText().toString();
				String DanWei = danwei.getText().toString();
				if (!name.equals("")) {
					if (!pw.equals("")) {
						if (!DanWei.equals("")) {
							// 验证流程
							FirstLogin(name, pw, DanWei);

						} else {
							Toast.makeText(this, "单位为空", Toast.LENGTH_SHORT).show();
						}
					} else {
						Toast.makeText(this, "密码为空", Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(this, "用户名为空", Toast.LENGTH_SHORT).show();
				}
			}else {
				Toast.makeText(context, "无法进行网络认证，请检查网络后重试", Toast.LENGTH_SHORT).show();
			}
			break;
//		case R.id.forgetPassWord:
//
//			Toast.makeText(this, "弹出网页", Toast.LENGTH_SHORT).show();
//			break;
		}
	}

	private String MakeKey() {
		// TODO Auto-generated method stub
//		String imei = tm.getDeviceId();// imei
		String name = userName.getText().toString();// 学号证号
//		SimpleDateFormat formatter = new SimpleDateFormat(
//				"yyyyMMddHHmmss");
//		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
//		String time = formatter.format(curDate);
		while(name.length() < 16){
			name = name + "0";
		}
		String key = "*9#aA" + name;
		return md5(key);
	}
	
	public static String md5(String string) {
	    byte[] hash;
	    try {
	        hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
	    } catch (NoSuchAlgorithmException e) {
	        throw new RuntimeException("Huh, MD5 should be supported?", e);
	    } catch (UnsupportedEncodingException e) {
	        throw new RuntimeException("Huh, UTF-8 should be supported?", e);
	    }

	    StringBuilder hex = new StringBuilder(hash.length * 2);
	    for (byte b : hash) {
	        if ((b & 0xFF) < 0x10) hex.append("0");
	        hex.append(Integer.toHexString(b & 0xFF));
	    }
	    return hex.toString();
	}

	public void FirstLogin(final String name, final String pw,
			final String danwei) {
		
		dialog.setTitle("正在登陆，请稍候...");
		dialog.show();
		dialog.setCanceledOnTouchOutside(false);
		new Thread() {

			@Override
			public void run() {
//				String path = "";
//				if(MyTools.getExtSDCardPaths().size() > 0){
//					path = MyTools.getExtSDCardPaths().get(0) + "/Android/data/com.wr.reader2/files/BOOKS/" + sp.getString("UserName", "") + "/";
//				}else {
//					path = Environment.getExternalStorageDirectory().getAbsolutePath()+ "/Android/data/com.wr.reader2/files/BOOKS/" + sp.getString("UserName", "") + "/";
//				}
//				File file = new File(path);
//				if (!file.exists()) {
//					file.mkdirs();
//				}
				try {
					String content = "http://data.iego.net:85/users/login1.aspx?";
					String imei = tm.getDeviceId();
					
					String regex = "^[a-z0-9A-Z]+$";
					if(pw.matches(regex)){
						content = content + "imei=" + imei + "&n=" + name
								+ "&id6=" + URLEncoder.encode(pw, "UTF-8") + "&unit=" + URLEncoder.encode(danwei, "UTF-8");
					}else{
						content = content + "imei=" + imei + "&n=" + name
								+ "&realname=" + URLEncoder.encode(pw, "UTF-8") + "&unit=" + URLEncoder.encode(danwei, "UTF-8");
					}
					System.out.println(content);
					URL url = new URL(content);
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.setRequestMethod("GET");
					conn.setReadTimeout(5000);
					if (conn.getResponseCode() == 200) {
						InputStream inStream = conn.getInputStream();
						ParseJson parseJson = new ParseJson();
						String result = parseJson.parseLogin(inStream);
						System.out.println("result=" + result);
						if (result.equals("0")) {
							handler.sendEmptyMessage(success);
						} else {
							handler.sendEmptyMessage(fail);
						}

					}else{
						handler.sendEmptyMessage(fail);
					}
					super.run();
				} catch (MalformedURLException e) {
					e.printStackTrace();
					handler.sendEmptyMessage(fail);
				} catch (ProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					handler.sendEmptyMessage(fail);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					handler.sendEmptyMessage(fail);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					handler.sendEmptyMessage(fail);
				}
			}

		}.start();
	}

}
