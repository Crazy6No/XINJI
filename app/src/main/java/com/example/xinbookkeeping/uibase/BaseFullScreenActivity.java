package com.example.xinbookkeeping.uibase;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;

/**
 *
 * 全屏activity
 * */
public abstract class BaseFullScreenActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setFullScreenActivity();
        super.onCreate(savedInstanceState);
    }

    protected void setFullScreenActivity() {
        //去掉标题栏
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉信息栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
