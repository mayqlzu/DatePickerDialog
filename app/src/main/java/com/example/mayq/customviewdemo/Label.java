package com.example.mayq.customviewdemo;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by mayq on 2016/7/20.
 * <p>
 * 默认的Button就能设置图片和文字，但是不好控制图片大小，只能使用图片的默认大小，
 * 所以自定义一个View，包含一个图片和一行文字，垂直排列，
 * 可以控制的参数包括：
 * 1) 图片前景 (单个或者selector)
 * 2) 文字内容
 * 3) 字体大小
 * 4) 文字颜色 (单个颜色或者selector)
 * 5) 图片和文字的间距
 */
public class Label extends LinearLayout {
    Button btn;

    private static final int DEFUALT_SIZE = -1;
    private static final int DEFUALT_PADDING = 0;// 图片和文字的间距

    private static final int DEFAULT_TEXT_SIZE = 40;
    private static final int DEFAULT_TEXT_COLOR = Color.BLACK;

    private ImageView iv;
    private MyTextView tv;
    private int padding;

    private Drawable img;
    private int imgWidth;
    private int imgHeight;

    private String text;
    private float textSize;
    private int textColor;
    private ColorStateList textColorStateList;

    // only used in layout xml file
    public Label(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        iv = new ImageView(context);
        tv = new MyTextView(context);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.Label,
                0, 0);

        try {
            img = a.getDrawable(R.styleable.Label_img);

            String width = a.getString(R.styleable.Label_imgWidth);
            // attrs.xml里定义了：-1表示wrap_content
            if ("-1".equals(width)) {
                imgWidth = DEFUALT_SIZE;
            } else {
                imgWidth = a.getDimensionPixelSize(R.styleable.Label_imgWidth,
                        DEFUALT_SIZE);
            }

            String height = a.getString(R.styleable.Label_imgHeight);
            if ("-1".equals(height)) {
                imgHeight = DEFUALT_SIZE;
            } else {
                imgHeight = a.getDimensionPixelSize(R.styleable.Label_imgHeight,
                        DEFUALT_SIZE);
            }

            text = a.getString(R.styleable.Label_text);

            /*
            a.getDimension() float
            a.getDimensionPixelSize() 四舍五入，int
            a.getDimensionPixelOffset() 向下取整，int
            */
            // 只能拿到px，拿不到sp
            textSize = a.getDimension(R.styleable.Label_textSize, DEFAULT_TEXT_SIZE);
            // return px
            padding = a.getDimensionPixelOffset(R.styleable.Label_padding, DEFUALT_PADDING);

            int colorResId = a.getResourceId(R.styleable.Label_textColor, 0);
            if (colorResId > 0)
                textColorStateList = context.getResources().getColorStateList(colorResId);
            else {
                textColor = a.getColor(R.styleable.Label_textColor, DEFAULT_TEXT_COLOR);
            }
        } finally {
            a.recycle();
        }

        // when should i call these?
        iv.setImageDrawable(img);
        iv.setClickable(true);

        tv.setText(text);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize); // px
        tv.setGravity(Gravity.CENTER);
        tv.setSingleLine();
        tv.setPadding(0, 0, 0, 0);

        if (null != textColorStateList)
            tv.setTextColor(textColorStateList);
        else
            tv.setTextColor(textColor);

        tv.setClickable(true); // set clickable, otherwise color will not change

        /* why return null?
        LinearLayout.LayoutParams p = (LinearLayout.LayoutParams) tv.getLayoutParams();
        p.topMargin = padding;
        */

        addView(iv);
        addView(tv);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        LinearLayout.LayoutParams tvParam = (LinearLayout.LayoutParams) tv.getLayoutParams();
        tvParam.topMargin = padding;
        tvParam.width = LayoutParams.WRAP_CONTENT;
        tvParam.height = LayoutParams.WRAP_CONTENT;
        tvParam.gravity = Gravity.CENTER_HORIZONTAL;

        // 默认情况下，没有文字，TextView也会占用一些空间，这不是我们想要的效果
        if (TextUtils.isEmpty(text))
            tvParam.height = 0;

        LinearLayout.LayoutParams ivParam = (LinearLayout.LayoutParams) iv.getLayoutParams();
        ivParam.width = (DEFUALT_SIZE == imgWidth ? LayoutParams.WRAP_CONTENT : imgWidth);
        ivParam.height = (DEFUALT_SIZE == imgHeight ? LayoutParams.WRAP_CONTENT : imgHeight);
        ivParam.gravity = Gravity.CENTER_HORIZONTAL;


        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //Log.d("mayq", "MyButton.onInterceptTouchEvent, action=" + Utils.getMotionName(ev));
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //Log.d("mayq", "MyButton.onTouch, action=" + Utils.getMotionName(event));
        boolean result = super.onTouchEvent(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_UP:
                iv.dispatchTouchEvent(event);
                tv.dispatchTouchEvent(event);
            case MotionEvent.ACTION_MOVE:
                if (!inBound(event)) {
                    iv.dispatchTouchEvent(event);
                    tv.dispatchTouchEvent(event);
                }

                break;
        }

        return result;
    }

    private boolean inBound(MotionEvent event) {
        return event.getX() >= 0 && event.getX() <= getWidth()
                && event.getY() >= 0 && event.getY() <= getHeight();
    }

    public void setMyEnabled(boolean enable) {
        setEnabled(enable);
        iv.setEnabled(enable);
        tv.setEnabled(enable);
    }

}
