package com.artgallery.activity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;

import com.artgallery.tool.EmergencyDBHelper;
import com.artgallery.wo.R;

public class MsgSupActivity extends Activity {
	private String econtent;
	private SharedPreferences sp;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.msgsup);
		ActionBar bar = getActionBar();
		bar.hide();
//		sp = getSharedPreferences("SP", MODE_PRIVATE);
//		Editor editor = sp.edit();
//		editor.putBoolean("jinji", false);
//		editor.commit();
		
		EmergencyDBHelper emergencyDBHelper = new EmergencyDBHelper(
				getApplicationContext());
		Cursor cursor = emergencyDBHelper.select(null, null, null);
		if (cursor.moveToNext()) {
			econtent = cursor.getString(cursor.getColumnIndex("content"));
			TextView a = (TextView) findViewById(R.id.msg_sup);
			a.setText(econtent);
		} else {
		}  
		cursor.close();

	}
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
			finish();
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		default:
			break;
		}
		return super.dispatchTouchEvent(ev);
	}

}
