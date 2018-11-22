package com.example.magic.myapplication.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.magic.myapplication.R;
import com.example.magic.myapplication.activity.StoreActivity;
import com.example.magic.myapplication.activity.interfaces.IRecyclerItem;
import com.example.magic.myapplication.activity.interfaces.IRecyclerItemChecked;
import com.example.magic.myapplication.activity.interfaces.IRecyclerItemLongClick;
import com.example.magic.myapplication.bean.VideoItemBean;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2017/12/22.
 * 我的收藏的adapter
 */

public class MyStoreAdapter extends RecyclerView.Adapter{
    private static final String TAG = "MyStoreAdapter";
    private IRecyclerItemLongClick myLongClickListener;
    private IRecyclerItem myClickListener;
    private IRecyclerItemChecked myCheckedListener;
    private View view;
    private Context contxt;
    private List<VideoItemBean> list;
    private MyStoreAdapter.Holder h;
    //用于存放cheeckbox的集合, 防止复用引发的错乱
    private List<String> cbList = new ArrayList<>();
    //是否勾选
    private  boolean  isChecked = false;
    //用于判断checkbox是否显示的标示
    private boolean flags = false;

    public MyStoreAdapter(Context contxt,List<VideoItemBean> list){
        this.contxt = contxt;
        this.list = list;
    }

    public void setMyItemLongClickListener(IRecyclerItemLongClick myLongClickListener){
        this.myLongClickListener = myLongClickListener;
    }

    public void setMyItemClickListener(IRecyclerItem myClickListener){
        this.myClickListener = myClickListener;
    }

    public void setMyItemCheckedListener(IRecyclerItemChecked myCheckedListener){
        this.myCheckedListener = myCheckedListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(contxt).inflate(R.layout.item_mystore, parent,false);
        //设置条目的背景颜色
        view.setBackgroundColor(Color.WHITE);
        MyStoreAdapter.Holder holder = new MyStoreAdapter.Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyStoreAdapter.Holder){
            h = (MyStoreAdapter.Holder) holder;
            h.tv_introduction.setText(list.get(position).getIntroduction());
            h.tv_attention.setText(list.get(position).getAttention());
            //解决收藏按钮的复用问题
            h.item_mystore_cb.setTag(String.valueOf(position));
            if (cbList != null) {
                if (cbList.contains(String.valueOf(position))){
                    h.item_mystore_cb.setChecked(true);
                }else{
                    h.item_mystore_cb.setChecked(false);
                }
            }

            if (flags){
                h.item_mystore_cb.setVisibility(View.VISIBLE);
            }else{
                h.item_mystore_cb.setVisibility(View.GONE);
            }
            Glide.with(contxt).load(Integer.parseInt(list.get(position).getIconId()))
                    .placeholder(R.mipmap.ic_launcher)//未能及时显示的时候 显示的默认图片
                    .error(R.mipmap.btn_goin) //加载失败的时候显示的图片
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(h.item_mystore_iv);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int p = holder.getAdapterPosition();
                    myClickListener.onItemClick(view,p);
                }
            });
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int p = holder.getAdapterPosition();
                    myLongClickListener.onItemLongClick(p);
                    flags = !flags;
                    //这里必须加上这个,否则没反应, 坑了我好久....
                    notifyDataSetChanged();
                    return true;
                }
            });

            //checkbox勾选
            h.item_mystore_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    int adapterPosition = holder.getAdapterPosition();

                    if (!cbList.contains(String.valueOf(position))){
                        cbList.add(String.valueOf(position));
                        isChecked = true;
                    }else{
                        cbList.remove(String.valueOf(position));
                        isChecked = false;
                    }
                    myCheckedListener.itemChecked(adapterPosition,isChecked);
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Holder extends RecyclerView.ViewHolder{
        private ImageView item_mystore_iv;
        public TextView tv_introduction;
        public TextView tv_attention;
        public CheckBox item_mystore_cb;
        public Holder(View itemView) {
            super(itemView);
            item_mystore_iv = (ImageView) itemView.findViewById(R.id.item_mystore_iv);
            tv_introduction = (TextView) itemView.findViewById(R.id.item_mystore_introduction);
            tv_attention = (TextView) itemView.findViewById(R.id.item_mystore_attention);
            item_mystore_cb = (CheckBox) itemView.findViewById(R.id.item_mystore_cb);
        }
    }
}
