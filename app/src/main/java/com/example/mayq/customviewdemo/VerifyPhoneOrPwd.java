package com.example.mayq.customviewdemo;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by mayq on 2016/8/2.
 * 支付的时候 ，验证手机号或者支付密码的对话框，
 * 因为逻辑不少，所以干脆做成一个Dialog风格的Activity吧；
 */
public class VerifyPhoneOrPwd extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_phone_or_pwd);
    }
}
