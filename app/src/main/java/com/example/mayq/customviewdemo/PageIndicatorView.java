package com.example.mayq.customviewdemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by mayq on 2017/1/10.
 */

public class PageIndicatorView extends View {
    int dotNum = 5;
    int dotSize = 10;
    int dotMargin = 10;
    int colorDefault = Color.BLACK;
    int colorChecked = Color.RED;

    int currentDotIndex = 0;

    Paint paint;

    public PageIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);


        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.PageIndicatorView,
                0, 0);

        try {
            /**
             * 只能拿到px，拿不到sp;
             *
             * a.getDimension() float;
             * a.getDimensionPixelSize() 四舍五入，int;
             * a.getDimensionPixelOffset() 向下取整，int;
             */
            dotSize = a.getDimensionPixelSize(R.styleable.PageIndicatorView_dotSize, 10);
            dotMargin = a.getDimensionPixelSize(R.styleable.PageIndicatorView_dotMargin, 10);

            colorDefault = a.getColor(R.styleable.PageIndicatorView_colorDefault, Color.BLACK);
            colorChecked = a.getColor(R.styleable.PageIndicatorView_colorChecked, Color.RED);
        } finally {
            a.recycle();
        }
    }

    public void setDotNum(int num) {
        dotNum = num;
        requestLayout();
    }

    public int getDotNum() {
        return dotNum;
    }

    public void setIndex(int i) {
        currentDotIndex = i;
        invalidate();
    }

    public int getIndex() {
        return currentDotIndex;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = dotSize * dotNum + dotMargin * (dotNum - 1);
        int height = dotSize;

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int radius = dotSize / 2;
        int cx = radius;
        for (int i = 0; i < dotNum; i++) {
            paint.setColor(currentDotIndex == i ? colorChecked : colorDefault);
            //public void drawCircle(float cx, float cy, float radius, @NonNull Paint paint) {
            canvas.drawCircle(cx, radius, radius, paint);
            cx += dotSize;
        }
    }
}
