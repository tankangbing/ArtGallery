package com.artgallery.service;

import com.artgallery.activity.MsgActivity;
import com.artgallery.tool.FullscreenDBHelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

public class OpenReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		System.out.println("全屏跳转判断结果");
		FullscreenDBHelper fullscreenDBHelper = new FullscreenDBHelper(
				context);
		Cursor cursor = fullscreenDBHelper.select(null,
				"model = ? and areaid = ? and dlpath=?", new String[] {
						"2", "0", "false" });
		
		if (!cursor.moveToNext()) {
			System.out.println("全屏进行跳转");
			Intent open = new Intent();
			open.setClass(context, MsgActivity.class);
			open.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(open);
		}else {
			System.out.println("全屏数据没就绪");
			Intent reset = new Intent();
			reset.setAction("com.fy.RESET_TIME");  
			context.sendBroadcast(reset); 
		}
	}//.......

}
