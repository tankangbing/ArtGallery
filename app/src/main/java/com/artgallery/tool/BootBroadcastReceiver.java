package com.artgallery.tool;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		System.out.println("自启动");
		Intent boot = context.getPackageManager().getLaunchIntentForPackage("com.artgallery.wo");
	    context.startActivity(boot);
	}

}
