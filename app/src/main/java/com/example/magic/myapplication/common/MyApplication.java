package com.example.magic.myapplication.common;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.example.magic.myapplication.R;
import com.example.magic.myapplication.utils.NetsWorkUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.PersistentCookieStore;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.internal.cache.CacheInterceptor;

/**
 * Created by Administrator on 2017/3/24.
 */

public class MyApplication extends Application {
    public static Context context;
    private final int TIMEOUT =1000;

    //对于新增和删除操作add和remove，LinedList比较占优势，因为ArrayList实现了基于动态数组的数据结构，要移动数据。LinkedList基于链表的数据结构,便于增加删除
    private List<Activity> activityList = new LinkedList<Activity>();
    private static MyApplication instance = null;
    public static MyApplication getInstance() {
        return instance;
    }
    //添加Activity到容器中
    public void addActivity(Activity activity)  {
        activityList.add(activity);
    }
    //遍历所有Activity并finish
    public void exit(){
        for(Activity activity:activityList) {
            activity.finish();
        }
        System.exit(0);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        instance = this;
//        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .connectTimeout(TIMEOUT, TimeUnit.SECONDS) /** 设置连接的超时时间*/
//                .writeTimeout(TIMEOUT, TimeUnit.SECONDS).readTimeout(TIMEOUT, TimeUnit.SECONDS)  /*** 设置响应的超时时间*/
//                .readTimeout(3000, TimeUnit.SECONDS)  /*** 请求的超时时间*/
//                .addInterceptor(new LoggerInterceptor("",true))
//                .cookieJar(new CookieJarImpl(new PersistentCookieStore(getApplicationContext())))
//                .build();

//        OkHttpUtils.initClient(okHttpClient);
    }
}
