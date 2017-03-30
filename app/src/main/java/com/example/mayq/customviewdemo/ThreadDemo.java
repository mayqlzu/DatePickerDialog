package com.example.mayq.customviewdemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by mayq on 2016/8/30.
 */
public class ThreadDemo extends Activity {
    private Button btn;
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private Future<String> future;
    private static int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.thread_demo);
        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != future) {
                    future.cancel(true);
                }

                future = executor.submit(new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        counter++;
                        try {
                            final String result = doLongRunningOperation();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.d("mayq", "thread" + counter + " finished");
                                    btn.setText("" + counter);
                                }
                            });
                            return result;
                        } catch (InterruptedException e) {
                            Log.d("mayq", "thread" + counter + " cancelled");
                            return null;
                        }
                    }
                });
            }

            private String doLongRunningOperation() throws InterruptedException {
                Thread.sleep(3 * 1000);
                return "i am done";
            }
        });
    }
}
