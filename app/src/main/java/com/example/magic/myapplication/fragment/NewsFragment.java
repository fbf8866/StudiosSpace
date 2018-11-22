package com.example.magic.myapplication.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.example.magic.myapplication.R;
import com.example.magic.myapplication.activity.HQActivity;
import com.example.magic.myapplication.activity.HQActivityNew;
import com.example.magic.myapplication.activity.StoreActivity;
import com.example.magic.myapplication.adapter.MyNewsAdapter;
import com.example.magic.myapplication.common.DrawItemLine;
import com.example.magic.myapplication.utils.GetScreenWidthAndHeight;
import com.example.magic.myapplication.utils.MyToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/13.
 */

public class NewsFragment extends BaseFragment {
    private String TAG = NewsFragment.class.getSimpleName();
    private View view;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwiperefreshlayout;
    private MyNewsAdapter adapter;
    public interface  onRecycleViewItemClickListener {
        //条目点击事件
        void onItemClick(View view, int position);
        //条目长按事件
        void onItemLongClick(int position);
    }
    //模拟数据
    private List<String> list = new ArrayList<String>();

    private LinearLayoutManager manager;
    //  private StaggeredGridLayoutManager manager;
//	private GridLayoutManager manager;
    @Override
    public View initView() {
        if (null == view) {
            view =  LayoutInflater.from(mContext).inflate(R.layout.fragment_news, null);
        }
        initChildViews();
        return view;
    }

    private void initChildViews() {
		mSwiperefreshlayout = (SwipeRefreshLayout) view.findViewById(R.id.mSwiperefreshlayout);
        //设置刷新时动画的颜色，可以设置4个
        mSwiperefreshlayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        mSwiperefreshlayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light,android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        //第一个参数是是否缩放,true缩放
        //第二个和第三个参数 是刷新进度条展示的相对于默认的展示位置,start和end组成一个范围
        mSwiperefreshlayout.setProgressViewOffset(true, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
        mSwiperefreshlayout.setOnRefreshListener(myOnRefreshListener);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.mRecyclerView);
		 //设置固定大小
		mRecyclerView.setHasFixedSize(true);
//		线性布局
		manager = new LinearLayoutManager(mContext);
		//瀑布流式布局
//		manager = new StaggeredGridLayoutManager(3,OrientationHelper.VERTICAL);
		//表格布局
//		manager = new GridLayoutManager(mContext,3);
		//垂直方向
//		manager.setOrientation(OrientationHelper.VERTICAL);
		//给RecyclerView设置布局管理器
		mRecyclerView.setLayoutManager(manager);
        //添加分割线,类似listview的deliver
        mRecyclerView.addItemDecoration(new DrawItemLine(mContext,LinearLayoutManager.HORIZONTAL));

        mRecyclerView.addOnScrollListener(myOnScrollListener);
    }

    @Override
    public void initData() {
        //设置默认进来就先刷新数据
        mSwiperefreshlayout.setRefreshing(true);
		 new Handler().postDelayed(new Runnable() {
             @Override
             public void run() {
                 for (int i = 1; i < 10; i++) {
                     list.add("这是第---"+i+"---个条目");
                 }
                 //将数据,还有点击的监听传递过去
                 adapter = new MyNewsAdapter(mContext,list,myOnRecycleViewItemClickListener);
                 //设置适配器
                 mRecyclerView.setAdapter(adapter);
                 mSwiperefreshlayout.setRefreshing(false);
             }
         }, 2000);
    }

    /**
     * recycleView条目点击和长按删除
     */
    onRecycleViewItemClickListener myOnRecycleViewItemClickListener = new onRecycleViewItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            MyToastUtils.show(mContext,"点击了"+list.get(position));
            Intent i = new Intent(mContext, HQActivityNew.class);
            startActivity(i);
        }

        @Override
        public void onItemLongClick(int position) {
            list.remove(list.get(position));
            adapter.notifyItemRemoved(position);
            //这行是必须的,否则position 不会变
            adapter.notifyItemRangeChanged(0,list.size()- position);
        }
    };

    /**
     * SwipeRefreshLayout的刷新监听
     */
    SwipeRefreshLayout.OnRefreshListener myOnRefreshListener =  new SwipeRefreshLayout.OnRefreshListener(){
        @Override
        public void onRefresh() {
            //这里模拟网络请求数据
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i <5; i++) {
                        adapter.addItem("刷新的数据"+i,0);
                    }
                    mSwiperefreshlayout.setRefreshing(false);
                }
            }, 3000);
        }
    };

    /**
     * RecycleView滑动监听
     */
    RecyclerView.OnScrollListener myOnScrollListener = new RecyclerView.OnScrollListener(){
        @Override
        public void onScrollStateChanged(final RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (null != adapter
                    &&newState == RecyclerView.SCROLL_STATE_IDLE
                    &&lastVisibleItemPosition + 1 == adapter.getItemCount()
                    &&dys>0){
                adapter.addMoreItem("",lastVisibleItemPosition,adapter.UP_PULL);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (list.size() > 30){
                            adapter.addMoreItem("",lastVisibleItemPosition,adapter.LOAD_OVER);
                        }else{
                            for (int i = 0;i < 3; i++){
                                adapter.addMoreItem("上拉加载的数据"+ i,lastVisibleItemPosition,adapter.UP_PULL);
                            }
                        }
                    }
                },2000);
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            //dx和dy表示x轴和y轴滑动值,dy>0向上
            super.onScrolled(recyclerView, dx, dy);
            //最后一条可见的条目
            lastVisibleItemPosition = manager.findLastVisibleItemPosition();
            dys = dy;

        }
    };
    int lastVisibleItemPosition;
    int dys ;


}
