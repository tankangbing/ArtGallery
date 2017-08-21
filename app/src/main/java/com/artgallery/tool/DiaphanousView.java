package com.artgallery.tool;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.Shader.TileMode;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * 透明悬浮框，可用作桌面歌词、或者遮蔽层。
 * 
 * @author 
 * 
 */
public class DiaphanousView extends TextView {
	private final String TAG = DiaphanousView.class.getSimpleName();
	public static int TOOL_BAR_HIGH = 0;
	public static WindowManager.LayoutParams params = new WindowManager.LayoutParams();
	private float startX;
	private float startY;
	private float x;
	private float y;

	private String text;
	Context context;

	WindowManager wm = (WindowManager) getContext().getApplicationContext()
			.getSystemService(getContext().WINDOW_SERVICE);

	public DiaphanousView(Context context) {

		super(context);
		text = "世上只有妈妈好,有妈的孩子像块宝";
		this.context = context;
		this.setBackgroundColor(Color.argb(255, 0, 255, 0));
		this.getBackground().setAlpha(0);

		handler = new Handler();
		// handler.post(update);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// 触摸点相对于屏幕左上角坐标
		x = event.getRawX();
		y = event.getRawY() - TOOL_BAR_HIGH;
		Log.d(TAG, "------X: " + x + "------Y:" + y);
		Intent intent = new Intent();
		intent.setAction("com.backToZreader");
		context.sendBroadcast(intent);
		setVisibility(GONE);

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startX = event.getX();
			startY = event.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			updatePosition();
			break;
		case MotionEvent.ACTION_UP:
			updatePosition();
			startX = startY = 0;
			break;
		}

		return true;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		float1 += 0.001f;
		float2 += 0.001f;

		if (float2 > 1.0) {
			float1 = 0.0f;
			float2 = 0.01f;
		}
		this.setText("");
		float len = this.getTextSize() * text.length();
		Shader shader = new LinearGradient(0, 0, len, 0, new int[] {
				Color.YELLOW, Color.RED }, new float[] { float1, float2 },
				TileMode.CLAMP);
		Paint p = new Paint();
		p.setShader(shader);
		p.setTypeface(Typeface.DEFAULT_BOLD);
		canvas.drawText(text, 0, 10, p);
	}

	private Runnable update = new Runnable() {
		public void run() {
			DiaphanousView.this.update();
			handler.postDelayed(update, 5);
		}
	};

	private void update() {
		postInvalidate(); // 更新界面
	}

	private float float1 = 0.0f;
	private float float2 = 0.01f;

	private Handler handler;

	// 更新浮动窗口位置参数
	private void updatePosition() {
		// View的当前位置
		params.x = (int) (x - startX);
		params.y = (int) (y - startY);
		wm.updateViewLayout(this, params);
	}
}