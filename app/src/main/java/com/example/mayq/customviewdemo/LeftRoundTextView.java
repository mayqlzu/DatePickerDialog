package com.example.mayq.customviewdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by mayq on 2016/8/5.
 * <p/>
 * 一个自定义TextView，背景的左边是半圆曲线，其他三边都是直线
 */
public class LeftRoundTextView extends TextView {
    private Paint paint;

    public LeftRoundTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(context.getResources().getColor(R.color.color0));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        // 先画一个圆
        //public void drawOval(float left, float top, float right, float bottom, @NonNull Paint paint) {
        canvas.drawOval(0, 0, height, height, paint);
        // 再画一个矩形
        //public void drawRect(float left, float top, float right, float bottom, @NonNull Paint paint) {
        canvas.drawRect(height / 2, 0, width, height, paint);

        super.onDraw(canvas);
    }
}
