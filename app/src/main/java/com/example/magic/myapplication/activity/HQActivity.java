package com.example.magic.myapplication.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import com.example.magic.myapplication.R;
//import com.example.magic.myapplication.adapter.ContentRecyclerAdapter;
import com.example.magic.myapplication.adapter.LeftRecyclerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 有bug,快速滑动 时候  不同步
 */
public class HQActivity extends BaseActivity {
    private static final String TAG = "HQActivity";
    //左侧模拟数据
    private List<String> left_recycler_data = new ArrayList<>();
    //右侧顶部模拟title数据
    private String[] title_ll_data = new String[]{"最新","涨幅","涨跌","涨速","总手","成交"};
    //右侧底部内容的模拟数据
    private List<String> content_recycler_data = new ArrayList<>();
    private RecyclerView left_recycler;
    private LinearLayout title_ll;
    private ScrollView content_right_scrollview;
    private LinearLayoutManager leftManager;
    private Activity mThis = this;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hq);
        initView();
        initData();
    }

    @Override
    public void initView() {
        //左侧recycler
        left_recycler = (RecyclerView) findViewById(R.id.left_recycler);
         leftManager = new LinearLayoutManager(this);
        left_recycler.setLayoutManager(leftManager);
        left_recycler.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        //右侧顶部title
        title_ll = (LinearLayout) findViewById(R.id.title_ll);
        content_right_scrollview = (ScrollView) findViewById(R.id.content_right_scrollview);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void initData() {
        for (int i=0;i<100;i++){
            left_recycler_data.add(i+"--左侧");
        }

        LeftRecyclerAdapter leftRecyclerAdapter = new LeftRecyclerAdapter(this,left_recycler_data);
        left_recycler.setAdapter(leftRecyclerAdapter);

        //右侧title
        for (int i=0;i<title_ll_data.length;i++){
            View item_title = LayoutInflater.from(this).inflate(R.layout.item_title_ll, null);
            TextView item_title_ll_tv = (TextView) item_title.findViewById(R.id.item_title_ll_tv);
            item_title_ll_tv.setText(title_ll_data[i]);
            title_ll.addView(item_title);
        }


        try {
            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            for (int i=0;i<title_ll_data.length;i++){
                jsonArray.put(i,i+"左侧");
            }
            jsonObject.put("param",jsonArray);
            String s = jsonObject.toString();
            for (int i =0;i<100;i++){
                content_recycler_data.add(s);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //右侧底部
        final LinearLayout content_ll = (LinearLayout) findViewById(R.id.content_ll);
        final LinearLayout item_layout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.container,null);
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int j=0;j<left_recycler_data.size();j++){
                    final LinearLayout linearLayout = new LinearLayout(mThis);
                    linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                    linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    for (int i=0;i<title_ll_data.length;i++){
                        View item_title = LayoutInflater.from(mThis).inflate(R.layout.item_title_ll, null);
                        TextView item_title_ll_tv = (TextView) item_title.findViewById(R.id.item_title_ll_tv);
                        try {
                            JSONObject jsonObject = new JSONObject(content_recycler_data.get(i));
                            JSONArray param = jsonObject.getJSONArray("param");
                            item_title_ll_tv.setText( param.getString(i));
                            linearLayout.addView(item_title);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            item_layout.addView(linearLayout);
                        }
                    });

                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        content_ll.addView(item_layout);
                    }
                });

            }
        }).start();



        content_right_scrollview.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int offsetY = scrollY - oldScrollY;
                int offsetX = scrollX - oldScrollX;
                Log.i(TAG, "onScrollChange: 滑动距离--"+offsetX);
                left_recycler.scrollBy(offsetX,offsetY);
            }
        });

        left_recycler.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //获取第一个可见
                int position = leftManager.findFirstVisibleItemPosition();
                View firstVisiableChildView = leftManager.findViewByPosition(position);
                int itemHeight = firstVisiableChildView.getHeight();
                //recyclerview 的偏移量
                int offset = (position) * itemHeight - firstVisiableChildView.getTop();
            }
        });

    }


}
