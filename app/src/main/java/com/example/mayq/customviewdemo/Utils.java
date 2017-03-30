package com.example.mayq.customviewdemo;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;

/**
 * Created by mayq on 2016/7/13.
 */
public class Utils {

    public static void printMeasreSpec(String tag, int measureSpec) {
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);

        String modeName = Utils.getModeName(mode);
        Log.d("yong", tag + " " + modeName + " " + size);
    }

    public static void printLayoutInfo(String tag, boolean changed, int l, int t, int r, int b) {
        Log.d("yong", tag + " changed=" + changed
                        + " l=" + l
                        + " t=" + t
                        + " r=" + r
                        + " b=" + b
        );
    }

    public static String getModeName(int mode) {
        switch (mode) {
            case View.MeasureSpec.UNSPECIFIED:
                return "UNSPECIFIED";
            case View.MeasureSpec.EXACTLY:
                return "EXACTLY";
            case View.MeasureSpec.AT_MOST:
                return "AT_MOST";
            default:
                return "UNKNOWN";
        }
    }

    public static String getMotionName(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return "down";
            case MotionEvent.ACTION_MOVE:
                return "move";
            case MotionEvent.ACTION_UP:
                return "up";
            default:
                return "other";
        }
    }
}
