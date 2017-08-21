package com.artgallery.application;

import java.util.List;

import com.artgallery.service.TimeService;
import com.umeng.analytics.MobclickAgent;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;

public class BaseActivity extends Activity {

	private Intent timeService;
	private boolean Foregroundflag = true;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		ActionBar bar = getActionBar();
		bar.hide();
		// Context context;
		// context = this;

//		Object service = getSystemService("statusbar");
//		try {
//			Class<?> statusBarManager = Class
//					.forName("android.app.StatusBarManager");
//			Method expand = statusBarManager.getMethod("disable", int.class);
//			expand.invoke(service, 0x00000001);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

		// try {
		// Object service = getSystemService("statusbar");
		// if (service != null) {
		// Method expand =
		// service.getClass().getMethod("expandNotificationsPanel");
		//
		// expand.invoke(service);
		// }
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

		// StatusBarManager mStatusBarManager = (StatusBarManager) context
		// .getSystemService(context.STATUS_BAR_SERVICE);
	}

	// @Override
	// public void onWindowFocusChanged(boolean hasFocus) {
	// // TODO Auto-generated method stub
	// super.onWindowFocusChanged(hasFocus);
	// try {
	//
	//
	// Object service = getSystemService("statusbar");
	// Class<?> statusbarManager =
	// Class.forName("android.app.StatusBarManager");
	// Method test = statusbarManager.getMethod("collapse");
	// test.invoke(service);
	// } catch (Exception ex) {
	// ex.printStackTrace();
	// }
	// }
	//

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		int touchEvent = ev.getAction();
		switch (touchEvent) {
		case MotionEvent.ACTION_DOWN:
			break;
		case MotionEvent.ACTION_UP:
			Intent reset = new Intent();
			reset.setAction("com.fy.RESET_TIME");
			sendBroadcast(reset);
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		default:
			break;
		}
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			timeService = new Intent(this, TimeService.class);
			stopService(timeService);
			finish();
			return true;
		} else
			return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();

//		if (!isAppOnForeground() && Foregroundflag) {
//			// app 进入后台
//			System.out.println("进入后台");
//			finish();
//
//			ActivityManager manager = (ActivityManager) getApplicationContext()
//					.getSystemService(Context.ACTIVITY_SERVICE);
//			List<RunningTaskInfo> task_info = manager.getRunningTasks(20);
//
//			String className = "";
//			for (int i = 0; i < task_info.size(); i++) {
//				if ("com.fy.wo".equals(task_info.get(i).topActivity
//						.getPackageName())) {
//					System.out.println("后台  "
//							+ task_info.get(i).topActivity.getClassName());
//
//					className = task_info.get(i).topActivity.getClassName();
//
//					// 这里是指从后台返回到前台 前两个的是关键
//					Intent intent = new Intent();
//					try {
//						intent.setClass(getApplicationContext(),
//								Class.forName(className));
//						startActivity(intent);
//					} catch (ClassNotFoundException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//
//				}
//			}
//		}

	}

	/**
	 * 程序是否在前台运行
	 * 
	 * @return
	 */
	public boolean isAppOnForeground() {
		// Returns a list of application processes that are running on the
		// device

		ActivityManager activityManager = (ActivityManager) getApplicationContext()
				.getSystemService(Context.ACTIVITY_SERVICE);
		String packageName = getApplicationContext().getPackageName();

		List<RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		if (appProcesses == null)
			return false;

		for (RunningAppProcessInfo appProcess : appProcesses) {
			// The name of the process that this object is associated with.
			if (appProcess.processName.equals(packageName)
					&& appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				return true;
			}
		}

		return false;
	}

	// @Override
	// public void finish() {
	// // TODO Auto-generated method stub
	// timeService = new Intent(this,TimeService.class);
	// stopService(timeService);
	// super.finish();
	// }
	//
	// @Override
	// protected void onDestroy() {
	// // TODO Auto-generated method stub
	// timeService = new Intent(this,TimeService.class);
	// stopService(timeService);
	// super.onDestroy();
	// }

	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		// if (!isActive) {
		// app 从后台唤醒，进入前台

		// isActive = true;
		// }
	}

	protected void onPause() {
		super.onPause();
		System.out.println("onPause");
		MobclickAgent.onPause(this);
	}

	public boolean isForegroundflag() {
		return Foregroundflag;
	}

	public void setForegroundflag(boolean foregroundflag) {
		Foregroundflag = foregroundflag;
	}
	
	public void unbindDrawables(View view)
    {
//        if (view.getBackground() != null)
//        {
//            view.getBackground().setCallback(null);
//        }
        if (view instanceof ViewGroup && !(view instanceof AdapterView))
        {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++)
            {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }
}
