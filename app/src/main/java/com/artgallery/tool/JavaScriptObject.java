package com.artgallery.tool;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

public class JavaScriptObject {
	Context mContxt;
	Handler handler;

	public JavaScriptObject(Context mContxt, Handler handler) {
		this.mContxt = mContxt;
		this.handler = handler;
	}

	public void fun1FromAndroid(String name) {

		handler.sendEmptyMessage(1);

	}

	public void fun2(String name) {
		Toast.makeText(mContxt, "fun2:" + name, Toast.LENGTH_SHORT).show();
	}
}
