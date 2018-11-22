package com.example.magic.myapplication.utils;

import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by Administrator on 2017/3/20.
 */

public class GetMidScreenPoint {
    public static int[] getMid(Context m_context) {
// 1.获取屏幕的宽高
        WindowManager wm = (WindowManager) m_context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;

// 2.获取状态栏的高度
        Rect frame = new Rect();
        int stateHeight = frame.top;

// 3.计算屏幕中心点的坐标
        int cx = width  / 2;
        int cy = (height - stateHeight ) / 2;
        int[] s = new int[2];
        s[0] = cx;
        s[1] = cy;
        return s;
    }
}
