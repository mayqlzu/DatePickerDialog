package com.example.mayq.customviewdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by mayq on 2016/7/21.
 */
public class Button2 extends View {
    Drawable drawable;
    String text = "aj";
    Paint paint;
    int textW, textH;
    int x, y;

    public Button2(Context context, AttributeSet attrs) {
        super(context, attrs);

        drawable = context.getResources().getDrawable(R.drawable.avatar1);
        paint = new Paint();
        paint.setTextSize(50);
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // calcuate here, in case, text or font changed, we will call requestLayout();
        textW = (int) paint.measureText(text);
        textH = (int) (paint.getFontMetrics().descent - paint.getFontMetrics().ascent);

        int w = measureWitdh(widthMeasureSpec);
        int h = measureHeight(heightMeasureSpec);


        x = w / 2;
        // y是baseline的位置
        // ref: http://www.xyczero.com/blog/article/20/
        int yOffset = (h - drawable.getIntrinsicHeight() - textH) / 2;
        y = drawable.getIntrinsicHeight() - (int) paint.getFontMetrics().ascent;
        if (yOffset > 0)
            y += yOffset;

        setMeasuredDimension(w, h);
    }

    private int measureWitdh(int widthMeasureSpec) {
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        switch (mode) {
            case MeasureSpec.UNSPECIFIED:
                return getDesiredWidth();
            case MeasureSpec.EXACTLY:
                return size;
            case MeasureSpec.AT_MOST:
                return Math.min(size, getDesiredWidth());
            default:
                return 0;
        }
    }

    private int measureHeight(int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        switch (mode) {
            case MeasureSpec.UNSPECIFIED:
                return getDesiredHeight();
            case MeasureSpec.EXACTLY:
                return size;
            case MeasureSpec.AT_MOST:
                return Math.min(size, getDesiredHeight());
            default:
                return 0;
        }
    }

    private int getDesiredWidth() {
        return Math.max(drawable.getIntrinsicWidth(), textW);
    }

    private int getDesiredHeight() {
        return drawable.getIntrinsicHeight() + textH;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        setBackgroundColor(getResources().getColor(android.R.color.white));
        int yOffset = (getHeight() - drawable.getIntrinsicHeight() - textH) / 2;
        int xOffset = (getWidth() - drawable.getIntrinsicWidth()) / 2;

        int left = 0;
        int top = 0;
        int right = drawable.getIntrinsicWidth();
        int bottom = drawable.getIntrinsicHeight();
        if(xOffset>0) {
            left += xOffset;
            right += xOffset;
        }

        if(yOffset>0){
            top += yOffset;
            bottom+=yOffset;
        }

        drawable.setBounds(left, top, right, bottom);
        drawable.draw(canvas);

        // y是baseline的位置
        canvas.drawText(text, x, y, paint);
    }
}
