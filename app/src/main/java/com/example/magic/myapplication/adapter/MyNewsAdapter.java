package com.example.magic.myapplication.adapter;


import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.magic.myapplication.R;
import com.example.magic.myapplication.fragment.NewsFragment;

import java.util.List;

public class MyNewsAdapter extends RecyclerView.Adapter{
	private Context mContext;
	private List<String> list;
	private NewsFragment.onRecycleViewItemClickListener myOnRecycleViewItemClickListener;
	/**
	 * 普通Item View
	 */
	private static final int TYPE_ITEM = 0;
	/**
	 * 底部FootView
	 */
	private static final int TYPE_FOOTER = 1;
	/**
	 * 数据加载中...
	 */
	public static final int UP_PULL = 1;
	/**
	 * 我是有底线的(数据加载完毕)
	 */
	public static final int LOAD_OVER = 2;
	private int LOAD_MORE_STATE  = 0;
	public View footerViews;

	public MyNewsAdapter(Context context, List<String> list, NewsFragment.onRecycleViewItemClickListener myOnRecycleViewItemClickListener){
		this.mContext = context;
		this.list= list;
		this.myOnRecycleViewItemClickListener = myOnRecycleViewItemClickListener;
	}

	@Override
	public int getItemCount() {
		//这里+1 是为了再底部添加一个脚布局
		return list.size()+1;
	}

	//重写这个方法, 是为了判断加载布局的类型
	@Override
	public int getItemViewType(int position) {
		if (position + 1 == getItemCount()) {
			return TYPE_FOOTER;
		} else {
			return TYPE_ITEM;
		}
	}


	/**
	 * 绑定数据
	 */
	@Override
	public void onBindViewHolder( RecyclerView.ViewHolder holder, final int position) {
		if (holder instanceof Holder){
			final Holder h = (Holder) holder;
			h.item_news_tv.setText(list.get(position));
		}else if (holder instanceof FooterViewHolder){
			final FooterViewHolder h = (FooterViewHolder) holder;
			switch (LOAD_MORE_STATE){
				case UP_PULL:
					footerViews.setVisibility(View.VISIBLE);
					h.pb.setVisibility(View.VISIBLE);
					h.footerView.setText("正在加载...");
					break;
				case LOAD_OVER:
					h.pb.setVisibility(View.INVISIBLE);
					h.footerView.setText("我是有底线的");
//					int height = footerViews.getHeight();
//					TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
//							0.0f, Animation.RELATIVE_TO_SELF, height);
//					mHiddenAction.setDuration(800);
//					footerViews.startAnimation(mHiddenAction);
//					footerViews.setVisibility(View.GONE);
					break;
			}
		}
	}

	/**
	 *  添加数据
	 */
	public void addItem(String data, int position) {
		list.add(position, data);
		//在position的位置插入数据
		notifyItemInserted(position);
		notifyDataSetChanged();
	}

	/**
	 * 上拉加载更多
	 */
	public void addMoreItem(String data, int position,int states){
		LOAD_MORE_STATE = states;
		if (!TextUtils.isEmpty(data)){
			list.add(position, data);
		}
		notifyDataSetChanged();
	}

	/**
	 * 创建holder
	 *  这边可以做一些属性设置，甚至事件监听绑定
	 */
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewTypese) {
		if (viewTypese == TYPE_ITEM){
			View view = LayoutInflater.from(mContext).inflate(R.layout.item_news_fragment, parent,false);
			//设置条目的背景颜色
			view.setBackgroundColor(Color.RED);
			Holder holder = new Holder(view);
			return holder;
		}else if (viewTypese == TYPE_FOOTER){
			if (null == footerViews){
				footerViews = LayoutInflater.from(mContext).inflate(R.layout.item_news_fragment_footer, parent,false);
			}
			//设置条目的背景颜色
			footerViews.setBackgroundColor(Color.GRAY);
			FooterViewHolder footViewHolder = new FooterViewHolder(footerViews);
			return footViewHolder;
		}
		return null;
	}

	class Holder extends RecyclerView.ViewHolder{
		public TextView item_news_tv;
		public TextView item_news_add;
		public Holder(View itemView) {
			super(itemView);
			item_news_tv = (TextView) itemView.findViewById(R.id.item_news_tv);
			item_news_add = (TextView) itemView.findViewById(R.id.item_news_add);

			//条目长按删除
			item_news_tv.setOnLongClickListener(new View.OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					if (null != myOnRecycleViewItemClickListener){
						/*
						这里只能用getLayoutPosition来获取position,不能直接用onBindHolder中position,
						因为那里的position是对应的条目显示的添加删除数据时 的position,所以会报数组角标越界
						getLayoutPosition 这个是从0开始的,0 1 2 3...
						 */
						myOnRecycleViewItemClickListener.onItemLongClick(getLayoutPosition());
					}
					return true;
				}
			});

			//条目点击事件
			item_news_tv.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v) {
					if (null != myOnRecycleViewItemClickListener){
						myOnRecycleViewItemClickListener.onItemClick(item_news_tv,getLayoutPosition());
					}
				}
			});

			item_news_add.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					addItem("添加的数据"+getLayoutPosition(),getLayoutPosition());
				}
			});
		}
	}

	/**
	 * 底部脚布局的holder
	 */
	class FooterViewHolder extends RecyclerView.ViewHolder{
		public TextView footerView;
		public ProgressBar pb;
		public FooterViewHolder(View itemView) {
			super(itemView);
			footerView = (TextView) itemView.findViewById(R.id.item_news_footer);
			pb = (ProgressBar) itemView.findViewById(R.id.item_news_pb);
		}
	}
}
