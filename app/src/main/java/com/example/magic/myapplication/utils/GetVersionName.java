package com.example.magic.myapplication.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * 获取版本号的工具类
 * Created by Administrator on 2017/4/7.
 */

public class GetVersionName{
    public static int getVersionName(Context context){
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            int version = info.versionCode;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }
}

