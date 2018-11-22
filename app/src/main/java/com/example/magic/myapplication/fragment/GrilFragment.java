package com.example.magic.myapplication.fragment;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.magic.myapplication.CircleTransformation;
import com.example.magic.myapplication.R;
//import com.example.magic.myapplication.bean.CookieBean;
import com.example.magic.myapplication.bean.CookieBean;
import com.example.magic.myapplication.common.CustomPb;
import com.example.magic.myapplication.utils.GsonUtils;
import com.example.magic.myapplication.utils.OKhttpManager;
import com.example.magic.myapplication.utils.RequestUtils;
import com.example.magic.myapplication.utils.SharedUtils;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/3/15.
 */

public class GrilFragment extends BaseFragment implements View.OnClickListener {
    private String TAG = GrilFragment.class.getSimpleName();
    private View view;
    private String cookie;
    private Button fragment_girl_btn;
    private ImageView fragment_girl_iv;
    private String num;
    private CustomPb custom_pb;
    private boolean isDown = false;
    private boolean flag = false;

    @Override
    public View initView() {
        if (null == view) {
            view =  LayoutInflater.from(mContext).inflate(R.layout.fragment_girl, null);
        }
        num = String.valueOf(15103240001L);
        fragment_girl_btn = (Button) view.findViewById(R.id.fragment_girl_btn);
        fragment_girl_iv = (ImageView) view.findViewById(R.id.fragment_girl_iv);
        custom_pb = (CustomPb) view.findViewById(R.id.custom_pb);
        fragment_girl_btn.setOnClickListener(this);
        custom_pb.setOnCustomClickListener(new CustomPb.onCustomPbClickListener() {
            @Override
            public void onClick() {
                if (isDown){
                    isDown = false;
                }else{
                    isDown = true;
                }
                if (isDown){
                    //这个加载图片
                    Picasso.with(mContext)
                            .load("http://pic.58pic.com/58pic/16/62/63/97m58PICyWM_1024.jpg")
                            .fetch(new com.squareup.picasso.Callback() { //存缓存是否成功
                                @Override
                                public void onSuccess() {
                                    flag = true;
                                    Picasso.with(mContext)
                                            .load("http://img1.imgtn.bdimg.com/it/u=2921733034,1911830275&fm=23&gp=0.jpg")
//                                            .load("http://sucai.qqjay.com/qqjayxiaowo/201210/26/1.jpg")
                                            .error(R.mipmap.ic_launcher) //加载失败时候显示的图片
                                            .placeholder(R.mipmap.ic_launcher)//没有加载图片时候显示的图片
                                            .transform(new CircleTransformation()) //转化为圆形
                                            .centerInside()
//                                            .noFade() //默认的Picasso库有一个图片的淡入效果,如果不喜欢,可以加上这行,意为取消淡入效果
                                            .resize(300,300)
                                            .onlyScaleDown()         //效率提高图片的加载时间配合resize(x,y)使用。
                                            .into(fragment_girl_iv);

                                    //这行是可以在图片的左上角 显示一个三角形的图片, 红色的标示从网上请求,蓝色标示从disk中,绿色标示从memory中
                                    Picasso.with(mContext).setIndicatorsEnabled(true);
                                    Log.i(TAG,"缓存"+flag);
                                }

                                @Override
                                public void onError() {
                                    flag = false;
                                    Picasso.with(mContext)
                                            .load("http://pic.58pic.com/58pic/16/62/63/97m58PICyWM_1024.jpg")
                                            .error(R.mipmap.ic_launcher) //加载失败时候显示的图片
                                            .into(fragment_girl_iv);
                                    Log.i(TAG,"缓存"+flag);
                                }
                            });

                    OKhttpManager.isDown = isDown;
//                    OKhttpManager.downLoadBitmap("http://pic.58pic.com/58pic/16/62/63/97m58PICyWM_1024.jpg", new OKhttpManager.BitmapCallBack() {
//                        @Override
//                        public void onSuccess(final Bitmap bitmap) {
//                            Log.i(TAG,"下载成功"+bitmap);
//                            getActivity().runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    fragment_girl_iv.setImageBitmap(bitmap);
//                                }
//                            });
//                        }
//
//                        @Override
//                        public void onError() {
//                            Log.i(TAG,"下载失败");
//
//                        }
//
//                        @Override
//                        public void onProgress(final int progress) {
//                            Log.i(TAG,"下载进度"+progress);
//                            getActivity().runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    custom_pb.setProgress(progress);
//                                }
//                            });
//
//                        }
//                    });
                }else{
                    OKhttpManager.isDown = isDown;
                }

            }
        });
        return view;
    }



    @Override
    public void initData() {
        Log.i(TAG, "initData: 女孩加载数据了...");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "一会加载"+TAG);
                Map<String, String> sendMesParams = new HashMap<String, String>();
                sendMesParams.put("phone_number", num);
                String url = RequestUtils.setUrl("sendPhone/sendPhoneNo.json",sendMesParams);
                OKhttpManager.postAsync(url,sendMesParams, new OKhttpManager.DataCallBack() {
                    @Override
                    public void requestFailure(Call call, IOException e) {
                        Log.i(TAG,"请求失败"+e);
                    }

                    @Override
                    public void requestSuccess(String result) {
                        Log.i(TAG,"请求成功"+result);
                        CookieBean cookieBean = GsonUtils.GsonToBean(result, CookieBean.class);
                        if (null != cookieBean){
                            CookieBean.SendSmsBeanCookieBean sendSmsBean = cookieBean.getSendSmsBean();
                            cookie = sendSmsBean.getSessionId();
                            SharedUtils.getInstance(mContext).put("Cookie",cookie);
                        }
                    }
                });
//                OkHttpUtils
//                        .post()
//                        .url(url)
//                        .addParams("phone_number",num)
//                        .build()
//                        .execute(new Callback() {
//                            @Override
//                            public Object parseNetworkResponse(Response response, int id) throws Exception {
//                                return response.body().string();
//                            }
//
//                            @Override
//                            public void onError(Call call, Exception e, int id) {
//
//                            }
//
//                            @Override
//                            public void onResponse(Object response, int id) {
//                                Log.i(TAG,"请求成功"+response.toString());
//                                CookieBean cookieBean = GsonUtils.GsonToBean(response.toString(), CookieBean.class);
//                                cookie = cookieBean.getSendSmsBean().getSessionId();
//                                SharedUtils.put(mContext,"Cookie",cookie);
//                            }
//                        });
            }
        }, 5000);


    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.fragment_girl_btn:
                    Map<String, String> loadParams = new HashMap<String, String>();
                    loadParams.put("phone_number",num);
                    loadParams.put( "checkCode","1234");
                    loadParams.put( "departmanager","");
                    loadParams.put( "signal","");
                    loadParams.put( "confirm_signal","");

                    String loadUrl = RequestUtils.setUrl("loginRest/clientLogin.json",loadParams);
                OKhttpManager.postAsync(loadUrl, loadParams,new OKhttpManager.DataCallBack() {
                    @Override
                    public void requestFailure(Call call, IOException e) {
                        Log.i(TAG,"登录网失败");
                    }

                    @Override
                    public void requestSuccess(String result) {
                        Log.i(TAG,"登录网成功 : "+result);
                    }
                });


                /**
                 * 以下是用张弘扬封装的okhttp请求
                 */
//                    OkHttpUtils.post()
//                            .url(loadUrl)
//                            .addParams("phone_number",num)
//                            .addParams("checkCode","1234")
//                            .addParams("departmanager","")
//                            .addParams("signal","")
//                            .addParams("confirm_signal","")
//                            .build()
//                            .execute(new Callback() {
//                                @Override
//                                public Object parseNetworkResponse(Response response, int id) throws Exception {
//                                    return response.body().string();
//                                }
//
//                                @Override
//                                public void onError(Call call, Exception e, int id) {
//
//                                }
//
//                                @Override
//                                public void onResponse(Object response, int id) {
//                                    Log.i(TAG,"登录成功 : "+response.toString());
//                                }
//                            });

//                OkHttpUtils
//                        .get()
//                        .url("http://pic6.huitu.com/res/20130116/84481_20130116142820494200_1.jpg")
//                        .build()
//                        .execute(new BitmapCallback() {
//                            @Override
//                            public void onError(Call call, Exception e, int id) {
//                                Log.i(TAG,"下载成功看吗 : ");
//                            }
//
//                            @Override
//                            public void onResponse(Bitmap response, int id) {
//                                Log.i(TAG,"下载成功 : "+response.toString());
//                            }
//
//                            @Override
//                            public void inProgress(float progress, long total, int id) {
//                                super.inProgress(progress, total, id);
//                                Log.i(TAG,"成功进度 : "+progress);
//                            }
//                        });


                break;
        }
    }
}
