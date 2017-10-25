package com.example.magic.teststudiosdemo.application;

import android.app.Application;
import android.content.Context;

/**
 * Created by Administrator on 2017/5/11.
 */

public class MyApplication extends Application{
    public static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }
}
