package com.example.mayq.customviewdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by mayq on 2016/8/1.
 */
public class DragDemo extends Activity {
    private LinearLayout ll;
    private TextView tv;

    private int left; //手指相对于child左侧的距离
    private int top;//手指相对于child顶部的距离

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drag);
        ll = (LinearLayout) findViewById(R.id.ll);
        tv = (TextView) findViewById(R.id.tv);

        ll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (onChild(event)) {
                            left = (int) event.getX() - tv.getLeft();
                            top = (int) event.getY() - tv.getTop();
                            return true;
                        } else {
                            return false;
                        }
                    case MotionEvent.ACTION_MOVE:
                        moveChild(event);
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }
                return true;
            }
        });
    }

    private boolean onChild(MotionEvent e) {
        return e.getX() > tv.getLeft()
                && e.getX() < tv.getRight()
                && e.getY() > tv.getTop()
                && e.getY() < tv.getBottom();
    }

    private void moveChild(MotionEvent e) {
        int x = (int) e.getX() - left;
        int y = (int) e.getY() - top;
        LinearLayout.LayoutParams p = (LinearLayout.LayoutParams) tv.getLayoutParams();
        p.leftMargin = x;
        p.topMargin = y;
        tv.setLayoutParams(p);
    }
}
