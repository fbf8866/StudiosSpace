package com.example.magic.myapplication.utils;

import android.app.Activity;
import android.graphics.Point;
import android.view.Display;

/**
 * Created by Administrator on 2017/3/15.
 * 获取屏幕的宽度和高度
 */

public class GetScreenWidthAndHeight {
    /**
     * 获取屏幕高度
     */
    public static int getDeviceWidth(Activity context){
        Display display = context.getWindowManager().getDefaultDisplay();
        Point p = new Point();
        display.getSize(p);
        return p.x;
    }

    /**获取屏幕的高*/
    public static int getDeviceHeight(Activity context){
        Display display = context.getWindowManager().getDefaultDisplay();
        Point p = new Point();
        display.getSize(p);
        return p.y;
    }
}
