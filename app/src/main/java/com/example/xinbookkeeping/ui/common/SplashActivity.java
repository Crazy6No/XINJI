package com.example.xinbookkeeping.ui.common;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;

import com.example.xinbookkeeping.R;
import com.example.xinbookkeeping.common.SPConstant;
import com.example.xinbookkeeping.ui.user.UserLoginActivity;
import com.example.xinbookkeeping.uibase.BaseFullScreenActivity;

/**
 *
 * 欢迎页
 * */
@SuppressLint("CustomSplashScreen")
public class SplashActivity extends BaseFullScreenActivity {
    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void init() {
        super.init();
        jumpTo();
    }

    private void jumpTo() {
        new Handler().postDelayed(() -> {
            startActivity(SelectRoleActivity.class);
            finish();
        }, 3000);
    }
}
