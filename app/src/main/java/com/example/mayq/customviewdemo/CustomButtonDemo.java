package com.example.mayq.customviewdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

/**
 * Created by mayq on 2016/7/20.
 */
public class CustomButtonDemo extends Activity {
    private Label btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_button);

        btn = (Label) this.findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CustomButtonDemo.this, "btn clicked", Toast.LENGTH_SHORT).show();
            }
        });

        //btn.setMyEnabled(false);
    }
}
