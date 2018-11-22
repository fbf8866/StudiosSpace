package com.example.magic.myapplication.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.magic.myapplication.R;
import com.example.magic.myapplication.activity.interfaces.IRecyclerItem;
import com.example.magic.myapplication.activity.interfaces.IRecyclerItemChecked;
import com.example.magic.myapplication.activity.interfaces.IRecyclerItemLongClick;
import com.example.magic.myapplication.activity.interfaces.IStore;
import com.example.magic.myapplication.adapter.MyStoreAdapter;
import com.example.magic.myapplication.bean.VideoItemBean;
import com.example.magic.myapplication.common.CustomToolBar;
import com.example.magic.myapplication.common.DrawItemLine;
import com.example.magic.myapplication.common.RecyclerViewLinearlayoutManager;
import com.example.magic.myapplication.fragment.VideoFragment;
import com.example.magic.myapplication.utils.MyToastUtils;
import com.example.magic.myapplication.utils.SharedUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 用于显示收藏的页面
 */
public class StoreActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "StoreActivity";
    private CustomToolBar store_activity_toolbar;
    private RecyclerView mystore_recyclerview;
    private RecyclerViewLinearlayoutManager manager;
    private Gson gson;
    private MyStoreAdapter myAdapter;
    //用于存放checkbox勾选的集合
    private List<VideoItemBean> cbCheckedList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        initView();
        initData();
    }

    @Override
    public void initView() {
        gson = new Gson();
        store_activity_toolbar = (CustomToolBar) findViewById(R.id.toolbar);
        store_activity_toolbar.hideRightDrawable();
        store_activity_toolbar.setCustomTitle("我的收藏");
        store_activity_toolbar.setCustomTitleColor(Color.WHITE);
        store_activity_toolbar.setCustomTitleSize(18);
        store_activity_toolbar.leftButtonOnClick(this);
        mystore_recyclerview = (RecyclerView) findViewById(R.id.mystore_crecyclerview);
        //如果确定每个item内容不会改变recyclerview 的大小, 那么设置这个可以提高性能
        mystore_recyclerview.setHasFixedSize(true);
        manager = new RecyclerViewLinearlayoutManager(this,1);
        manager.setOrientation(RecyclerView.VERTICAL);
        mystore_recyclerview.setLayoutManager(manager);
        //添加分割线,类似listview的deliver
        mystore_recyclerview.addItemDecoration(new DrawItemLine(this, LinearLayoutManager.HORIZONTAL));

    }

    @Override
    public void initData() {
        String store_position = (String) SharedUtils.getInstance(this).get("store_position", "");
        //将gson字符串转为集合
        final List<VideoItemBean> l = gson.fromJson(store_position, new TypeToken<List<VideoItemBean>>() {}.getType());
        if (null != l && l.size() > 0){
            myAdapter = new MyStoreAdapter(this,l);
            mystore_recyclerview.setAdapter(myAdapter);
            myAdapter.setMyItemLongClickListener(new IRecyclerItemLongClick() {
                @Override
                public void onItemLongClick(int position) {

//            list.remove(list.get(position));
//            adapter.notifyItemRemoved(position);
//            //这行是必须的,否则position 不会变
//            adapter.notifyItemRangeChanged(0,list.size()- position);
                }
            });

            myAdapter.setMyItemClickListener(new IRecyclerItem() {
                @Override
                public void onItemClick(View view, int position) {
                    String videoUrl = l.get(position).getVideoUrl();
                    Intent intent = new Intent(StoreActivity.this, VideoPlayActivity.class);
                    intent.putExtra("videoUrl",videoUrl);
                    startActivity(intent);
                }
            });

            myAdapter.setMyItemCheckedListener(new IRecyclerItemChecked() {
                @Override
                public void itemChecked(int position, boolean isChecked) {
                    if (isChecked){
                        cbCheckedList.add(l.get(position));
                        MyToastUtils.show(StoreActivity.this,"勾选成功");
                    }else{
                        cbCheckedList.remove(l.get(position));
                        MyToastUtils.show(StoreActivity.this,"取消勾选");
                    }
                }
            });

        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.custom_child_left_img:
                finish();
                break;
        }
    }
}
