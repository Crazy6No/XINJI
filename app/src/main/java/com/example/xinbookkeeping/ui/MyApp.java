package com.example.xinbookkeeping.ui;

import android.app.Application;

public class MyApp extends Application {

    private static MyApp mApp;

    public static MyApp getInstance() {
        return mApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
    }

}
