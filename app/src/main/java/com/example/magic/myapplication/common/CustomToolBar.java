package com.example.magic.myapplication.common;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.magic.myapplication.R;

/**
 * Created by Administrator on 2017/7/26.
 */

/**
 * 自定义ToolBar
 */
public class CustomToolBar extends Toolbar {
    private static final String TAG = "CustomToolBar";
    private View mView;
    private ImageView custom_child_search;
    private ImageView custom_child_left_img;
    private Drawable drawable_right;
    private Drawable drawable_left;
    private TextView custom_child_title;

    public CustomToolBar(Context context) {
        super(context);
        initView();
        Log.i(TAG, "CustomToolBar: 1");
    }

    public CustomToolBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
        Log.i(TAG, "CustomToolBar: 2");
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.custom_toolbar);
        drawable_left = typedArray.getDrawable(R.styleable.custom_toolbar_left_img);
        if (null != drawable_left){
            setLeftDrawable(drawable_left);
        }
        drawable_right = typedArray.getDrawable(R.styleable.custom_toolbar_right_img);
        if (null != drawable_right){
            setRightDrawable(drawable_right);
        }
        typedArray.recycle();
    }

    public CustomToolBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        if (null == mView){
            mView = LayoutInflater.from(getContext()).inflate(R.layout.custom_toolbar,null);
        }
        custom_child_search = (ImageView) mView.findViewById(R.id.custom_child_search);
        custom_child_left_img = (ImageView) mView.findViewById(R.id.custom_child_left_img);
        custom_child_title = (TextView) mView.findViewById(R.id.custom_child_title);
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(mView, lp);
    }

    //设置title的文字
    public void setCustomTitle(String title){
        custom_child_title.setText(title);
    }

    //设置title的字体颜色
    public void setCustomTitleColor(int color){
        custom_child_title.setTextColor(color);
    }

    //设置title的字体大小
    public void setCustomTitleSize(float size){
        custom_child_title.setTextSize(size);
    }

    //左侧返回按钮点击
    public void leftButtonOnClick(OnClickListener listener){
        custom_child_left_img.setOnClickListener(listener);
    }

    //设置左侧图片
    public void setLeftDrawable(Drawable drawable){
        custom_child_left_img.setImageDrawable(drawable);
    }

    //隐藏左侧图片
    public void hideLeftDrawable(){
        custom_child_left_img.setVisibility(View.GONE);
    }

    //右侧搜索按钮的点击
    public void rightButtonOnClick(OnClickListener listener){
        custom_child_search.setOnClickListener(listener);
    }

    //设置右侧的图片
    public void setRightDrawable(Drawable drawable){
        custom_child_search.setImageDrawable(drawable);
    }

    //隐藏右侧图片
    public void hideRightDrawable(){
        custom_child_search.setVisibility(View.GONE);
    }
}
