package com.example.magic.myapplication.fragment;

import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.magic.myapplication.R;
import com.example.magic.myapplication.cookie.LogInterceptor;
import com.example.magic.myapplication.utils.DateUtils;
import com.example.magic.myapplication.utils.MyToastUtils;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/3/15.
 */

public class VideoFragment extends BaseFragment implements View.OnClickListener, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener{
    private static final String TAG = "VideoFragment";
    private View view;
    private AppBarLayout layout_appbar;
    private CollapsingToolbarLayout layout_collapsingtoolbar;
    private FrameLayout fl_img_boy;
    private Toolbar layout_toolbar;

    private  CollapsingToolbarLayoutState state;
    private SurfaceView layout_sv; //(用来播放)
    private MediaPlayer mediaPlayer;
    private SurfaceHolder holder;
    private ImageView surfaceview_bg;
    private int currentPosition = 0; //视频播放的位置
    private Uri uri;
    private LinearLayout time_container;
    private TextView time_video_progress;
    private SeekBar video_seekbar;
    private TextView time_total;
    private boolean flag = false; //是否播放/暂停
    private int current;
    private boolean users;
    private  Timer timer;
    private TimerTask task;

    private enum  CollapsingToolbarLayoutState{
        EXPANDED, //展开
        COLLAPSED, //折叠
        INTERNEDIATE //中间
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case 0:
                    updateProgress();
                    break;
                case 1:
                    break;
            }
        }
    };


    //切换到别的fragment时候, 将视频暂停掉
    @Override
    protected void onInvisible() {
        super.onInvisible();
        Log.i(TAG, "onInvisible: 不可见");
        if (null != mediaPlayer && mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }

    }

    @Override
    public View initView() {
        Log.i(TAG, "initView: ----initView");
        if (null == view) {
            view =  LayoutInflater.from(mContext).inflate(R.layout.fragment_video, null);
        }
        layout_appbar = (AppBarLayout) view.findViewById(R.id.layout_appbar);
        layout_collapsingtoolbar = (CollapsingToolbarLayout) view.findViewById(R.id.layout_collapsingtoolbar);
        fl_img_boy = (FrameLayout) view.findViewById(R.id.fl_img_boy);
        layout_toolbar = (Toolbar) view.findViewById(R.id.layout_toolbar);
        surfaceview_bg = (ImageView)view.findViewById(R.id.surfaceview_bg); //surfaceView的背景

        //偏移量的监听
        layout_appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                //获取最大的偏移值
                int totalScrollRange = appBarLayout.getTotalScrollRange();
                layout_collapsingtoolbar.setTitleEnabled(false);
                if (verticalOffset == 0){
                    if (state != CollapsingToolbarLayoutState.EXPANDED){
                        state = CollapsingToolbarLayoutState.EXPANDED;
                        layout_toolbar.setTitle("展开");
                    }

                }else if (Math.abs(verticalOffset) >= totalScrollRange){
                    if (state != CollapsingToolbarLayoutState.COLLAPSED){
                        layout_toolbar.setTitle("折叠");
                        fl_img_boy.setVisibility(View.VISIBLE);
                        state = CollapsingToolbarLayoutState.COLLAPSED;
                        if(null != mediaPlayer && mediaPlayer.isPlaying()){
                            mediaPlayer.pause();
                        }

                    }

                }else{
                    if(state != CollapsingToolbarLayoutState.INTERNEDIATE){
                        if (state == CollapsingToolbarLayoutState.COLLAPSED){
                            fl_img_boy.setVisibility(View.GONE);
                        }
                        layout_toolbar.setTitle("中间");
                        state = CollapsingToolbarLayoutState.INTERNEDIATE;
                    }
                }
            }
        });

        findviewbyid(view);
        initSurfaceView();
        return view;
    }

    private void findviewbyid(View v) {
        time_container = (LinearLayout)v.findViewById(R.id.time_container);
        time_video_progress = (TextView)v.findViewById(R.id.time_video_progress);
        video_seekbar = (SeekBar)v.findViewById(R.id.video_seekbar);
        time_total = (TextView)v.findViewById(R.id.time_video_total);

    }

    private void initSurfaceView() {
        layout_sv = (SurfaceView)view.findViewById(R.id.layout_iv);
        holder = layout_sv.getHolder(); //得到显示界面内容的容器
        /**
         * 确保底层的表面是一个推送缓冲区表面，用于视频播放和摄像头的浏览
         */
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        // //在界面【最小化】时暂停播放，并记录holder播放的位置
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) { //holder被创建时回调
                Log.i(TAG, "surfaceCreated: 创建了");
                surfaceview_bg.setVisibility(View.VISIBLE);//使surfaceView的背景显示出来
                playNetWorkVideo();
//                playRawVideo();
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {  //holder宽高发生变化（横竖屏切换）时回调
                Log.i(TAG, "surfaceChanged: 改变了");
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) { //holder被销毁时回调。最小化时都会回调
                Log.i(TAG, "surfaceDestroyed: 销毁了");
                flag = false;
                if (null != mediaPlayer ){
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
                time_container.setVisibility(View.GONE);
            }
        });
        layout_sv.setOnClickListener(this);
        video_seekbar.setOnSeekBarChangeListener(onSeekBarListener);

    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.layout_iv:
                if (null != mediaPlayer && mediaPlayer.isPlaying()){
                    flag = false;
                    mediaPlayer.pause();
                    currentPosition = mediaPlayer.getCurrentPosition();
                    time_container.setVisibility(View.VISIBLE);
                }else{
                    flag = true;
                    mediaPlayer.start();
                    Log.i(TAG, "onClick: --"+mediaPlayer.getCurrentPosition());
                    updateProgress();
                }
                break;
        }
    }

    private void updateProgress(){
        if (flag ){
            current = mediaPlayer.getCurrentPosition();
                video_seekbar.setProgress(current);
                time_video_progress.setText(DateUtils.getTimeShowString(current/1000));
                Log.i(TAG, "----updateProgress:-- "+current);
                mHandler.sendEmptyMessageDelayed(0,10);
        }
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        Log.i(TAG, "onCompletion: 播放完毕");
        flag = false;
        time_container.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        currentPosition = 0;
        video_seekbar.setProgress(currentPosition);
        surfaceview_bg.setVisibility(View.GONE);
        time_container.setVisibility(View.VISIBLE);
        time_total.setText(DateUtils.getTimeShowString(mediaPlayer.getDuration()/1000)); //设置视频总长度
        time_video_progress.setText(DateUtils.getTimeShowString(currentPosition/1000));
        video_seekbar.setMax(mediaPlayer.getDuration());
        mediaPlayer.seekTo(currentPosition);

        Log.i(TAG, mediaPlayer.getDuration()+"onPrepared: 准备好了"+currentPosition);

    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        Log.i(TAG, "onError: 播放错误");
        flag = false;
        return false;
    }

    /**
     * 播放raw下的本地视屏文件
     */
    public void playRawVideo() {
            try {
                if (null != mediaPlayer){
                    mediaPlayer.reset();
                }
                mediaPlayer = MediaPlayer.create(mContext,R.raw.video);
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setOnErrorListener(this); //当播放中发生错误的时候回调。
                mediaPlayer.setLooping(false);  //设置是否循环播放

                mediaPlayer.setDisplay(holder);//****************在哪个容器里显示内容
//                mediaPlayer.prepare();  //缓冲, 播放本地即raw下的视频时候,不能加这个,否则报错
                mediaPlayer.setVolume(1.0F,1.0F);//设置音量，两个参数分别是左声道和右声道的音量
                mediaPlayer.setOnCompletionListener(this); //网络流媒体播放结束监听
                surfaceview_bg.setVisibility(View.GONE);
                mediaPlayer.seekTo(currentPosition);

                time_container.setVisibility(View.VISIBLE);
                time_total.setText(DateUtils.getTimeShowString(mediaPlayer.getDuration()/1000)); //设置视频总长度
                time_video_progress.setText(DateUtils.getTimeShowString(currentPosition/1000));
                Log.i(TAG, mediaPlayer.getDuration()+"onPrepared: 准备好了"+currentPosition);
                Log.i(TAG, "play: 准备了吗 啊");
            } catch (Exception e) {
                e.printStackTrace();
                MyToastUtils.show(mContext, "请检查是否有写SD卡权限"+e);
            }
    }

    /**
     * 播放网络视频
     */
    public void playNetWorkVideo(){
        try {
            if (null == mediaPlayer){
                mediaPlayer = new MediaPlayer(); //播放网络视频的时候用这个方法来创建
            }
            mediaPlayer.reset();
            mediaPlayer.setLooping(false);  //设置是否循环播放
            mediaPlayer.setDisplay(holder);//*********在哪个容器里显示内容
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnPreparedListener(VideoFragment.this); //当装备好的时候回调
            mediaPlayer.setOnCompletionListener(VideoFragment.this); //网络流媒体播放结束监听
            mediaPlayer.setOnErrorListener(VideoFragment.this); //当播放中发生错误的时候回调。
//            uri = Uri.parse("https://gslb.miaopai.com/stream/NR-AFsHNLuTZSet-uDvZFSNOlZ-Gt5SZeUa36w__.mp4?yx=&refer=weibo_app&Expires=1504085504&ssig=/0GI7JJCQe&KID=unistore,video");
            uri = Uri.parse("https://gslb.miaopai.com/stream/YT6~sGYBDJeqPufxq~XsRJ4sHdb9l2VmBrYh4A__.mp4?yx=&amp;refer=weibo_app&amp;mpflag=32&amp;mpr=1505538260&amp;Expires=1506407967&amp;ssig=E5%2B0ATbisE&amp;KID=unistore,video");//uri 网络视频
//            uri = Uri.parse("https://gslb.miaopai.com/stream/zjb313oV4hoPU9p~4R-A8hmBv12giV8B.mp4?yx=&amp;refer=weibo_app&amp;Expires=1506408504&amp;ssig=DntWei4zl8&amp;KID=unistore,video");//uri 网络视频
            mediaPlayer.setDataSource(mContext, uri);
            mediaPlayer.prepareAsync();  //缓冲
            Log.i(TAG, "play: 准备了吗 啊");
        } catch (Exception e) {
            e.printStackTrace();
            MyToastUtils.show(mContext, "请检查是否有写SD卡权限"+e);
        }
    }



    @Override
    public void initData() {
        Log.i(TAG, "initData: 是在加载数据吗");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "一会加载---"+TAG);
            }
        }, 5000);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG, "onDestroyView: --- destroy");
    }

    public SeekBar.OnSeekBarChangeListener onSeekBarListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//            if (fromUser && mediaPlayer != null) {
//                currentPosition = progress * mediaPlayer.getDuration() / 100;
//                mediaPlayer.seekTo(currentPosition);
//            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            mediaPlayer.seekTo(seekBar.getProgress());
            time_video_progress.setText(DateUtils.getTimeShowString(seekBar.getProgress()/1000));
        }
    };

}
