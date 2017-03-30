package com.example.mayq.customviewdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by mayq on 2016/8/30.
 */
public class IndicatorDemo extends Activity {
    private PageIndicatorView indicatorView;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.indicator_demo);
        indicatorView = (PageIndicatorView) findViewById(R.id.piv);
        indicatorView.setDotNum(6);

        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int size = indicatorView.getDotNum();
                int index = indicatorView.getIndex();
                int next = index + 1;
                if (next == size)
                    next = 0; //wrap
                indicatorView.setIndex(next);
            }
        });
    }
}
