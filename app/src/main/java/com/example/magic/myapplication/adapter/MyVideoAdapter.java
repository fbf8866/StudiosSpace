package com.example.magic.myapplication.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.magic.myapplication.R;
import com.example.magic.myapplication.activity.interfaces.IRecyclerItem;
import com.example.magic.myapplication.bean.VideoItemBean;
import com.example.magic.myapplication.utils.MyToastUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/12/11.
 */

public class MyVideoAdapter extends RecyclerView.Adapter {
    private static final String TAG = "MyVideoAdapter";
    private View view;
    private Context context;
    private List<VideoItemBean> list;
    private IRecyclerItem myOnRecycleViewItemClickListener;
    private MyVideoAdapter.Holder h;
    public MyVideoAdapter(Context context, List<VideoItemBean> list, IRecyclerItem myOnRecycleViewItemClickListener){
        this.context = context;
        this.list = list;
        this.myOnRecycleViewItemClickListener = myOnRecycleViewItemClickListener;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.item_video_fragment, parent,false);
        //设置条目的背景颜色
        view.setBackgroundColor(Color.RED);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyVideoAdapter.Holder){
            h = (MyVideoAdapter.Holder) holder;
            h.tv_introduction.setText(list.get(position).getIntroduction());
            h.tv_attention.setText(list.get(position).getAttention());
            Glide.with(context).load(Integer.parseInt(list.get(position).getIconId()))
                    .placeholder(R.mipmap.ic_launcher)//未能及时显示的时候 显示的默认图片
                    .error(R.mipmap.btn_goin) //加载失败的时候显示的图片
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(h.item_video_iv);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class Holder extends RecyclerView.ViewHolder{
        private final ImageView item_video_iv;
        public TextView tv_introduction;
        public TextView tv_attention;
        public Holder(View itemView) {
            super(itemView);
            tv_introduction = (TextView) itemView.findViewById(R.id.item_video_introduction);
            tv_attention = (TextView) itemView.findViewById(R.id.item_video_attention);
            item_video_iv = (ImageView) itemView.findViewById(R.id.item_video_iv);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null != myOnRecycleViewItemClickListener){
                        myOnRecycleViewItemClickListener.onItemClick(view,getLayoutPosition());
                    }
                }
            });
        }
    }
}
