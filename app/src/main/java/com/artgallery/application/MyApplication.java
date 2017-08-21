package com.artgallery.application;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;

public class MyApplication extends Application {
	
	private static MyApplication instance;
	private boolean isDownload;
	private boolean isMsg; //
	
	private Context dingweicontext;
	public Context getDingweicontext() {
		return dingweicontext;
	}


	public void setDingweicontext(Context dingweicontext) {
		this.dingweicontext = dingweicontext;
	}


	private Context context;
	private String apkurl;
	private String saveFileName;
	private String buffer;

	private boolean seviceOn = true;
	public static MyApplication getInstance() {
		return instance;
	}

	
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressWarnings("unused")
	@Override
	public void onCreate() {
		
		
		
		if (false && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyDialog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyDeath().build());
		}

		super.onCreate();
		instance=this;
		initImageLoader(getApplicationContext());
	}

	
	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you may tune some of them,
		// or you can create default configuration by
		//  ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.threadPoolSize(3)//线程池内加载的数量
				.denyCacheImageMultipleSizesInMemory()
//				.memoryCache(new UsingFreqLimitedMemoryCache(100 * 1024 * 1024))
				.memoryCache(new WeakMemoryCache())
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app
				.build();
		ImageLoader.getInstance().init(config);
	}






	public boolean isDownload() {
		return isDownload;
	}


	public void setDownload(boolean isDownload) {
		this.isDownload = isDownload;
	}


	public String getApkurl() {
		return apkurl;
	}


	public void setApkurl(String apkurl) {
		this.apkurl = apkurl;
	}


	public Context getContext() {
		return context;
	}


	public void setContext(Context context) {
		this.context = context;
	}


	public String getSaveFileName() {
		return saveFileName;
	}


	public void setSaveFileName(String saveFileName) {
		this.saveFileName = saveFileName;
	}




	public String getbuffer() {
		if(buffer==null){
			return "";
		}
		return buffer;
	}


	public void setbuffer(String buffer) {
		this.buffer = buffer;
	}


	public boolean getMsg() {
		return isMsg;
	}


	public void setMsg(boolean isMsg) {
		this.isMsg = isMsg;
	}


	public boolean isSeviceOn() {
		return seviceOn;
	}


	public void setSeviceOn(boolean seviceOn) {
		this.seviceOn = seviceOn;
	}

	
	


	
}