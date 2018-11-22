package com.example.magic.myapplication.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.magic.myapplication.MainActivity;
import com.example.magic.myapplication.R;
import com.example.magic.myapplication.adapter.SplashImageAdapter;
import com.example.magic.myapplication.bean.InfoBean;

import java.util.ArrayList;

public class SplashActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "SplashActivity";
    private ArrayList<InfoBean> imageList = new ArrayList<InfoBean>();
    private ViewPager mViewPager;
    private LinearLayout point;
    private TextView tv_pager;
    private Button btn_goin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
        initData();
        initPoint();

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        View decorview = getWindow().getDecorView();
        //1.全屏状态
        int option = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorview.setSystemUiVisibility(option);
        ActionBar actionBar = getSupportActionBar();
        if (null  != actionBar){
            actionBar.hide();//隐藏actionbar
        }
    }

    @Override
    public void initView() {
        mViewPager = (ViewPager) findViewById(R.id.activity_splash_viewpager);
        point = (LinearLayout)findViewById(R.id.activity_splash_point);
        tv_pager = (TextView)findViewById(R.id.activity_splash_tv);
        btn_goin = (Button)findViewById(R.id.btn_goin);
    }

    public void initData() {
        imageList.add(new InfoBean("广告1",R.mipmap.aa+""));
        imageList.add(new InfoBean("广告2",R.mipmap.bb+""));
        SplashImageAdapter mSplashImageAdapter = new SplashImageAdapter(this,imageList);
        mViewPager.setAdapter(mSplashImageAdapter);
        mViewPager.setOnPageChangeListener(pagerchangeListener);
        btn_goin.setOnClickListener(this);
    }

    ViewPager.OnPageChangeListener  pagerchangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //viewpager 滑动的时候改变下面点的样式
            changePagerStyle();
        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    /**
     * 初始化viewpager下面的圆点
     */
    private void initPoint() {
        for (int i = 0; i < imageList.size(); i++) {
            View view = new View(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20, 20);
            params.leftMargin = 10;
            view.setLayoutParams(params);
            view.setBackgroundResource(R.drawable.unfocus);
            // 将view添加到已经创建好的线性布局中
            point.addView(view);
        }
    }

    private void changePagerStyle() {
        int currentItem = mViewPager.getCurrentItem() % imageList.size();
        tv_pager.setText(imageList.get(currentItem).getTitle());
        for (int i = 0; i < imageList.size(); i++) {
            View child = point.getChildAt(i);
            if (i == currentItem) {
                child.setBackgroundResource(R.drawable.focus);
            } else {
                child.setBackgroundResource(R.drawable.unfocus);
            }
        }

        if (currentItem == imageList.size()-1){
            btn_goin.setVisibility(View.VISIBLE);
        }else{
            btn_goin.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_goin:
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

}
