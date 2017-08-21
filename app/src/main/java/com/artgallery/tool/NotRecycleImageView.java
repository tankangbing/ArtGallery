package com.artgallery.tool;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by Administrator on 2017/3/15.
 */

public class NotRecycleImageView extends ImageView {
    public NotRecycleImageView(Context context) {
        super(context);
    }

    public NotRecycleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        try {
            super.onDraw(canvas);
        } catch (Exception e) {
            Log.e("鍔炲鐞嗗康", "NotRecycledImageView.onDraw->exception=Canvas: trying to use a recycled bitmap");
        }
    }
}
