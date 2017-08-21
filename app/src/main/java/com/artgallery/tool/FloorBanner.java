package com.artgallery.tool;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class FloorBanner{

	public static String IMAGE_CACHE_PATH = "imageloader/Cache"; // 图片缓存路径

	private ViewPager adViewPager;
	private List<ImageView> imageViews;// 滑动的图片集合

	private int currentItem = 0; // 当前图片的索引号
	// 定义的五个指示点


	private ScheduledExecutorService scheduledExecutorService;

	// 异步加载图片
	private ImageLoader mImageLoader;
	private DisplayImageOptions options;
	private Context context;
	private ArrayList<String> imglist = new ArrayList<String>();

	// 轮播banner的数据
	private List<AdDomain> adList;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			adViewPager.setCurrentItem(currentItem);
		};
	};

	
	public void banner(Context context,ArrayList<String>imgurl,ViewPager adViewPager){
		// 获取图片加载实例
		this.context=context;
		this.imglist=imgurl;
		this.adViewPager=adViewPager;
		mImageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.cacheInMemory(true).cacheOnDisc(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();

		initAdData();
		startAd();
	}



	private void initAdData() {
		// 广告数据
		adList = getBannerAd(imglist);

		imageViews = new ArrayList<ImageView>();

		// 点
//		dots = new ArrayList<View>();
//		dot0 = layout.findViewById(R.id.v_dot0);
//		dot1 = layout.findViewById(R.id.v_dot1);
//		dot2 = layout.findViewById(R.id.v_dot2);
//		dot3 = layout.findViewById(R.id.v_dot3);
//		dot4 = layout.findViewById(R.id.v_dot4);
//		dots.add(dot0);
//		dots.add(dot1);
//		dots.add(dot2);
//		dots.add(dot3);
//		dots.add(dot4);
//		adViewPager = (ViewPager) layout.findViewById(R.id.vp);
		
		addDynamicView();
		adViewPager.setAdapter(new MyAdapter());// 设置填充ViewPager页面的适配器
		// 设置一个监听器，当ViewPager中的页面改变时调用
		adViewPager.setOnPageChangeListener(new MyPageChangeListener());
		
	}

	private void addDynamicView() {
		// 动态添加图片和下面指示的圆点
		// 初始化图片资源
		for (int i = 0; i < adList.size(); i++) {
			ImageView imageView = new ImageView(context);
			// 异步加载图片
			mImageLoader.displayImage(adList.get(i).getImgUrl(), imageView,
					options);
			imageView.setScaleType(ScaleType.FIT_XY);
			imageViews.add(imageView);
		}
	}



	public void startAd() {
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		// 当Activity显示出来后，每5秒切换一次图片显示
		scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, 5,
				TimeUnit.SECONDS);
	}

	private class ScrollTask implements Runnable {

		@Override
		public void run() {
			synchronized (adViewPager) {
				currentItem = (currentItem + 1) % imageViews.size();
				handler.obtainMessage().sendToTarget();
			}
		}
	}

	public  void VpStop() {
		scheduledExecutorService.shutdown();
	}

	private class MyPageChangeListener implements OnPageChangeListener {

		private int oldPosition = 0;

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int position) {
			currentItem = position;
			AdDomain adDomain = adList.get(position);
			oldPosition = position;
		}
	}

	private class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return adList.size();
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView iv = imageViews.get(position);
			((ViewPager) container).addView(iv);
			final AdDomain adDomain = adList.get(position);
			// 在这个方法里面设置图片的点击事件
			iv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 处理跳转逻辑
				}
			});
			return iv;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView((View) arg2);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {

		}

		@Override
		public void finishUpdate(View arg0) {

		}

	}

	/**
	 * 轮播广播模拟数据
	 * @param imglist 
	 * 
	 * @return
	 */
	public static List<AdDomain> getBannerAd(ArrayList<String> imglist) {
		List<AdDomain> adList = new ArrayList<AdDomain>();

		for (int i = 0; i < imglist.size(); i++) {
			AdDomain adDomain = new AdDomain();
			adDomain.setImgUrl(imglist.get(i));
			adDomain.setAd(false);
			adList.add(adDomain);
		}
		return adList;
	}

}
