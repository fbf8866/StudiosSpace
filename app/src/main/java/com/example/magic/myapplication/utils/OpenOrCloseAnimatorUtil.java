package com.example.magic.myapplication.utils;

import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2017/8/11.
 * 打开或者关闭的动画,比直接visiable和gone体验好点
 */

public class OpenOrCloseAnimatorUtil {
    public static ValueAnimator animator;
    public static void toggleContentAnimator(boolean isOpen, final View view,
                                       int height) {
        int choiceHeight = getMeasureHeight(view);

        if (isOpen) {
            animator = ValueAnimator.ofInt(choiceHeight, height);

        } else {
            animator = ValueAnimator.ofInt(height, choiceHeight);

        }
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                ViewGroup.LayoutParams params = view.getLayoutParams();
                params.height = value;
                view.setLayoutParams(params);
            }
        });
        animator.setDuration(choiceHeight / 2);
        animator.start();
    }

    /**
     * 获取布局测量后的高度
     *
     * @param view
     * @return
     */
    private static int getMeasureHeight(View view) {
        view.measure(0, 0);
        return view.getMeasuredHeight();

    }

    /**
     * 设置布局参数的高度为零，从而达到隐藏的效果
     *
     * @param view
     */
    public static void setLayoutParamsZero(View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = 0;
        view.setLayoutParams(params);
    }
}
