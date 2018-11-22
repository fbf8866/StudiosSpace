package com.example.magic.myapplication.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.magic.myapplication.R;
import com.example.magic.myapplication.activity.listener.ScreenShotListenerManager;

import java.lang.reflect.Method;


public class ScreenShotActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "ScreenShotActivity";
    private TextView tv_screenshot;
    private ImageView iv_screenshot;
    private View dview;
    private Bitmap b1;
    private Button btn_screen_shot_again;
    private int width;
    private int height;
    private int statusBarHeight;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_shot);

        initView();
        initData();
    }

    @Override
    public void initData() {
        tv_screenshot.setOnClickListener(this);
        btn_screen_shot_again.setOnClickListener(this);
    }

    @Override
    public void initView() {
        tv_screenshot = (TextView)findViewById(R.id.tv_screenshot);
        iv_screenshot = (ImageView)findViewById(R.id.iv_screenshot);
        btn_screen_shot_again = (Button)findViewById(R.id.btn_screen_shot_again);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        bitmap = screenShot();
    }

    private Bitmap screenShot(){
        // View是你需要截图的View
        dview =getWindow().getDecorView();
        dview.setDrawingCacheEnabled(true);
        dview.buildDrawingCache();
        b1 = dview.getDrawingCache();
        // 获取状态栏高度
        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        statusBarHeight = frame.top;
        // 获取屏幕长和高
        width = getWindowManager().getDefaultDisplay().getWidth();
        height = getWindowManager().getDefaultDisplay().getHeight();
        Bitmap bmp = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height- statusBarHeight);
        Log.i(TAG, dview.getMeasuredHeight()+"screenShot: ---"+dview.getMeasuredWidth());
        dview.destroyDrawingCache();
        dview.setDrawingCacheEnabled(false);
        return bmp;
    }

    int i = 0;
    Bitmap bitmap;
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.tv_screenshot:
                i++;
                bitmap = screenShot();
                iv_screenshot.setImageBitmap(bitmap);
                tv_screenshot.setText("屏幕截屏"+i);
                Log.i(TAG, iv_screenshot.getWidth()+"onClick: "+iv_screenshot.getHeight());
                break;

            case R.id.btn_screen_shot_again:
                bitmap.recycle();
                iv_screenshot.setImageBitmap(bitmap);
                break;
        }
    }

    @Override
    public void backhome() {
        super.backhome();
        finish();
    }
}
