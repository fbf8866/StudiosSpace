package com.example.magic.myapplication.common;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Administrator on 2017/11/30.
 * 这个类是为了解决RecyclerView中多次快速滑动的时候报的下面这个错误
 * java.lang.IndexOutOfBoundsException: Inconsistency detected.
 * Invalid view holder adapter positionViewHolder{8f3995e position=28 id=-1, oldPos=-1, pLpos:-1 no parent}
 * 原因就是 在进行数据的增加或者删除的时候, adapter中操作的数据和实际的集合中的数据不一致导致的
 */

public class RecyclerViewLinearlayoutManager extends GridLayoutManager  {
    private static final String TAG = "RecyclerViewLinearlayou";

    public RecyclerViewLinearlayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public RecyclerViewLinearlayoutManager(Context context, int orientation) {
        super(context, orientation);
    }

    public RecyclerViewLinearlayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout){
        super(context, spanCount, orientation, reverseLayout);
    }

    @Override
    public int getChildCount() {
        return super.getChildCount();
    }

    @Override
    public View getChildAt(int index) {
        return super.getChildAt(index);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    public View getFocusedChild() {
        return super.getFocusedChild();
    }

    @Override
    public int getPosition(View view) {
        return super.getPosition(view);
    }

    /**
     * 获取列数
     * @return
     */
    @Override
    public int getSpanCount() {
        return super.getSpanCount();
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            //try catch一下
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    /**
     *  * 防止当recyclerview上下滚动的时候焦点乱跳
     * @param focused
     * @param focusDirection
     * @param recycler
     * @param state
     * @return
     */
    @Override
    public View onFocusSearchFailed(View focused, int focusDirection, RecyclerView.Recycler recycler, RecyclerView.State state) {

        // Need to be called in order to layout new row/column
        View nextFocus = super.onFocusSearchFailed(focused, focusDirection, recycler, state);
        if (nextFocus == null) {
            return null;
        }
        /**
         * 获取当前焦点的位置
         */
        int fromPos = getPosition(focused);
        /**
         * 获取我们希望的下一个焦点的位置
         */
        int nextPos = getNextViewPos(fromPos, focusDirection);

        return findViewByPosition(nextPos);

    }

    protected int getNextViewPos(int fromPos, int direction) {
        int offset = calcOffsetToNextView(direction);

        if (hitBorder(fromPos, offset)) {
            return fromPos;
        }

        return fromPos + offset;
    }

    protected int calcOffsetToNextView(int direction) {
        int spanCount = getSpanCount();
        int orientation = getOrientation();

        if (orientation == VERTICAL) {
            switch (direction) {
                case View.FOCUS_DOWN:
                    return spanCount;
                case View.FOCUS_UP:
                    return -spanCount;
                case View.FOCUS_RIGHT:
                    return 1;
                case View.FOCUS_LEFT:
                    return -1;
            }
        } else if (orientation == HORIZONTAL) {
            switch (direction) {
                case View.FOCUS_DOWN:
                    return 1;
                case View.FOCUS_UP:
                    return -1;
                case View.FOCUS_RIGHT:
                    return spanCount;
                case View.FOCUS_LEFT:
                    return -spanCount;
            }
        }

        return 0;
    }

    private boolean hitBorder(int from, int offset) {
        int spanCount = getSpanCount();

        if (Math.abs(offset) == 1) {
            int spanIndex = from % spanCount;
            int newSpanIndex = spanIndex + offset;
            return newSpanIndex < 0 || newSpanIndex >= spanCount;
        } else {
            int newPos = from + offset;
            return newPos < 0 && newPos >= spanCount;
        }
    }

}
