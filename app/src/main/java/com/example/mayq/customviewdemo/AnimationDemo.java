package com.example.mayq.customviewdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by mayq on 2016/7/29.
 */
public class AnimationDemo extends Activity {
    private Button btnTrans;
    private Button btnRotate;
    private Button btnScale;
    private Button btnAlpha;
    private Button btnAll;
    private boolean moveRight = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.animation);

        btnTrans = (Button) findViewById(R.id.btn_trans);
        btnRotate = (Button) findViewById(R.id.btn_rotate);
        btnScale = (Button) findViewById(R.id.btn_scale);
        btnAlpha = (Button) findViewById(R.id.btn_alpha);
        btnAll = (Button) findViewById(R.id.btn_all);
        btnTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move();
            }
        });

        btnRotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rotate();
            }
        });
        btnScale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scale();
            }
        });

        btnAlpha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alpha();
            }
        });

        btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                all();
            }
        });

    }

    private void move() {
        TranslateAnimation a;
        if (moveRight)
            a = new TranslateAnimation(0, 400, 0, 0);
        else
            a = new TranslateAnimation(0, -400, 0, 0);

        a.setDuration(1 * 1000);
        a.setFillAfter(true);
        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                LinearLayout.LayoutParams p = (LinearLayout.LayoutParams) btnTrans.getLayoutParams();
                if (moveRight)
                    p.leftMargin += 400;
                else
                    p.leftMargin -= 400;

                btnTrans.setLayoutParams(p);
                btnTrans.clearAnimation();
                moveRight = !moveRight;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        btnTrans.startAnimation(a);
    }

    private void rotate() {

        //public RotateAnimation(float fromDegrees, float toDegrees,
        // int pivotXType, float pivotXValue,
        //int pivotYType, float pivotYValue) {
        RotateAnimation a = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        a.setDuration(1 * 1000);
        a.setFillAfter(true);
        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                btnRotate.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        btnRotate.startAnimation(a);
    }


    private void scale() {
        //public ScaleAnimation(float fromX, float toX, float fromY, float toY) {
        ScaleAnimation a = new ScaleAnimation(1, 2, 1, 2);
        a.setDuration(1 * 1000);
        a.setFillAfter(true);
        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                btnScale.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        btnScale.startAnimation(a);
    }

    private void alpha() {
        AlphaAnimation a = new AlphaAnimation(1, 0);
        a.setDuration(1 * 1000);
        a.setFillAfter(true);
        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                btnAlpha.clearAnimation();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        btnAlpha.startAnimation(a);
    }

    private void all() {
        RotateAnimation a = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        a.setDuration(2 * 1000);
        a.setFillAfter(true);

        AlphaAnimation a2 = new AlphaAnimation(1, 0);
        a2.setDuration(2 * 1000);
        a2.setFillAfter(true);

        AnimationSet set = new AnimationSet(false);
        set.addAnimation(a);
        set.addAnimation(a2);
        set.setFillAfter(true);

        btnAll.startAnimation(set);
    }


}
