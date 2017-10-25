package com.example.magic.teststudiosdemo.urils;

import android.app.Activity;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/11.
 * Activity管理类
 */
public class ActivityCollector {
    public static ArrayList<Activity> activities = new ArrayList<>();
    public static void addActivity(Activity activity){
        activities.add(activity);
    }
    public static void removeActivity(Activity activity){
        activities.remove(activity);
    }

    /**
     * 想要退出程序的时候,在activity中调用这个方法就可以了
     */
    public static void removeAll(){
        for (Activity a: activities){
            if(!a.isFinishing()){
                a.finish();
            }
        }
    }
}
