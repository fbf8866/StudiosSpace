package com.example.magic.myapplication.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.magic.myapplication.R;
import com.example.magic.myapplication.activity.BaseActivity;
import com.example.magic.myapplication.activity.interfaces.IRecyclerItem;
import com.example.magic.myapplication.activity.interfaces.IRecyclerItemLongClick;
import com.example.magic.myapplication.activity.interfaces.IStore;
import com.example.magic.myapplication.bean.VideoItemBean;
import com.example.magic.myapplication.fragment.VideoFragment;
import com.example.magic.myapplication.utils.MyToastUtils;
import com.example.magic.myapplication.utils.SharedUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/11.
 */

public class MyVideoAdapter extends RecyclerView.Adapter{
    private static final String TAG = "MyVideoAdapter";
    private View view;
    private Context context;
    private List<VideoItemBean> list;
    private MyVideoAdapter.Holder h;
    private IRecyclerItem myClickListener;
    private IStore myStore;
    //用于存放收藏状态的集合, 防止复用引发的错乱
    private List<String>  storeList ;
    private  boolean  isStored = false;
    private final Gson gson = new Gson();

    public MyVideoAdapter(Context c, List<VideoItemBean> list){
        this.context = c;
        this.list = list;
    }

    //对外的点击事件
    public void setOnMyClickListener(IRecyclerItem myClickListener){
        this.myClickListener = myClickListener;
    }

    //对外的收藏的事件
    public void setMyStore(IStore myStore){
        this.myStore = myStore;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.item_video_fragment, parent,false);
        //设置条目的背景颜色
        view.setBackgroundColor(Color.WHITE);
        Holder holder = new Holder(view);
//        String stored = (String) SharedUtils.get(context, "stored", "");
//        if(TextUtils.isEmpty(stored)){
//            storeList = new ArrayList<>();
//        }else{
//            String substring = stored.substring(1,stored.length()-1);
//            storeList = new ArrayList<>();
////            storeList = gson.fromJson(substring, new TypeToken<List<String>>() {}.getType());
//            Log.i(TAG, stored+"-----onCreateViewHolder: ---"+substring);
//        }
        storeList = SharedUtils.getInstance(context).getList("stored");
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyVideoAdapter.Holder){
            h = (MyVideoAdapter.Holder) holder;
            h.tv_introduction.setText(list.get(position).getIntroduction());
            h.tv_attention.setText(list.get(position).getAttention());
            //解决收藏按钮的复用问题
            h.iv_store.setTag(list.get(position).getVideoUrl());
            if (storeList != null && storeList.size() > 0) {
                if (storeList.contains(list.get(position).getVideoUrl())){
                    h.iv_store.setBackgroundResource(R.drawable.store);
                }else{
                    h.iv_store.setBackgroundResource(R.drawable.store_nomal);
                }
            }

            //先获取是否本地有收藏
            String store_position = (String) SharedUtils.getInstance(context).get("store_position", "");
            if (!TextUtils.isEmpty(store_position)){
                //将gson字符串转为集合
                List<VideoItemBean> lll = gson.fromJson(store_position, new TypeToken<List<VideoItemBean>>() {}.getType());
                for (int i = 0; i < lll.size(); i++){
                    String videoUrl = lll.get(i).getVideoUrl();
                    if (h.iv_store.getTag().equals(videoUrl)){
                        h.iv_store.setBackgroundResource(R.drawable.store);
                    }
                }
            }



            Glide.with(context).load(Integer.parseInt(list.get(position).getIconId()))
                    .placeholder(R.mipmap.ic_launcher)//未能及时显示的时候 显示的默认图片
                    .error(R.mipmap.btn_goin) //加载失败的时候显示的图片
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(h.item_video_iv);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int p = holder.getAdapterPosition();
                    myClickListener.onItemClick(view,p);
                }
            });

            //收藏
            h.iv_store.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int adapterPosition = holder.getAdapterPosition();


                    if (storeList == null ) {
                        storeList = new ArrayList<String>();
                    }

//                    String store_position = (String) SharedUtils.getInstance(context).get("store_position", "");
//                    if (!TextUtils.isEmpty(store_position)){
//                        //将gson字符串转为集合
//                        List<VideoItemBean> lll = gson.fromJson(store_position, new TypeToken<List<VideoItemBean>>() {}.getType());
//                        for (int i = 0; i < lll.size(); i++){
//                            String videoUrl = lll.get(i).getVideoUrl();
//                            if (h.iv_store.getTag().equals(videoUrl)){
//                                h.iv_store.setBackgroundResource(R.drawable.store);
//                            }
//                        }
//                    }

                    if (!storeList.contains(gson.toJson(list.get(position)))){
                        String s = gson.toJson(list.get(position));
                        storeList.add(s);
                        Log.i(TAG, "onClick: onCreateViewHolder: ---"+list.get(position).getVideoUrl());
                        isStored = true;
                        if (h.iv_store.getTag().equals(list.get(position).getVideoUrl())){
                            h.iv_store.setBackgroundResource(R.drawable.store);
                            Log.i(TAG, "onClick: 改变的号");
                        }else{
                            Log.i(TAG, "onClick: 改变不了啊");
                        }
                    }else{
                        String s = gson.toJson(list.get(position));
                        storeList.remove(s);
                        isStored = false;
                        if (!h.iv_store.getTag().equals(list.get(position).getVideoUrl())){
                            h.iv_store.setBackgroundResource(R.drawable.store_nomal);
                            Log.i(TAG, "onClick: 改变不");
                        }else{
                            Log.i(TAG, "onClick: 不改变");
                        }
                    }
                    myStore.storeStateChanged(adapterPosition,isStored);
                    SharedUtils.getInstance(context).putList("stored",storeList);
                    notifyDataSetChanged();
                }
            });

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
        public ImageView iv_store;
        public Holder(View itemView) {
            super(itemView);
            tv_introduction = (TextView) itemView.findViewById(R.id.item_video_introduction);
            tv_attention = (TextView) itemView.findViewById(R.id.item_video_attention);
            item_video_iv = (ImageView) itemView.findViewById(R.id.item_video_iv);
            iv_store = (ImageView) itemView.findViewById(R.id.store);
        }
    }
}
