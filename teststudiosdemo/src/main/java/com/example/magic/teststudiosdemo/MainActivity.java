package com.example.magic.teststudiosdemo;

import android.content.Context;
import android.media.Image;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.LoginFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.magic.teststudiosdemo.urils.MyToast;
import com.example.magic.teststudiosdemo.urils.NetUtils;
import com.example.magic.teststudiosdemo.urils.SaveFileUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivitys";
    private TextView tv;
    private ImageView iv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        ActionBar supportActionBar = getSupportActionBar();
//        supportActionBar.setTitle("登录");
        initView();

        String data = SaveFileUtil.getInstance(this).getAsFile("data");
        MyToast.show(this,data);

        //邮箱校验
        Pattern p = Pattern.compile("[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?");
        Matcher matcher =p.matcher("fanbingfeng0601@rayootech.co");
        boolean matches = matcher.matches();
        Log.i(TAG, "onCreate: "+matches);


//        boolean ping = NetUtils.isConnectNetwork();
//        MyToast.show(this,ping+"网络");
    }

    private void initView() {
        tv = (TextView) findViewById(R.id.tv);
        iv = (ImageView) findViewById(R.id.iv);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        /**
         * 此方法用于初始化菜单，其中menu参数就是即将要显示的Menu实例。 返回true则显示该menu,false 则不显示;
         * */
        getMenuInflater().inflate(R.menu.menu_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_add:
                Log.i(TAG, "onOptionsItemSelected: 添加");
                break;

            case R.id.menu_item_delete:
                Log.i(TAG, "onOptionsItemSelected: 删除");
                break;
        }
        return true;
    }

    @Override
    public void isNetAvaiable(NetUtils.NetType apnType) {
        super.isNetAvaiable(apnType);
        if (apnType == NetUtils.NetType.WIFI){
           tv.setText("wifi网络可用");
            MyToast.show(this,"wifi网络可用");
        }else if (apnType == NetUtils.NetType.NONE){
            tv.setText("网络不可用");
            MyToast.show(this,"没有网络可用");
        }else{
            tv.setText("移动数据可用");
            MyToast.show(this,"移动网络可用");
        }
    }
}
