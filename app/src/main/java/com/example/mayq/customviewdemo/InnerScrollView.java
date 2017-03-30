package com.example.mayq.customviewdemo;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ScrollView;
import android.view.View;
import android.widget.Scroller;

/**
 * Created by mayq on 2016/7/19.
 */
public class InnerScrollView extends ScrollView {
    int scrollY = 0;


    public InnerScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public boolean hitTop() {
        //Log.d("mayq", "hitTop? read scrollY=" + scrollY);
        return Math.abs(scrollY - 0) < 10;
    }

    public boolean hitBottom() {
        int childHeight = getChildAt(0).getMeasuredHeight();
        int height = getMeasuredHeight();
        int maxScrollY = childHeight - height;

        //Log.d("mayq", "hitBottom=" + (scrollY == maxScrollY)
//                + " scrollY=" + scrollY + " maxScrollY=" + maxScrollY);

        //Log.d("mayq", "hitBottom? read scrollY=" + scrollY);
        return Math.abs(scrollY - maxScrollY) < 10;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {

        scrollY = t;
        //Log.d("mayq", "write scrollY=" + t);

        super.onScrollChanged(l, t, oldl, oldt);
    }
}
