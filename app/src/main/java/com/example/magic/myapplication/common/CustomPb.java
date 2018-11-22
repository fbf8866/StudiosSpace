package com.example.magic.myapplication.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.example.magic.myapplication.R;

/**
 * Created by Administrator on 2017/3/21.
 */

public class CustomPb extends View{
    private String TAG = CustomPb.class.getSimpleName();
    private int textColor;
    private String text;
    private float textsize;
    private Paint mPaint;
    public  int PROGRESS = 0;
    private String texts;
    /**
     * 最大进度
     */
    private int MAX = 100;
    /**
     * 画圆角矩形进度条的 圆角半径
     */
    private final float RADIUS = 10;

    /**
     *  定义个接口
     */
    public interface  onCustomPbClickListener{
        void onClick();
    }
    /**
     * 定义个变量
     */
    public onCustomPbClickListener onClickListener;
    /**
     * 定义个供外的方法
     */
    public void setOnCustomClickListener(onCustomPbClickListener onClickListener){
        this.onClickListener = onClickListener;
    }

    public CustomPb(Context context) {
        super(context);
        initPaint();
    }

    public CustomPb(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.customprogressbar);
        textColor = array.getColor(R.styleable.customprogressbar_pbtextcolor, Color.RED);
        text = array.getString(R.styleable.customprogressbar_pbtext);
        textsize = array.getDimension(R.styleable.customprogressbar_pbtextsize,22);
        array.recycle();
        initPaint();

    }

    public CustomPb(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /**
         * 画圆角矩形进度条
         */
        drawRectProgress(canvas);

    }

    private void drawRectProgress(Canvas canvas){
        mPaint.setColor(Color.BLUE);
        /**
         * 画底层的实心圆角方块
         */
        canvas.drawRoundRect(new RectF(0,0,getWidth(),getHeight()),RADIUS,RADIUS,mPaint);
        /**
         * 画进度的圆角方块
         */
        mPaint.setColor(Color.RED);
        canvas.drawRoundRect(new RectF(0,0,PROGRESS * getWidth()/MAX,getHeight()),RADIUS,RADIUS,mPaint);
        /**
         * 画进度文字
         */
        mPaint.setColor(Color.BLACK);
        if (PROGRESS == 0){
            texts = "下载";
        }else if (PROGRESS == MAX){
            texts = "完成";
        }else{
            texts = PROGRESS+"%";
        }
        mPaint.setTextSize(44);
        Rect rect = new Rect();
        mPaint.getTextBounds(texts,0,texts.length(),rect);
        int textwidth = rect.width();
        canvas.drawText(texts, getWidth()/2 - textwidth/2 , getHeight()/2-(mPaint.descent()+mPaint.ascent())/2, mPaint);

        /**
         * 点击的回调
         */
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onClickListener){
                    onClickListener.onClick();
                }
            }
        });
    }


    /**
     * 设置开始下载
     * @param progress 开始进度
     */
    public void setProgress(int progress){
        PROGRESS = progress;
        postInvalidate();
    }
}
