package com.example.mayq.customviewdemo;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ScrollView;
import android.view.View;

/**
 * Created by mayq on 2016/7/19.
 */
public class OutScrollView extends ScrollView {
    InnerScrollView innerScrollView;
    float lastY = Integer.MIN_VALUE;

    public OutScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        innerScrollView = (InnerScrollView) this.findViewById(R.id.inner_scroll_view);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        boolean result = super.onInterceptTouchEvent(ev);
        Log.d("mayq", "onInterceptTouchEvent(), result=" + result);
        return result;


        /*
        //return false;
        boolean result = super.onInterceptTouchEvent(ev);

        if (MotionEvent.ACTION_DOWN == ev.getAction()) {
            //Log.d("mayq", "down，直接返回result=" + result);
            lastY = Integer.MIN_VALUE;
            return false;
        }

        if (Integer.MIN_VALUE != lastY) {
            boolean scrollContentDown = ev.getY() - lastY > 0;
            if (scrollContentDown) {
                if (innerScrollView.hitTop()) {
                    //Log.d("mayq", "inner到顶了");
                    result = true;
                } else {
                    //Log.d("mayq", "inner还没到顶");
                    result = false;
                }
            } else {
                if (innerScrollView.hitBottom()) {
                    //Log.d("mayq", "inner到底了");
                    result = true;
                } else {
                    //Log.d("mayq", "inner没有到底");
                    result = false;
                }
            }
        }

        lastY = ev.getY();

        //Log.d("mayq", "outer拦截吗？" + result);
        if (true == result)
            Log.d("mayq", "outer拦截了" + result);
        return result;
    */
    }

}
