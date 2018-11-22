package com.example.magic.myapplication.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;

import com.example.magic.myapplication.R;
import com.example.magic.myapplication.common.MyApplication;
import com.example.magic.myapplication.utils.MyToastUtils;
import com.example.magic.myapplication.utils.NetUtils;
import com.example.magic.myapplication.utils.StoreInfomationUtils;

/**
 * Created by Administrator on 2017/7/27.
 */

public abstract class BaseActivity extends AppCompatActivity{
    private static final String TAG = "BaseActivity";

    private MyOpenReceiver receiver;
    private NetUtils.NetType apnType;
    private Handler threadHandler;
    private long mExitTime = 0;

    private  Object theme_store; //保存本地的主题
    // 默认是日间模式  这里的style对应自己写的
    public int theme = R.style.AppTheme;

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
        theme_store = StoreInfomationUtils.get(this, "theme", 0);
        setTheme(theme);
        if (null != theme_store){
            if(theme_store instanceof Integer){
                int value = (Integer)theme_store;
                theme = value;
                setTheme(theme);
            }
        }
        MyApplication.getInstance().addActivity(this);

        super.onCreate(savedInstanceState);
    }


    @Override
    protected void onStart() {
        super.onStart();
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
        getApplicationContext().registerReceiver(receiver,filter); //上下文用全局的,否则会偶尔报错说,没有注销广播,但是注销广播的代码已经写了
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

    /**
     * 初始化数据
     */
    public abstract void initData();

    /**
     * 初始化布局
     */
    public abstract void initView();

    /**
     * 返回键默认点击提示,如果想要别的效果 可以重写这个方法
     */
    public  void backhome(){
        if(System.currentTimeMillis() - mExitTime > 2000) {
            //3.保存当前时间
            mExitTime  = System.currentTimeMillis();
            MyToastUtils.show(this,"再次点击退出");
        } else {
            //5.点击的时间差小于2000，退出。
            MyApplication.getInstance().exit();
            System.exit(0);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
            backhome();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null !=receiver){
            getApplicationContext().unregisterReceiver(receiver);
        }
    }

    public  void isNetAvaiable(NetUtils.NetType apnType){}


}
