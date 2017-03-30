package com.example.mayq.customviewdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * 一个Demo，演示DatePickerDialog的用法
 */
public class DatePickDialogDemo extends Activity {
    private Button btn;
    private int mYear;
    private int mMonth;
    private int mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date_picker_dialog_demo);

        btn = (Button) this.findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDlg();
            }
        });
    }

    private void showDlg() {
        // 1 实例化对话框对象
        DatePickerDialog dlg = new DatePickerDialog(this);

        // 2 注册回调
        dlg.setOnSelectListener(new DatePickerDialog.OnSelectListener() {
            @Override
            public void onSelect(int year, int month, int day) {
                // 处理回调事件
                mYear = year;
                mMonth = month;
                mDay = day;

                btn.setText(year + " " + month + " " + day);
            }
        });

        // 3 设置默认选中的项
        if (0 == mYear) {
            dlg.setTodayAsDefault();
        } else {
            dlg.setDefaultChoice(mYear, mMonth, mDay);
        }

        // 4 显示
        dlg.show();
    }

}
