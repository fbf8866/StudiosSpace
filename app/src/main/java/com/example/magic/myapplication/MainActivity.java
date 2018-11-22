package com.example.magic.myapplication;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.magic.myapplication.activity.BaseActivity;
import com.example.magic.myapplication.activity.HQActivity;
import com.example.magic.myapplication.activity.SearchActivity;
import com.example.magic.myapplication.adapter.MyAdapter;
import com.example.magic.myapplication.common.CustomButton;
import com.example.magic.myapplication.common.CustomToolBar;
import com.example.magic.myapplication.common.GlideCircleTransform;
import com.example.magic.myapplication.fragment.ChatFragment;
import com.example.magic.myapplication.fragment.GrilFragment;
import com.example.magic.myapplication.fragment.NewsFragment;
import com.example.magic.myapplication.fragment.SetFragment;
import com.example.magic.myapplication.fragment.VideoFragment;
import com.example.magic.myapplication.utils.CameraUtils;
import com.example.magic.myapplication.utils.CleanCacheUtils;
import com.example.magic.myapplication.utils.DialogUtils;
import com.example.magic.myapplication.utils.GetScreenWidthAndHeight;
import com.example.magic.myapplication.utils.MyToastUtils;
import com.example.magic.myapplication.utils.OpenOrCloseAnimatorUtil;
import com.example.magic.myapplication.utils.StoreInfomationUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener{
    private String TAG = MainActivity.class.getSimpleName();
    private String[] TITLE = new String[]{"新闻","聊天","设置","女人天下","直播"};
    private int[] IMAGE  = new int[]{R.drawable.news,R.drawable.chat,R.drawable.set
    ,R.drawable.women,R.drawable.video};
    private ViewPager mViewPager;
    private List<Fragment> fragmentList = new ArrayList<Fragment>();
    private Button btn_take_pic;
    private Button btn_get_pic;
    private  ImageView iv_showpic;
    private Uri uri;
    private TabLayout tabLayout;
    private  MyAdapter adapter;
    private CustomToolBar toorbar;
    private DrawerLayout mDrawerLayout;
    private Dialog icon_people; //显示拍照还是相册的dialog
    private Button btn_cancle;
    private Button btn_clean_cache;
    private TextView tv_show_cache;
    private String totalCacheSize; //缓存大小
    private CleanCacheUtils cleanCacheUtils;
    // 默认是日间模式  这里的style对应自己写的
//    private int theme = R.style.AppTheme;
    private CustomButton day_night_custombutton;
    private FrameLayout view_framelayout;
    private  Object isopen; //保存本地的夜间模式的开关
    private boolean isopens; //类型转化后的夜间模式开关
    private View view_frame;
    private float downX;
    private float downY;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 判断是否有主题存储
        if(savedInstanceState != null){
            theme = savedInstanceState.getInt("theme");
            Log.i(TAG, "onCreate: 主题"+theme);
            setTheme(theme);
        }
        setContentView(R.layout.activity_main_app);

        initFragment();
        initView();
        initData();

    }

    private void initFragment() {
        Fragment newFragment = new NewsFragment();
        Fragment chatFragment = new ChatFragment();
        Fragment setFragment = new SetFragment();
        Fragment grilFragment = new GrilFragment();
        Fragment videoFragment = new VideoFragment();

        fragmentList.add(newFragment);
        fragmentList.add(chatFragment);
        fragmentList.add(setFragment);
        fragmentList.add(grilFragment);
        fragmentList.add(videoFragment);
    }

    public void initView() {
        mDrawerLayout = (DrawerLayout)findViewById(R.id.mDrawerLayout);
        //设置锁定模式,可以禁用手势滑动,只能点击左上角按钮弹出抽屉
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        // 点击除开侧边栏的区域会收起侧边栏。
        mDrawerLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mDrawerLayout.closeDrawers();
                        break;
                }
                return false;
            }
        });
        tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        toorbar = (CustomToolBar)findViewById(R.id.toolbar);
        toorbar.leftButtonOnClick(leftlistener);
        toorbar.setLeftDrawable(getResources().getDrawable(R.drawable.left));
        toorbar.rightButtonOnClick(rightlistener);
        mViewPager = (ViewPager)findViewById(R.id.mViewPager);
        adapter = new MyAdapter(getSupportFragmentManager(),fragmentList,TITLE,IMAGE,this);
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setupWithViewPager(mViewPager);
        //设置文字默认颜色和选中后的颜色
        tabLayout.setTabTextColors(Color.BLACK,Color.RED);
        resetTablayout();
        tabLayout.getTabAt(0).getCustomView().setSelected(true);
        //解决点击tab时,viewpager过度动画的问题
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // 默认切换的时候，会有一个过渡动画，设为false后，取消动画，直接显示
                mViewPager.setCurrentItem(tab.getPosition(), false);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        iv_showpic = (ImageView) findViewById(R.id.iv_showpic);
        iv_showpic.setOnClickListener(this);
        icon_people = DialogUtils.getInstance().creatIconPeopleDialog(MainActivity.this);

        btn_take_pic = (Button)icon_people.findViewById(R.id.btn_take_pic);
        btn_take_pic.setOnClickListener(this);
        btn_get_pic = (Button)icon_people.findViewById(R.id.btn_get_pic);
        btn_get_pic.setOnClickListener(this);
        btn_cancle = (Button)icon_people.findViewById(R.id.btn_cancle);
        btn_cancle.setOnClickListener(this);

        btn_clean_cache = (Button) findViewById(R.id.btn_clean_cache);
        btn_clean_cache.setOnClickListener(this);
        tv_show_cache = (TextView)findViewById(R.id.tv_show_cache);

        view_frame = findViewById(R.id.view_frame);


        //设置夜间或者白天
        day_night_custombutton = (CustomButton)findViewById(R.id.day_night_custombutton);
        day_night_custombutton.setShapeType(2);
        isopen = StoreInfomationUtils.get(MainActivity.this, "isopen", false);
        if (null != isopen){
            if (isopen instanceof Boolean){
                isopens = (boolean)isopen;
                day_night_custombutton.setState(isopens);
            }
        }
        OpenOrCloseAnimatorUtil.setLayoutParamsZero(view_frame);
        day_night_custombutton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                float x = motionEvent.getX();
                float y = motionEvent.getY();
                switch(motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        downX = x;
                        downY = y;
                        break;
                    case  MotionEvent.ACTION_UP:
                        float dx = x - downX;
                        float dy = y - downY;
                        if (Math.abs(dx)<3 ){ //按下的时候也加载覆盖的view
                            OpenOrCloseAnimatorUtil.toggleContentAnimator(true,view_frame, GetScreenWidthAndHeight.getDeviceHeight(MainActivity.this));
                            Log.i(TAG, dx+ "--onTouch: ---  dx --1-"+isopens);
                        }
                        String orientation = dx>0?  "right":"left";
                        switch (orientation){ //向右滑,并且是开着时候和向左滑并且关着的时候,不会加载覆盖的view
                            case "right":
                                if (!isopens){
                                    OpenOrCloseAnimatorUtil.toggleContentAnimator(true,view_frame, GetScreenWidthAndHeight.getDeviceHeight(MainActivity.this));
                                }
                                Log.i(TAG,  dx+ "--onTouch: ---  dx --2-"+isopens);
                                break;
                            case  "left":
                               if (isopens){
                                   OpenOrCloseAnimatorUtil.toggleContentAnimator(true,view_frame, GetScreenWidthAndHeight.getDeviceHeight(MainActivity.this));
                                   Log.i(TAG,  dx+ "--onTouch: ---  dx --3-"+isopens);
                               }
                                break;
                        }
                        break;
                }

                return false;
            }
        });

        try{ //这里加try catch为了防止清除缓存的时候,更改主题, 会报错找不到资源文件
            day_night_custombutton.setSlideListener(new CustomButton.SlideListener() {
                @Override
                public void open() {
                    setDayOrNightMode(true);
                }

                @Override
                public void close() {
                    setDayOrNightMode(false);
                }
            });
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public void initData() {
        //清除缓存的工具类
        cleanCacheUtils = new CleanCacheUtils(this);
        showCacheSize();

        Object iconPeopleUri = StoreInfomationUtils.get(this, "uri", "");
        if (null != iconPeopleUri && !"".equals(iconPeopleUri)){
            Glide.with(this).load(iconPeopleUri).transform(new GlideCircleTransform(MainActivity.this)).into(iv_showpic);
        }
    }

    /**
     * 显示缓存大小
     */
     private void showCacheSize(){
         try {
             totalCacheSize = cleanCacheUtils.getCacheSize();
             tv_show_cache.setText(totalCacheSize);
         } catch (Exception e) {
             e.printStackTrace();
         }
     }
    /**
     * Tablayout自定义view
     * 使用这个方法可以解决图片在文字上方或者下方的问题,当然要在adapter中使用getTabView的那个方法
     * 如果只是单纯文字的标题,或者图片在文字的左右方, 直接在adapter中重写getPageTitle方法就可以
     */
    private void resetTablayout() {
        for (int i=0;i<tabLayout.getTabCount();i++){
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab!=null){
                tab.setCustomView(adapter.getTabView(i));
            }
        }
    }
    /**
     * 打开相册
     */
    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,2);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    openAlbum();
                }else{
                    MyToastUtils.show(MainActivity.this,"没有权限,请开启权限");
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1: //拍照
                if (resultCode == RESULT_OK){
                    //这是直接转化为图片
//                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));

                    //用三方图片加载库glide来加载圆形图片
                        Glide
                                .with(MainActivity.this)
                                .load(uri)
                                .transform(new GlideCircleTransform(MainActivity.this))
                                .into(iv_showpic);
                    StoreInfomationUtils.put(MainActivity.this,"uri",uri);
                    showCacheSize();
                }
                break;

            case 2: //本地相册
                if(resultCode == RESULT_OK){
                    String imagePath;
                    //4.4以上
                    if (Build.VERSION.SDK_INT >= 19){
                        imagePath = CameraUtils.getInstance(MainActivity.this).getImageOnKitKat(data);
                        Log.i(TAG, "1 onActivityResult: "+Build.VERSION.SDK_INT);
                    }else{
                        imagePath = CameraUtils.getInstance(MainActivity.this).getImageBeforeKitKat(data);
                        Log.i(TAG, "2 onActivityResult: "+Build.VERSION.SDK_INT);
                    }
                    uri = Uri.fromFile(new File(imagePath));
                    Glide
                            .with(MainActivity.this)
                            .load(uri)
                            .transform(new GlideCircleTransform(MainActivity.this))
                            .into(iv_showpic);
                    StoreInfomationUtils.put(MainActivity.this,"uri",uri);
                    showCacheSize();
                }
                break;
        }
    }

    //左侧按钮的点击事件
    View.OnClickListener leftlistener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mDrawerLayout.openDrawer(Gravity.LEFT);
        }
    };

    //右侧按钮的点击事件
    View.OnClickListener rightlistener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this,SearchActivity.class);
            startActivity(intent);

        }
    };


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_showpic:
                icon_people.show();
                break;
            case R.id.btn_take_pic: //拍照
                uri = CameraUtils.getInstance(MainActivity.this).takePhotos("com.example.magic.myapplication.provider");
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
                startActivityForResult(intent,1);
                icon_people.dismiss();
                break;

            case R.id.btn_get_pic: //本地相册选择
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }else{
                    openAlbum();
                }
                icon_people.dismiss();
                break;

            case R.id.btn_cancle: //取消
                icon_people.dismiss();
                break;

            case R.id.btn_clean_cache: //清除缓存
                StoreInfomationUtils.clear(MainActivity.this);
                cleanCacheUtils.clearAppCache();
                showCacheSize();
                break;

        }
    }


    /**
     * 设置白天夜晚模式
     */
    private void setDayOrNightMode(Boolean flag){
        if (theme == R.style.moonAppTheme){
            theme = R.style.AppTheme;
        }else{
            theme = R.style.moonAppTheme;
        }
        StoreInfomationUtils.put(MainActivity.this,"isopen",flag);
        StoreInfomationUtils.put(MainActivity.this,"theme",theme);
        MainActivity.this.recreate();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) { //备份数据
        super.onSaveInstanceState(outState);
        outState.putInt("theme", theme);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) { //还原数据
        super.onRestoreInstanceState(savedInstanceState);
        theme = savedInstanceState.getInt("theme");
    }


}
