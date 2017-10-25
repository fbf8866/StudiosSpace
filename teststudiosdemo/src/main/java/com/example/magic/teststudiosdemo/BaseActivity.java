package com.example.magic.teststudiosdemo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.magic.teststudiosdemo.urils.ActivityCollector;
import com.example.magic.teststudiosdemo.urils.MyToast;
import com.example.magic.teststudiosdemo.urils.NetUtils;
import com.example.magic.teststudiosdemo.urils.SaveFileUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Administrator on 2017/5/11.
 */

public class BaseActivity  extends AppCompatActivity {
    private static final String TAG = "BaseActivity";
    private MyOpenReceiver receiver;
    private NetUtils.NetType apnType;
    private Handler threadHandler;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            boolean flag = (boolean) msg.obj;
            if (flag){
                apnType = NetUtils.NetType.WIFI;
            }else{
                apnType = NetUtils.NetType.NONE;
            }
            isNetAvaiable(apnType);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        HandlerThread thread = new HandlerThread("checkstate");
        thread.start();
        threadHandler = new Handler(thread.getLooper()){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (null != msg){
                    if (1 == msg.what){
                        boolean connectNetwork = NetUtils.isConnectNetwork();
                        Message message = threadHandler.obtainMessage();
                        message.obj = connectNetwork;
                        handler.sendMessage(message);
                    }
                }
            }
        };

        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new MyOpenReceiver();
        registerReceiver(receiver,filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        ActivityCollector.removeActivity(this);
        SaveFileUtil.getInstance(this).saveAsFile("data","你好啊");
    }

    class MyOpenReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.net.conn.CONNECTIVITY_CHANGE".equals(action)) {
                apnType = NetUtils.getAPNType(context);
                if (apnType == NetUtils.NetType.WIFI){
                    threadHandler.sendEmptyMessage(1);
                }else{
                    isNetAvaiable(apnType);
                }
            }
        }
    }


    public  void isNetAvaiable(NetUtils.NetType apnType){}
}
