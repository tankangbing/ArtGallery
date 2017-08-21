package com.artgallery.tool;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class MarqueeTextView extends TextView implements Runnable {

	private static final String TAG = "MarqueeTextView";
	
	private int circleTimes = 1;
	private int hasCircled = 0;
	private int currentScrollPos = 0;
	private int circleSpeed = 1;
	private int textWidth = 0;
	private boolean isMeasured = false;
	private Handler handler;
	private boolean flag = false;
	public MarqueeTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.removeCallbacks(this);
		post(this);
//		handler = new Handler();
//		handler.postDelayed(this, 300);
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(!isMeasured){
			getTextWidth();
			isMeasured = true;
		}
	}
	
	@Override
	public void setVisibility(int visibility){
		//二次进入时初始化成员变量
		flag = false;
		isMeasured = false;
		this.hasCircled = 0;
		super.setVisibility(visibility);
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		//起始滚动位置
		currentScrollPos += 1;
		scrollTo(0,currentScrollPos);
		
		//判断滚动一次
		if(currentScrollPos >= textWidth) {
			//从屏幕哪侧开始出现
//			currentScrollPos = -this.getWidth();	
//			currentScrollPos = this.getHeight()-1800;
			currentScrollPos = 0;
			if(hasCircled >= this.circleTimes) {
				this.setVisibility(View.GONE);
				flag = true;
			}
			hasCircled += 1;
		}
		if(!flag) {			
			//滚动时间间隔
			postDelayed(this, circleSpeed);
			//handler.postDelayed(this, circleSpeed);
		}		
	}
		
	/**
	 * 获取文本显示长度
	 */
	private void getTextWidth() {
		Paint paint = this.getPaint();
		String str = this.getText().toString();
		if(str == null) {
			textWidth = 0;
		}		
		textWidth = (int)paint.measureText(str);
	}
	/**
	 * 设置滚动次数，达到次数后设置不可见
	 * @param circleTimes
	 */
	public void setCircleTimes(int circleTimes){
		this.circleTimes = circleTimes;
	}

	public void setSpeed(int speed) {
		this.circleSpeed = speed;
	}
	
	public void startScrollShow() {
		if(this.getVisibility() == View.GONE)
			this.setVisibility(View.VISIBLE);
		this.removeCallbacks(this);
		post(this);
	}
	
	private void stopScroll() {
		handler.removeCallbacks(this);
	}
}


