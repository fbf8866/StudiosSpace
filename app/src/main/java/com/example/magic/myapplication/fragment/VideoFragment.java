package com.example.magic.myapplication.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.magic.myapplication.R;
import com.example.magic.myapplication.activity.VideoPlayActivity;
import com.example.magic.myapplication.activity.interfaces.IRecyclerItem;
import com.example.magic.myapplication.adapter.MyNewsAdapter;
import com.example.magic.myapplication.adapter.MyVideoAdapter;
import com.example.magic.myapplication.bean.VideoItemBean;
import com.example.magic.myapplication.common.DrawItemLine;
import com.example.magic.myapplication.common.RecyclerViewLinearlayoutManager;
import com.example.magic.myapplication.cookie.LogInterceptor;
import com.example.magic.myapplication.utils.DateUtils;
import com.example.magic.myapplication.utils.MyToastUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_DRAGGING;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_SETTLING;
import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_FLING;

/**
 * Created by Administrator on 2017/3/15.
 */

public class VideoFragment extends BaseFragment {
    private static final String TAG = "VideoFragment";
    private View view;
    private RecyclerView video_recycler;
    private RecyclerViewLinearlayoutManager manager;
    private MyVideoAdapter adapter;
    private List<VideoItemBean> itemList = new ArrayList<VideoItemBean>();

    @Override
    public View initView() {
        if (null == view){
            view = LayoutInflater.from(mContext).inflate(R.layout.fragment_video,null);
        }
        video_recycler = (RecyclerView) view.findViewById(R.id.video_recycler);
        video_recycler.setHasFixedSize(true);
        manager = new RecyclerViewLinearlayoutManager(mContext,1);
        manager.setOrientation(RecyclerView.VERTICAL);
        video_recycler.setLayoutManager(manager);
        for (int i = 1; i < 20; i++) {
            itemList.add(new VideoItemBean("马友友吃货的日常记录,搞笑,哈哈哈哈哈哈哈哈哈哈啊马友友吃货的日常记录,搞笑,哈哈哈哈哈哈哈哈哈哈啊马友友吃货的日常记录,搞笑,哈哈哈哈哈哈哈哈哈哈啊",R.drawable.icon_people+"",
                    "马友友","https://gslb.miaopai.com/stream/YT6~sGYBDJeqPufxq~XsRJ4sHdb9l2VmBrYh4A__.mp4?yx=&amp;refer=weibo_app&amp;mpflag=32&amp;mpr=1505538260&amp;Expires=1506407967&amp;ssig=E5%2B0ATbisE&amp;KID=unistore,video"));
            itemList.add(new VideoItemBean("鹦鹉猫咪的搞笑日常,哈哈哈哈哈哈哈哈哈哈啊",R.drawable.boy+"",
                    "搞笑","https://gslb.miaopai.com/stream/NR-AFsHNLuTZSet-uDvZFSNOlZ-Gt5SZeUa36w__.mp4?yx=&refer=weibo_app&Expires=1504085504&ssig=/0GI7JJCQe&KID=unistore,video"));
            itemList.add(new VideoItemBean("小家伙的 洗澡,哈哈哈哈哈哈哈哈哈哈啊",R.drawable.girl+"",
                    "搞笑","https://gslb.miaopai.com/stream/zjb313oV4hoPU9p~4R-A8hmBv12giV8B.mp4?yx=&amp;refer=weibo_app&amp;Expires=1506408504&amp;ssig=DntWei4zl8&amp;KID=unistore,video"));
        }
        //将数据,还有点击的监听传递过去
        adapter = new MyVideoAdapter(mContext,itemList,myOnRecycleViewItemClickListener);
        //设置适配器
        video_recycler.setAdapter(adapter);
        //添加分割线,类似listview的deliver
        video_recycler.addItemDecoration(new DrawItemLine(mContext, LinearLayoutManager.HORIZONTAL));
        video_recycler.addOnScrollListener(scrollListener);
        return view;
    }

    @Override
    public void initData() {

    }

    //recycler的条目点击事件
    IRecyclerItem myOnRecycleViewItemClickListener = new IRecyclerItem() {
        @Override
        public void onItemClick(View view, int position) {
            Log.i(TAG, "onItemClick: 点击了哈哈哈--"+position);
            String videoUrl = itemList.get(position).getVideoUrl();
            Intent intent = new Intent(mContext, VideoPlayActivity.class);
            intent.putExtra("videoUrl",videoUrl);
            startActivity(intent);
        }

        @Override
        public void onItemLongClick(int position) {
//            list.remove(list.get(position));
//            adapter.notifyItemRemoved(position);
//            //这行是必须的,否则position 不会变
//            adapter.notifyItemRangeChanged(0,list.size()- position);
        }
    };

    //recyclerView的滑动监听,只有在停止滑动的时候才加载图片
    RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener(){
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            switch (newState){
                case SCROLL_STATE_DRAGGING://正在滚动,手指还在屏幕上
                case SCROLL_STATE_IDLE:
                    //滑动停止
                    try {
                        if(getContext() != null) {
                            Glide.with(getContext()).resumeRequests(); //恢复请求
                        }
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case SCROLL_STATE_SETTLING: //惯性滑动
                    try {
                        if(getContext() != null) {
                            Glide.with(getContext()).pauseRequests(); //暂停请求
                        }
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    };
}
