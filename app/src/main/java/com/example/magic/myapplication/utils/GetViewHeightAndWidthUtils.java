package com.example.magic.myapplication.utils;

import android.view.View;

/**
 * Created by Administrator on 2017/8/18.
 * 获取控件的宽高
 */

public class GetViewHeightAndWidthUtils {

    public  static int getHeight(View view){
        int intw=View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        int inth=View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        view.measure(intw, inth);
        int intheight = view.getMeasuredHeight();
        return intheight;
    }

    public  static int getWidth(View view){
        int intw=View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        int inth=View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        view.measure(intw, inth);
        int intwidth = view.getMeasuredWidth();
        return intwidth;
    }
}
