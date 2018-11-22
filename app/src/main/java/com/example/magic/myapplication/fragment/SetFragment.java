package com.example.magic.myapplication.fragment;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.example.magic.myapplication.R;
import com.example.magic.myapplication.common.CustomPb;
import com.example.magic.myapplication.utils.MyToastUtils;

/**
 * Created by Administrator on 2017/3/13.
 */

public class SetFragment extends BaseFragment {
    private String TAG = SetFragment.class.getSimpleName();
    private View view;
    private CustomPb custom_pb;
    private Handler mHandler;
    private HandlerThread thread;
    private  int pro = 0;
    /**
     * 是否在下载
     */
    private boolean isDown = false;
    /**
     * 最大进度
     */
    private int MAX = 100;

    @Override
    public View initView() {
        if (null == view) {
            view =  LayoutInflater.from(mContext).inflate(R.layout.fragment_set, null);
        }
        custom_pb = (CustomPb) view.findViewById(R.id.custom_pb);
        initThread();
        return view;
    }

    @Override
    public void initData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "一会加载"+TAG);

            }
        }, 5000);

        custom_pb.setOnCustomClickListener(new CustomPb.onCustomPbClickListener() {
            @Override
            public void onClick() {
                if (isDown){
                    isDown = false;
                }else{
                    isDown = true;
                    mHandler.sendEmptyMessage(1);
                }
            }
        });
    }

    private void initThread(){
        thread = new HandlerThread("progress");
        thread.start();
        mHandler = new Handler(thread.getLooper()){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (null != msg){
                    if (msg.what == 1){
                        while(isDown){
                            try {
                                if (pro<MAX){
                                    pro += 1;
                                }else{
                                    isDown = false;
                                }
                                Message message = mHandler.obtainMessage();
                                message.obj  = pro;
                                h.sendMessage(message);
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        };
    }

    Handler h = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (null != msg){
                int progress = (int) msg.obj;
                custom_pb.setProgress(progress);
            }
        }
    };

}
