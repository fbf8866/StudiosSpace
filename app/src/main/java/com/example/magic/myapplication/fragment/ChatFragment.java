package com.example.magic.myapplication.fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.example.magic.myapplication.R;
import com.example.magic.myapplication.common.CustomView;

/**
 * Created by Administrator on 2017/3/13.
 */

public class ChatFragment extends BaseFragment {
    private String TAG = ChatFragment.class.getSimpleName();
    private View view;
    private CustomView customView;
    @Override
    public View initView() {
        if (null == view) {
            view =  LayoutInflater.from(mContext).inflate(R.layout.fragment_chat, null);
        }
        customView = (CustomView)view.findViewById(R.id.customview);
        return view;
    }

    @Override
    public void initData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "一会加载"+TAG);
                //放开后 可以一直从小到大画圆
//                new Thread(customView).start();
            }
        }, 5000);

    }

}
