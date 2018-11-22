package com.example.magic.myapplication.activity.interfaces;

/**
 * Created by Administrator on 2017/12/22.
 * 长按条目出现checkbox的勾选的对外接口
 */

public interface IRecyclerItemChecked {
    /**
     * @param position 条目的位置
     * @param isChecked 是否勾选
     */
    void itemChecked(int position,boolean isChecked);
}
