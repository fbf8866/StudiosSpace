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
import android.support.design.widget.CheckableImageButton;
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
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.magic.myapplication.R;
import com.example.magic.myapplication.activity.StoreActivity;
import com.example.magic.myapplication.activity.VideoPlayActivity;
import com.example.magic.myapplication.activity.interfaces.IRecyclerItem;
import com.example.magic.myapplication.activity.interfaces.IRecyclerItemLongClick;
import com.example.magic.myapplication.activity.interfaces.IStore;
import com.example.magic.myapplication.adapter.MyNewsAdapter;
import com.example.magic.myapplication.adapter.MyVideoAdapter;
import com.example.magic.myapplication.bean.VideoItemBean;
import com.example.magic.myapplication.common.DrawItemLine;
import com.example.magic.myapplication.common.RecyclerViewLinearlayoutManager;
import com.example.magic.myapplication.cookie.LogInterceptor;
import com.example.magic.myapplication.utils.DateUtils;
import com.example.magic.myapplication.utils.MyToastUtils;
import com.example.magic.myapplication.utils.SharedUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    String[] pic_icon_id = new String[]{ "http://img.my.csdn.net/uploads/201407/26/1406383299_1976.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383291_6518.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383291_8239.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383290_9329.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383290_1042.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383275_3977.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383265_8550.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383264_3954.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383264_4787.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383264_8243.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383248_3693.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383243_5120.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383242_3127.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383242_9576.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383242_1721.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383219_5806.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383214_7794.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383213_4418.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383213_3557.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383210_8779.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383172_4577.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383166_3407.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383166_2224.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383166_7301.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383165_7197.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383150_8410.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383131_3736.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383130_5094.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383130_7393.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383129_8813.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383100_3554.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383093_7894.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383092_2432.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383092_3071.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383091_3119.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383059_6589.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383059_8814.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383059_2237.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383058_4330.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383038_3602.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382942_3079.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382942_8125.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382942_4881.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382941_4559.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382941_3845.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382924_8955.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382923_2141.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382923_8437.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382922_6166.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382922_4843.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382905_5804.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382904_3362.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382904_2312.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382904_4960.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382900_2418.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382881_4490.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382881_5935.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382880_3865.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382880_4662.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382879_2553.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382862_5375.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382862_1748.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382861_7618.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382861_8606.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382861_8949.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382841_9821.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382840_6603.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382840_2405.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382840_6354.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382839_5779.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382810_7578.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382810_2436.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382809_3883.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382809_6269.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382808_4179.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382790_8326.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382789_7174.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382789_5170.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382789_4118.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382788_9532.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382767_3184.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382767_4772.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382766_4924.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382766_5762.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382765_7341.jpg"};

    private static final String TAG = "VideoFragment";
    private View view;
    private RecyclerView video_recycler;
    private RecyclerViewLinearlayoutManager manager;
    private MyVideoAdapter adapter;
    private List<VideoItemBean> itemList = new ArrayList<VideoItemBean>();
    private Gson gson;

    @Override
    public View initView() {
        gson = new Gson();
        if (null == view){
            view = LayoutInflater.from(mContext).inflate(R.layout.fragment_video,null);
        }

        video_recycler = (RecyclerView) view.findViewById(R.id.video_recycler);
        //如果确定每个item内容不会改变recyclerview 的大小, 那么设置这个可以提高性能
        video_recycler.setHasFixedSize(true);
        manager = new RecyclerViewLinearlayoutManager(mContext,1);
        manager.setOrientation(RecyclerView.VERTICAL);
        video_recycler.setLayoutManager(manager);

            itemList.add(new VideoItemBean("马友友吃货的日常记录,搞笑,哈哈哈哈哈哈哈哈哈哈啊马友友吃货的日常记录,搞笑,哈哈哈哈哈哈哈哈哈哈啊马友友吃货的日常记录,搞笑,哈哈哈哈哈哈哈哈哈哈啊",R.drawable.icon_people+"",
                    "马友友","https://gslb.miaopai.com/stream/YT6~sGYBDJeqPufxq~XsRJ4sHdb9l2VmBrYh4A__.mp4?yx=&amp;refer=weibo_app&amp;mpflag=32&amp;mpr=1505538260&amp;Expires=1506407967&amp;ssig=E5%2B0ATbisE&amp;KID=unistore,video"));
            itemList.add(new VideoItemBean("鹦鹉猫咪的搞笑日常,哈哈哈哈哈哈哈哈哈哈啊",R.drawable.boy+"",
                    "搞笑","https://gslb.miaopai.com/stream/NR-AFsHNLuTZSet-uDvZFSNOlZ-Gt5SZeUa36w__.mp4?yx=&refer=weibo_app&Expires=1504085504&ssig=/0GI7JJCQe&KID=unistore,video"));
            itemList.add(new VideoItemBean("小家伙的 洗澡,哈哈哈哈哈哈哈哈哈哈啊",R.drawable.girl+"",
                    "搞笑","https://gslb.miaopai.com/stream/zjb313oV4hoPU9p~4R-A8hmBv12giV8B.mp4?yx=&amp;refer=weibo_app&amp;Expires=1506408504&amp;ssig=DntWei4zl8&amp;KID=unistore,video"));

            for (int i = 0;i<20;i++){
                itemList.add(new VideoItemBean("1111111111111111111111111111111",R.drawable.boy+"",
                        "搞笑","https://gslb.miaopai.com/stream/zjb313oV4hoPU9p~4R-A8hmBv12giV8B.mp4?yx=&amp;refer=weibo_app&amp;Expires=1506408504&amp;ssig=DntWei4zl8&amp;KID=unistore,video1"+i));
            }


        //将数据,传递过去
        adapter = new MyVideoAdapter(mContext,itemList);
        //设置适配器
        video_recycler.setAdapter(adapter);
        //添加分割线,类似listview的deliver
        video_recycler.addItemDecoration(new DrawItemLine(mContext, LinearLayoutManager.HORIZONTAL));
        video_recycler.addOnScrollListener(scrollListener);

        //先获取是否本地有收藏
        String store_position = (String) SharedUtils.getInstance(mContext).get( "store_position", "");
        final List<VideoItemBean> l ;
        if (TextUtils.isEmpty(store_position)){ //没有收藏
            l = new ArrayList<>();
        }else{
            //将gson字符串转为集合
            l = gson.fromJson(store_position, new TypeToken<List<VideoItemBean>>() {}.getType());
        }

        adapter.setOnMyClickListener(new IRecyclerItem() {
            @Override
            public void onItemClick(View view, int position) {
//                String videoUrl = itemList.get(position).getVideoUrl();
//                Intent intent = new Intent(mContext, VideoPlayActivity.class);
//                intent.putExtra("videoUrl",videoUrl);
                Intent intent = new Intent(mContext, StoreActivity.class);
                startActivity(intent);
            }
        });

        adapter.setMyStore(new IStore() {
            @Override
            public void storeStateChanged(int position,boolean isStored) {
                if (isStored){
                    l.add(itemList.get(position));
                    MyToastUtils.show(mContext,"收藏成功");
//                    itemList.get(position).setIsStore(true);
                }else{
                    l.remove(itemList.get(position));
                    MyToastUtils.show(mContext,"取消收藏");
//                    itemList.get(position).setIsStore(false);
                }
                String s = gson.toJson(l);
                SharedUtils.getInstance(mContext).put("store_position",s);
            }
        });
        return view;
    }


    @Override
    public void initData() {

    }


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
