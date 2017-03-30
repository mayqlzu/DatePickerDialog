package com.example.mayq.customviewdemo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mayq on 2016/7/26.
 */
public class RefreshListViewDemo extends Activity {
    private RefreshListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pull_to_refresh);

        lv = (RefreshListView) findViewById(R.id.lv);
        List<String> data = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            data.add("" + i);
        }

        lv.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data));
        lv.setOnRefreshListener(new RefreshListView.OnRefreshListener() {

            @Override
            public void onRefresh() {
                refresh();
            }

            @Override
            public void loadMore() {
                loadNextPage();
            }

        });

        //lv.setEnableLoadMore(false);
    }

    private void refresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                lv.finishRefresh();
            }
        }, 4 * 1000);
    }

    private void loadNextPage() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                lv.finishLoadMore(true);
            }
        }, 4 * 1000);
    }


}
