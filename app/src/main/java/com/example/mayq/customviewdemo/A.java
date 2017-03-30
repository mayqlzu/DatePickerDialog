package com.example.mayq.customviewdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

/**
 * Created by mayq on 2016/7/13.
 */
public class A extends LinearLayout {

    public A(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Utils.printMeasreSpec("A.onMeasure() width: ", widthMeasureSpec);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //Utils.printLayoutInfo("A.onLayout()", changed, l, t, r, b);
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //Log.d("yong", "A.onDraw()");
        super.onDraw(canvas);
    }
}
