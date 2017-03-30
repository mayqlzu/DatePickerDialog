package com.example.mayq.customviewdemo;

import android.content.Context;


import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by mayq on 2016/7/14.
 * <p>
 * 一个包含年月日选择的自定义View，
 * 包含3个滚轮和1个确定按钮，
 * 可以嵌入到Dialog中使用；
 */
public class DatePickerView extends LinearLayout {
    private Button btnOk;
    private WheelView year;
    private WheelView month;
    private WheelView day;
    private int daySizeLastTime = -1; //上一次选择的年月有几天

    private Calendar calendar = Calendar.getInstance();
    private OnSelectListener listener;

    private int defYear;
    ;
    private int defMonth;
    ;
    private int defDay;
    ;

    public interface OnSelectListener {
        void onSelect(int year, int month, int day);
    }

    public DatePickerView(Context context) {
        this(context, null);
    }

    public DatePickerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    private void init(Context context) {
        // 用layout，方便点
        inflate(context, R.layout.date_picker_view, this);

        btnOk = (Button) this.findViewById(R.id.btn_ok);
        year = (WheelView) this.findViewById(R.id.wheel0);
        month = (WheelView) this.findViewById(R.id.wheel1);
        day = (WheelView) this.findViewById(R.id.wheel2);

        /*
        year = new WheelView(context);
        month = new WheelView(context);
        day = new WheelView(context);

        addView(year);
        addView(month);
        addView(day);
        */

        //暂时显示去年/今年/明年，你可以按需修改
        ArrayList<Integer> y = getRecentYears(1, 1);
        ArrayList<Integer> m = getMonths();
        ArrayList<Integer> d = getDays(y.get(0), m.get(0));

        year.setData(y);
        month.setData(m);
        day.setData(d);

        btnOk.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isScrolling()) {
                    int y = year.getChoice();
                    int m = month.getChoice();
                    int d = day.getChoice();
                    listener.onSelect(y, m, d);
                }
            }
        });

        year.setListener(new WheelView.OnChangeListener() {
            @Override
            public void onChange(Integer item) {
                updateDays();
            }
        });

        month.setListener(new WheelView.OnChangeListener() {
            @Override
            public void onChange(Integer item) {
                updateDays();
            }
        });
    }

    /*
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int childWidthSpec = MeasureSpec.makeMeasureSpec(widthSize / 3, MeasureSpec.EXACTLY);
        int childHeightSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);

        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            child.measure(childWidthSpec, childHeightSpec);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    */

    /*
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        year.layout(0, 0, width / 3, height);
        month.layout(width / 3, 0, 2 * width / 3, height);
        day.layout(2 * width / 3, 0, 3 * width / 3, height);
    }
    */

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    // 根据年和月调整天的数据
    private void updateDays() {
        int y = year.getChoice();
        int m = month.getChoice();
        int oldDay = day.getChoice();

        ArrayList<Integer> newDays = getDays(y, m);
        day.setData(newDays);

        if (newDays.size() < daySizeLastTime) {
            if (!newDays.contains(oldDay))
                day.gotoItemByIndex(newDays.size() - 1);
        }
        daySizeLastTime = newDays.size();
    }

    /**
     * 获取最近的几年
     *
     * @param before 往前几年
     * @param after  往后几年
     * @return
     */
    private ArrayList<Integer> getRecentYears(int before, int after) {
        int thisYear = calendar.get(Calendar.YEAR);
        int minYear = thisYear - before;
        int count = before + 1 + after;

        ArrayList<Integer> result = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            result.add(minYear + i);
        }
        return result;
    }

    // 12个月，从1开始的
    private ArrayList<Integer> getMonths() {
        ArrayList<Integer> result = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            result.add(i + 1);
        }
        return result;
    }

    /**
     * @param year
     * @param month 从1开始的
     * @return
     */
    private ArrayList<Integer> getDays(int year, int month) {
        ArrayList<Integer> result = new ArrayList<>();

        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month - 1); //参数接受从0开始的月份
        int max = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 0; i < max; i++) {
            result.add(i + 1);
        }

        return result;
    }

    /**
     * 定位到今天
     */
    public void gotoToday() {
        Calendar c = Calendar.getInstance();
        year.gotoItemByValue(c.get(Calendar.YEAR));
        month.gotoItemByValue(c.get(Calendar.MONTH) + 1);
        day.gotoItemByValue(c.get(Calendar.DAY_OF_MONTH));
    }

    private boolean isScrolling() {
        return year.isScrolling() || month.isScrolling() || day.isScrolling();
    }

    public void setOnSelectListener(OnSelectListener l) {
        listener = l;
    }

    public void setDefaultChoice(int y, int m, int d) {
        year.setDefaultVal(y);
        month.setDefaultVal(m);
        day.setDefaultVal(d);

        // 构造方法里的days数据是按照year.get(0)和month.get(0)计算的，这儿需要更新了
        ArrayList<Integer> days = getDays(y, m);
        day.setData(days);
    }
}
