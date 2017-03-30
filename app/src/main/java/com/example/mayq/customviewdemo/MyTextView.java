package com.example.mayq.customviewdemo;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * Created by mayq on 2016/7/20.
 */
public class MyTextView extends TextView {
    public MyTextView(Context context) {
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = super.onTouchEvent(event);
        //Log.d("mayq", "MyTextView.onTouch, action=" + Utils.getMotionName(event));
        return result;
    }
}
