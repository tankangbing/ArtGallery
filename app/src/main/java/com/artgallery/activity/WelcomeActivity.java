package com.artgallery.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.artgallery.application.BaseActivity;
import com.artgallery.wo.R;

public class WelcomeActivity extends BaseActivity {
	
//	private Intent timeService;
//	private Intent getMsgService;
	private Intent mainIntent;
	private Context context;
	private final int START_MAIN = 0;
	private final static int EXIT = 1;
	private static Handler exithandler;
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case START_MAIN:
				mainIntent = new Intent();
				mainIntent.setClass(context, LoginActivity.class);
				startActivity(mainIntent);
				break;
			default:
				break;
			}
		};
	};
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome_activity);
		System.out.println("欢迎开启");
//		getMsgService = new Intent(this, MsgService.class);
//		timeService = new Intent(this, TimeService.class);
//		this.startService(timeService);
//		this.startService(getMsgService);
//		System.out.println("开启msg-time");
		context = this;
		
		
		new Thread(){
			public void run() {
				try {
					Thread.sleep(2000);
					handler.sendEmptyMessage(START_MAIN);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			};
		}.start();
		
		exithandler = new Handler(){
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case EXIT:
					finish();
//					stopService(getMsgService);
//					stopService(timeService);
					break;
				default:
					break;
				}
			};
		};
	}
	
	public static class WelcomeexitReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO 自动生成的方法存根

			if (intent.getAction().equals("com.fy.exit")) {

				exithandler.sendEmptyMessage(EXIT);

			}
		}

	}
	
	
}
