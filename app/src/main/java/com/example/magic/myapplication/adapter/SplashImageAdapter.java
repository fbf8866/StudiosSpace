package com.example.magic.myapplication.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.magic.myapplication.bean.InfoBean;

import java.util.ArrayList;

import static com.example.magic.myapplication.common.MyApplication.context;

/**
 * Created by Administrator on 2017/10/30.
 */

public class SplashImageAdapter extends PagerAdapter {
    private Context context;
    private  ArrayList<InfoBean> list;
    public SplashImageAdapter(Context context, ArrayList<InfoBean> list){
        this.context = context;
        this.list = list;
    }
    @Override
    public int getCount() {
        //这里可以直接返回一个很大的数值,可以使轮播图一直轮播
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView mImageView = new ImageView(context);
        // 设置图片根据imageView来将图片进行拉伸
        mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        ViewGroup parent = (ViewGroup) mImageView.getParent();
        if (null != parent) {
            parent.removeAllViews();
        }
        // 当前页面为第几页(60000页) 然后除以集合的长度 取余数就是当前页面在list集合中对应图片的位置
        mImageView.setImageResource(Integer.parseInt(list.get(position % list.size()).getIconId()));
        container.addView(mImageView);
        return mImageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
