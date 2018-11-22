package com.example.magic.myapplication.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2017/3/13.
 * Viewpager + Fragment情况下，fragment的生命周期因Viewpager的缓存机制而失去了具体意义
 */

public abstract class BaseFragment  extends Fragment {
    private String TAG = BaseFragment.class.getSimpleName();
    protected View mRootView;
    public Context mContext;
    private boolean isPrepared;
    private boolean isFirst = true;
    /**
     *  Fragment当前状态是否可见
     */
    protected boolean isVisible;

    /**
     * 设置fragment的可见状态
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(getUserVisibleHint()) { //获取fragment的可见状态
            isVisible = true;
            onVisible();
        }
        else {
            isVisible = false;
            onInvisible();
        }
    }

    /**
     * 可见
     */
    protected void onVisible() {
        lazyLoad();
    }
    /**
     * 不可见
     */
    protected void onInvisible() {}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = initView();
        }else{
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            if (null != parent) {
                parent.removeAllViews();
            }
        }
        return mRootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isPrepared = true;
        lazyLoad();
    }

    protected void lazyLoad() {
        if (!isPrepared || !isVisible || !isFirst) {
            return;
        }
        Log.d("TAG", getClass().getName() + "----initData()");
        initData();
        isFirst = false;
    }

    /**
     * 初始化布局
     */
    public abstract View initView();

    /**
     * 加载数据
     */
    public abstract void initData();
}
