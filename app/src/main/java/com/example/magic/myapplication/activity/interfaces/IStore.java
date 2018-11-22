package com.example.magic.myapplication.activity.interfaces;

/**
 * Created by Administrator on 2017/12/20.
 * recyclerView条目中点击右侧星星的收藏功能
 */

public interface IStore {
    /**
     * @param positiion 点击条目的位置
     * @param isStore 是否收藏
     */
    void storeStateChanged(int positiion,boolean isStore);
}
