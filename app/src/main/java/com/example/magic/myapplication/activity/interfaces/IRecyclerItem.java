package com.example.magic.myapplication.activity.interfaces;

import android.view.View;

/**
 * Created by Administrator on 2017/12/18.
 * recyclerView中的条目的点击和长按点击事件
 */

public interface IRecyclerItem {
    //条目点击事件
    void onItemClick(View view, int position);
    //条目长按事件
    void onItemLongClick(int position);
}
