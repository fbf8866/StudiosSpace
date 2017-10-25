package com.example.magic.myapplication.common;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.magic.myapplication.utils.GetMidScreenPoint;
import com.example.magic.myapplication.utils.GetScreenWidthAndHeight;
import com.example.magic.myapplication.utils.MyToastUtils;

/**
 * Created by Administrator on 2017/3/20.
 * 自定义控件的学习view
 */

public class CustomView extends View  implements Runnable{
    private Paint myPaint;
    private Context mContext;
    private float radius = 50;
    private Handler mHandler;
    private Path mPath;

    public CustomView(Context context) {
        super(context);
        initPaint(context);
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint(context);
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint(context);
    }

    private void initPaint(Context context){
        mContext = context;
        this.myPaint = new Paint();
        //设置抗锯齿
        myPaint.setAntiAlias(true);
        /*
         * 画笔样式分三种：
         * 1.Paint.Style.STROKE：描边
         * 2.Paint.Style.FILL_AND_STROKE：描边并填充
         * 3.Paint.Style.FILL：填充
         */
        myPaint.setStyle(Paint.Style.STROKE);
        //设置描边的宽度,单位像素px
        myPaint.setStrokeWidth(10);
        myPaint.setColor(Color.RED);


        /**
         * 绘制路径
         */
        this.mPath = new Path();
        /**
         * lineTo的起始点是0,0
         * moveTo是将起始点移动到 x y
         */
        mPath.moveTo(100,100);
        mPath.lineTo(400,100);
        mPath.lineTo(400,300);
        mPath.lineTo(100,300);
        //闭合
        mPath.close();

        /**
         *  quadTo 二阶贝赛尔
         *  前两个参数 是控制点的 坐标
         *  后两个参数 是终点的坐标
         */
        mPath.moveTo(100,400);
        mPath.quadTo(200,200,500,400);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawPath(mPath,myPaint);


        canvas.save();
        //在屏幕的中心点上画半径50px的圆
        canvas.drawCircle(GetMidScreenPoint.getMid(mContext)[0],
                GetMidScreenPoint.getMid(mContext)[1],radius,myPaint);
        canvas.drawLine(GetMidScreenPoint.getMid(mContext)[0],
                GetMidScreenPoint.getMid(mContext)[1]+50,GetMidScreenPoint.getMid(mContext)[0],
                GetMidScreenPoint.getMid(mContext)[1]+100,myPaint);
        canvas.restore();
        drawCircleByDegress(canvas,60);
        drawCircleByDegress(canvas,120);
        drawCircleByDegress(canvas,180);
        drawCircleByDegress(canvas,240);
        drawCircleByDegress(canvas,300);
        drawCircleByDegress(canvas,360);
    }

    /**
     * @param canvas
     * @param degrees  旋转的角度
     */
    private void drawCircleByDegress(Canvas canvas,float degrees) {
        //锁定画布
        canvas.save();
        canvas.rotate(degrees, GetMidScreenPoint.getMid(mContext)[0],
                GetMidScreenPoint.getMid(mContext)[1]);
        canvas.drawCircle(GetMidScreenPoint.getMid(mContext)[0],
                GetMidScreenPoint.getMid(mContext)[1]+150,radius,myPaint);

        //画线, 起始点xy坐标, 终点xy坐标, 画笔
        canvas.drawLine(GetMidScreenPoint.getMid(mContext)[0],
                GetMidScreenPoint.getMid(mContext)[1]+50,GetMidScreenPoint.getMid(mContext)[0],
                GetMidScreenPoint.getMid(mContext)[1]+100,myPaint);
        //释放画布
        canvas.restore();
    }

    @Override
    public void run() {
        /*
         * 确保线程不断执行不断刷新界面
         */
        while (true) {
            try {
                /*
                 * 如果半径小于200则自加否则大于200后重置半径值以实现往复
                 */
                if (radius <= 200) {
                    radius += 10;

                    // 刷新View
                    postInvalidate();
                } else {
                    radius = 50;
                }

                // 每执行一次暂停40毫秒
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
