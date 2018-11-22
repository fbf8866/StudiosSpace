package com.example.magic.myapplication.utils;

import android.os.SystemClock;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/8/18.
 */

public class DoubleClickUtils {
    private static long[] mHits = new long[3];// 实现三连击的数组
    public static boolean isDoubleClick(){
        // 实现三连击点击效果
        System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
        // 实现左移，然后最后一个位置更新距离开机的时间，如果最后一个时间和最开始时间小于500，即双击,大于就是三连击
        mHits[mHits.length - 1] = SystemClock.uptimeMillis();
        boolean flag =  (SystemClock.uptimeMillis() - mHits[0]) < 1000;
        return flag;
    }
}

