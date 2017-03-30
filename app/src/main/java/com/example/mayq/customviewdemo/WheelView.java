package com.example.mayq.customviewdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

import java.util.ArrayList;

/**
 * Created by mayq on 2016/7/14.
 * <p>
 * 一个模拟滚轮的自定义View，可以显示指定的任意一列数据
 */
public class WheelView extends View {
    private Paint paint;
    private float paperTop = 0;
    private float paperTopWhenFingerDown;
    private float yDown;
    private float lineHeight;
    private float minPaperTop;
    private boolean inFling = false;
    private boolean inFineTune = false;
    private ArrayList<Integer> data = new ArrayList<>();
    private static final int VISIBLE_ITEM_COUNT = 3;
    private static final int SCROLL_BY_ITEM_COUNT = 10;
    private static final int BLANK_VAL = Integer.MAX_VALUE;

    private static int COLOR_NORMAL;
    private static int COLOR_SELECTED;

    private OnChangeListener listener;

    private int defaltValCache = BLANK_VAL;

    public interface OnChangeListener {
        void onChange(Integer item);
    }

    private Scroller scroller;
    private GestureDetector.SimpleOnGestureListener gestureListener =
            new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

                    //public void fling(int startX, int startY, int velocityX, int velocityY,
                    //int minX, int maxX, int minY, int maxY) {

                    //Log.d("mayq", "onFling，开始滚动");
                    inFling = true;
                    scroller.fling(0, (int) paperTop, 0, (int) velocityY,
                            0, 0, (int) (paperTop - SCROLL_BY_ITEM_COUNT * lineHeight),
                            (int) (paperTop + SCROLL_BY_ITEM_COUNT * lineHeight));
                    invalidate();

                    return true;
                }
            };

    private GestureDetector gestureDetector;

    public WheelView(Context context) {
        this(context, null);
    }

    public WheelView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }


    private void init(Context context) {
        COLOR_NORMAL = context.getResources().getColor(R.color.color2);
        COLOR_SELECTED = context.getResources().getColor(R.color.color0);

        paint = new Paint();
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setAntiAlias(true);
        paint.setTextSize(60);
        paint.setColor(COLOR_NORMAL);

        gestureDetector = new GestureDetector(context, gestureListener);
        scroller = new Scroller(context, new DecelerateInterpolator());
    }


    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            paperTop = scroller.getCurrY();

            if (paperTop > 0) {
                paperTop = 0;
                scroller.forceFinished(true);
            } else if (paperTop < minPaperTop) {
                paperTop = minPaperTop;
                scroller.forceFinished(true);
            }

            invalidate();
        } else {
            if (inFling) {
                inFling = false;
                //Log.d("mayq", "fling结束，开始微调");
                // fling结束，微调
                fineTuneIfNeeded();
            } else if (inFineTune) {
                inFineTune = false;
                //Log.d("mayq", "微调结束，可以触发listener了, item=" + getChoice());
                if (null != listener)
                    listener.onChange(getChoice());
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        lineHeight = height / VISIBLE_ITEM_COUNT;
        minPaperTop = -(lineHeight * (data.size() - VISIBLE_ITEM_COUNT));

        // 此时lineHeight才得到有效值
        if (BLANK_VAL != defaltValCache) {
            // 更新paperTop
            setDefaultItemByValue(defaltValCache);
            // 消耗掉，避免重复进入
            defaltValCache = BLANK_VAL;
        }

        for (int i = 0; i < data.size(); i++) {
            String text = (BLANK_VAL == data.get(i) ? " "/*1个空格*/ : "" + data.get(i));

            Rect rect = new Rect();
            paint.getTextBounds(text, 0, 1, rect); // text不能为空，否则抛异常
            float textHeight = rect.height();

            float yOffset = lineHeight / 2 + textHeight / 2;
            float x = width / 2;
            float y = paperTop + lineHeight * i + yOffset;

            float min = lineHeight * 1;
            float max = lineHeight * 2;

            if (y > min && y < max) {
                paint.setColor(COLOR_SELECTED);
            } else {
                paint.setColor(COLOR_NORMAL);
            }
            canvas.drawText(text, x, y, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                yDown = event.getY();
                paperTopWhenFingerDown = paperTop;

                scroller.forceFinished(true);
                break;
            case MotionEvent.ACTION_MOVE:
                float yNow = event.getY();
                float yOffset = yNow - yDown;
                paperTop = paperTopWhenFingerDown + yOffset;
                if (paperTop > 0) {
                    paperTop = 0;
                } else if (paperTop < minPaperTop) {
                    paperTop = minPaperTop;
                }

                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                //Log.d("mayq", "手指抬起");

                /**
                 *  注意，这儿有个前提条件：
                 *  gestureDetector.onTouchEvent(event); 赶在前面调用了，
                 *  所以onFling()已经触发，继而scroll.fling()已经触发，
                 *  所以这儿可以用scroller.isFinished()判断
                 */
                if (scroller.isFinished()) {
                    //Log.d("mayq", "没有触发fling，开始微调");
                    // 微调
                    fineTuneIfNeeded();
                } else {
                    // 在computeScroll()里处理微调
                }
                break;
            default:
                break;
        }

        return true;
    }

    // 计算需要微调的距离
    private int getDx() {
        // 转成正数计算，比较好理解
        float postive = Math.abs(paperTop);

        float rem = postive % lineHeight;
        if (0 == rem)
            return 0;

        int timesLow = (int) (postive / lineHeight);
        int timesUper = (int) ((postive + lineHeight) / lineHeight);

        float low = lineHeight * timesLow;
        float upper = lineHeight * timesUper;

        if (Math.abs(postive - low) < Math.abs(postive - upper)) {
            // 负数向下滚动
            return (int) (postive - low);
        } else {
            // 正数向上滚动
            return (int) (postive - upper);
        }
    }

    // 设置要显示的数据
    public void setData(ArrayList<Integer> d) {
        data.clear();
        data.add(BLANK_VAL);
        data.addAll(d);
        data.add(BLANK_VAL);
        invalidate();
    }

    public void setListener(OnChangeListener l) {
        listener = l;
    }

    // 微调
    private void fineTuneIfNeeded() {
        int dx = getDx();
        if (0 != dx) {
            inFineTune = true;
            scroller.startScroll(0, (int) paperTop, 0, dx, 200);
            invalidate();
        } else {
            //Log.d("mayq", "不需要微调, item=" + getChoice());
            if (null != listener)
                listener.onChange(getChoice());
        }
    }

    // 获取当前的选择
    public int getChoice() {
        int index = (int) (Math.abs(paperTop) / lineHeight) + 1;
        return data.get(index);
    }

    /**
     * 定位到指定item的位置
     *
     * @param value item的值
     */
    public void gotoItemByValue(int value) {
        int index = 0;
        for (int i = 0; i < data.size(); i++) {
            if (value == data.get(i)) {
                index = i;
                break;
            }
        }

        if (index > 0)
            index--; //不考虑首位空元素
        gotoItemByIndex(index);
    }

    /**
     * 定位到指定item的位置
     *
     * @param index item的index，没考虑首尾的空元素
     */
    public void gotoItemByIndex(int index) {
        index++; //算上首位的空元素
        paperTop = -lineHeight * (index - 1);
        //Log.d("mayq", "需要定位到index=" + index + "(算上首位空元素)");
        invalidate();
    }

    /**
     * 设置默认的item，下次draw才会生效
     *
     * @param value item的值
     */
    public void setDefaultItemByValue(int value) {
        int index = 0;
        for (int i = 0; i < data.size(); i++) {
            if (value == data.get(i)) {
                index = i;
                break;
            }
        }

        if (index > 0)
            index--; //不考虑首位空元素
        setDefaultItemByIndex(index);
    }

    /**
     * 设置默认的item，下次draw才会生效
     *
     * @param index item的index，没考虑首尾的空元素
     */
    public void setDefaultItemByIndex(int index) {
        index++; //算上首位的空元素
        paperTop = -lineHeight * (index - 1);
        //Log.d("mayq", "需要定位到index=" + index + "(算上首位空元素)");
        //invalidate(); NO INVALIDATE！
    }


    // 还在滚动中
    public boolean isScrolling() {
        return !scroller.isFinished();
    }

    public void setDefaultVal(int val) {
        defaltValCache = val;
    }
}
