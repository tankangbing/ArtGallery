package com.artgallery.tool;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

public class MultiDownLoadUtil {

	private String downloadPath;
	private List<String> urls;
	private Context context;
	private FullscreenDBHelper fullscreenDBHelper;
	private DownloadStateListener listener;
	private SharedPreferences sp;
	// 下载个数
	private int size = 0;

	private static ExecutorService executorService;
	private String cachepath = "";

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				onFail();
				break;
			case 2:
				onSuccess();
				break;
			default:

				break;
			}
		}
	};

	private boolean success = true;

	private void onFail() {
		size++;
		success = false;
		if (size == urls.size()) {
			// 释放资源
			executorService.shutdownNow();
			// 如果下载成功的个数与列表中 url个数一致，说明下载成功
			if (listener != null) {
				listener.onFailed(); // 下载成功回调
			}

		}
	}

	private void onSuccess() {
		String nowurl = urls.get(size);

		fullscreenDBHelper.update(nowurl, "true");
		size++;
		System.out.println("download video ok");
		if (size == urls.size()) {
			// 释放资源
			executorService.shutdownNow();
			// 如果下载成功的个数与列表中 url个数一致，说明下载成功
			if (listener != null) {
				if (success) {
					listener.onFinish(); // 下载成功回调

				} else {
					listener.onFailed(); // 下载成功回调
				}
			}

		}
	}

	private static final int MAXIMUM_POOL_SIZE = 1;

	static {
		executorService = Executors.newFixedThreadPool(MAXIMUM_POOL_SIZE);
	}

	public MultiDownLoadUtil(String downloadPath, List<String> urls,
			Context context) {
		super();
		this.context = context;
		this.downloadPath = downloadPath;
		this.urls = urls;

		fullscreenDBHelper = new FullscreenDBHelper(context);
//		sp = getSharedPreferences("SP", context.MODE_PRIVATE);
	}

	public DownloadStateListener getListener() {
		return listener;
	}

	public void setListener(DownloadStateListener listener) {
		this.listener = listener;
	}

	/**
	 * 开始下载
	 */
	public void startDownload() {
		// 首先检测path是否存在
		File downloadDirectory = new File(downloadPath);
		if (!downloadDirectory.exists()) {
			downloadDirectory.mkdirs();
		}
		if (executorService.isShutdown()) {
			executorService = Executors.newFixedThreadPool(MAXIMUM_POOL_SIZE);
			
		}

		for (int i = 0; i < urls.size(); i++) {
			String url = urls.get(i);
			try {
				executorService.execute(new MyTask(url));
			} catch (RejectedExecutionException e) {
				e.printStackTrace();
				if (listener != null) {
					listener.onFailed();
					listener = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
				if (listener != null) {
					listener.onFailed();
					listener = null;
				}
			}

			// new Thread(new MyTask(url)).start();
		}
	}

	private final class MyTask implements Runnable {

		private String url;

		public MyTask(String url) {
			this.url = url;
		}

		@Override
		public void run() {
			downloadBitmap(url);
		}
	}

	/**
	 * 下载图片
	 * 
	 * @param urlString
	 * @return
	 */
	private void downloadBitmap(String urlString) {
		String fileName = getFileName(urlString);

		// fileName = fileName.substring(0, fileName.lastIndexOf("."));
		// 图片命名方式
		File file = new File(downloadPath, fileName);

		if (file.exists()) {
			// 每下载成功一个，统计一下图片个数
			sendMsg(2);
			return;
		}
		if (MyTools.getExtSDCardPaths().size() > 0) {
			cachepath = MyTools.getExtSDCardPaths().get(0)
					+ "/Android/data/com.fy.wo/msgdownload/";
		} else {
			cachepath = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/Android/data/com.fy.wo/msgdownload/";
		}
		HttpURLConnection urlConnection = null;
		FileOutputStream fos = null;
		File cacheFile = null;
		InputStream is = null;
		try {
			cacheFile = new File(cachepath, "cache.mp4");
			cacheFile.createNewFile();
			URL url = new URL(urlString);
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.connect();
			if (true) {
//				if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				
				is = urlConnection.getInputStream();
				fos = new FileOutputStream(cacheFile);
				byte[] buf = new byte[1024];
				int length = -1;
				while ((length = is.read(buf)) != -1) {
					fos.write(buf, 0, length);
					fos.flush();
				}

				// 每下载成功一个，统计一下图片个数
				if (cacheFile.renameTo(file)) {
					sendMsg(2);
				} else {
					sendMsg(1);
				}
			} else {
				sendMsg(1);
			}
		} catch (IOException e) {

			e.printStackTrace();

			if (cacheFile != null && cacheFile.exists()) {
				cacheFile.delete();
			}
			// 有一个下载失败，则表示批量下载没有成功
			sendMsg(1);

		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (final IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (urlConnection != null) {
				urlConnection.disconnect();
			}

		}

	}

	private void sendMsg(int what) {
		synchronized (this) {
			if (handler != null) {
				handler.sendEmptyMessage(what);
			}
		}
	}

	private String getFileName(String urlString) {
		// String[] split = urlString.split("/");
		String[] ssss;
		ssss = urlString.split("/");
		String name = ssss[ssss.length - 1];

		return name;
	}

	/**
	 * 统计下载个数
	 */
	private void statDownloadNum() {
		synchronized (this) {
			size++;

			if (size == urls.size()) {
				// 释放资源
				executorService.shutdownNow();
				// 如果下载成功的个数与列表中 url个数一致，说明下载成功
				if (listener != null) {
					listener.onFinish(); // 下载成功回调
				}

			}
		}
	}

	// 下载完成回调接口
	public interface DownloadStateListener {

		public void onFinish();

		public void onFailed();
	}

	public void removeListener() {
		this.listener = null;
		if (handler != null) {
			handler.removeCallbacksAndMessages(null);
			handler = null;
		}
	}
}
