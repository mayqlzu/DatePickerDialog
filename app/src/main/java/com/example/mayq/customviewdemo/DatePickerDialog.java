package com.example.mayq.customviewdemo;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;

import java.util.Calendar;

/**
 * Created by mayq on 2016/7/18.
 * <p>
 * 一个选择年月日的对话框
 */
public class DatePickerDialog extends Dialog {
    private DatePickerView datePickerView;
    public OnSelectListener listener;

    public interface OnSelectListener {
        void onSelect(int year, int month, int day);
    }

    public DatePickerDialog(Context context) {
        super(context, R.style.DatePickerDialog);

        datePickerView = new DatePickerView(context);
        datePickerView.setOnSelectListener(new DatePickerView.OnSelectListener() {
            @Override
            public void onSelect(int year, int month, int day) {
                listener.onSelect(year, month, day);
                dismiss();
            }
        });

        setContentView(datePickerView);
        getWindow().setGravity(Gravity.BOTTOM); //显示在屏幕底部
    }

    public void setOnSelectListener(DatePickerDialog.OnSelectListener l) {
        listener = l;
    }

    /**
     * 默认选中指定的日期
     *
     * @param year
     * @param month
     * @param day
     */
    public void setDefaultChoice(int year, int month, int day) {
        datePickerView.setDefaultChoice(year, month, day);
    }

    /**
     * 默认选中今天
     */
    public void setTodayAsDefault() {
        Calendar c = Calendar.getInstance();
        this.setDefaultChoice(
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH) + 1,
                c.get(Calendar.DAY_OF_MONTH));
    }

}
