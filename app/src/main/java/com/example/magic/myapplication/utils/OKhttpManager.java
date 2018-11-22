package com.example.magic.myapplication.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.magic.myapplication.common.MyApplication;
import com.example.magic.myapplication.cookie.CacheInterceptors;
import com.example.magic.myapplication.cookie.CookieJarImpl;
import com.example.magic.myapplication.cookie.LogInterceptor;
import com.example.magic.myapplication.cookie.PersistentCookieStore;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/3/23.
 */

public class OKhttpManager {
    private final String TAG = OKhttpManager.class.getSimpleName();
    /**
     * 上传字符串
     */
    public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");
    /**
     * 上传json
     */
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private Cache cache = null;
    private File file = null;

    private OkHttpClient client;
    private static OKhttpManager oKhttpManager;
    private Handler mHandler;
    private final int TIMEOUT =1000;
    public static boolean isDown = false;
    private CacheInterceptors cacheInterceptors;

    /**
     * 单例获取 OKhttpManager实例
     */
    private  static  OKhttpManager getInstance() {
        if (oKhttpManager == null){
            synchronized (OKhttpManager.class){
                if (oKhttpManager == null) {
                    oKhttpManager = new OKhttpManager();
                }
            }
        }

        return oKhttpManager;
    }

    private OKhttpManager() {
        File cacheDir = MyApplication.context.getCacheDir();
        if (null != cacheDir){
            file = new File(cacheDir,"cache");
            cache = new Cache(file,1024*1024*10); //10M的缓存空间
        }
        cacheInterceptors = new CacheInterceptors(MyApplication.context);
        client = new OkHttpClient.Builder()
            .addInterceptor(new LogInterceptor())/**设置应用拦截,用于日志打印*/
            .addNetworkInterceptor(cacheInterceptors) /**设置网络拦截,有网请求,无网走缓存*/
            .addInterceptor(cacheInterceptors) /**这里必须加上这句,否则无网的时候不走缓存*/
            .cache(cache) /** 设置10M的缓存*/
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS) /** 设置连接的超时时间,客服端和服务端建立连接*/
            .writeTimeout(TIMEOUT, TimeUnit.SECONDS).readTimeout(TIMEOUT, TimeUnit.SECONDS)  /*** 设置响应的超时时间,客户端发送数据到服务端writeTimeout*/
            .readTimeout(3000, TimeUnit.SECONDS)  /*** 请求的超时时间,，服务端将相应数据发送到客户端readTimeout*/
            .cookieJar(new CookieJarImpl(new PersistentCookieStore(MyApplication.context)))  /*** 允许使用Cookie*/
            .retryOnConnectionFailure(true) /** 错误重连*/
            .build();

        mHandler = new Handler(Looper.getMainLooper());
    }

    /**************
     * 内部逻辑处理
     ****************/
    /**
     * 同步get
     * @param url
     * @return
     * @throws IOException
     */
    private Response p_getSync(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        return response;
    }

    private String p_getSyncAsString(String url) throws IOException {
        return p_getSync(url).body().string();
    }

    /**
     * 异步get
     * @param url
     * @param callBack
     */
    private void p_getAsync(String url, final DataCallBack callBack) {
        final Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                deliverDataFailure(call, e, callBack);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    String result = response.body().string();
                    deliverDataSuccess(result, callBack);
                } catch (IOException e) {
                    deliverDataFailure(call, e, callBack);
                }
            }
        });
    }

    private void p_postAsync(String url, Map<String, String> params, final DataCallBack callBack) {
        RequestBody requestBody = null;
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey().toString();
            String value = null;
            if (entry.getValue() == null) {
                value = "";
            } else {
                value = entry.getValue().toString();
            }
            builder.add(key, value);
        }
        requestBody = builder.build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                deliverDataFailure(call, e, callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String result = response.body().string();
                    deliverDataSuccess(result, callBack);
                } catch (IOException e) {
                    deliverDataFailure(call, e, callBack);
                }
            }
        });
    }

    /**
     * @param url 下载连接
     * @param saveDir 储存下载文件的SDCard目录
     * @param listener 下载监听
     */
    public void downloadFile(final String url, final String saveDir, final OnDownloadListener listener) {
        Request request = new Request
                .Builder()
                .url(url)
                .build();
        client.newCall(request)
                .enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 下载失败
                listener.onDownloadFailed();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                // 储存下载文件的目录
                String savePath = isExistDir(saveDir);
                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    File file = new File(savePath, getNameFromUrl(url));
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        // 下载中
                        listener.onDownloading(progress);
                    }
                    fos.flush();
                    // 下载完成
                    listener.onDownloadSuccess();
                } catch (Exception e) {
                    listener.onDownloadFailed();
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                    }
                }
            }
        });
    }

    /**
     * @param saveDir
     * @return
     * @throws IOException
     * 判断下载目录是否存在
     */
    private String isExistDir(String saveDir) throws IOException {
        // 下载位置
        File downloadFile = new File(Environment.getExternalStorageDirectory(), saveDir);
        if (!downloadFile.mkdirs()) {
            downloadFile.createNewFile();
        }
        String savePath = downloadFile.getAbsolutePath();
        return savePath;
    }

    /**
     * @param url
     * @return
     * 从下载连接中解析出文件名
     */
    private String getNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    public interface OnDownloadListener {
        /**
         * 下载成功
         */
        void onDownloadSuccess();

        /**
         * @param progress
         * 下载进度
         */
        void onDownloading(int progress);

        /**
         * 下载失败
         */
        void onDownloadFailed();
    }

    /**
     * 下载图片
     * @param url
     * @param listener
     */
    public void downloadBitmap(final String url, final BitmapCallBack listener) {

        Request request = new Request
                .Builder()
                .url(url)
                .build();
        client.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        // 下载失败
                        listener.onError();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        InputStream inputStream = response.body().byteStream();

                        byte[] buf = new byte[2048];
                        long total = response.body().contentLength();
                        Log.i(TAG,"请求数据库"+total);
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        int len;
                        int num = 0;
                        try{
                            while(isDown && (len = inputStream.read(buf))>0){
                                num += len;
                                outputStream.write(buf,0,len);
                                int progress = (int) (num * 1.0f / total * 100);
                                listener.onProgress(progress);
                            }
                            outputStream.flush();
                            Bitmap bitmap = BitmapFactory.decodeByteArray(outputStream.toByteArray(), 0, outputStream.size());
                            listener.onSuccess(bitmap);

                            Log.i(TAG,outputStream.size()+"--图片--"+outputStream.toByteArray()+"--"+bitmap);
                        }catch (Exception e){
                            listener.onError();
                        }finally {
                            if (null != inputStream){
                                inputStream.close();
                            }
                            if (null != outputStream){
                                outputStream.close();
                            }
                        }
                    }
                });
    }

    public interface  BitmapCallBack {
        void onSuccess(Bitmap bitmap);
        void onError();
        void onProgress(int progress);
    }

    /**
     * 上传字符串
     * @param url 上传地址
     * @param s 上传的串
     * @param callBack
     */
    public void postString(String url, String s, final DataCallBack callBack){
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(MEDIA_TYPE_MARKDOWN,s))
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                deliverDataFailure(call, e, callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String result = response.body().string();
                    deliverDataSuccess(result, callBack);
                } catch (IOException e) {
                    deliverDataFailure(call, e, callBack);
                }
            }
        });
    }

    /**
     * 上传json
     * @param url
     * @param json
     * @param callBack
     */
    public void postJson(String url, String json, final DataCallBack callBack){
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                deliverDataFailure(call, e, callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String result = response.body().string();
                    deliverDataSuccess(result, callBack);
                } catch (IOException e) {
                    deliverDataFailure(call, e, callBack);
                }
            }
        });
    }


    /**
     * 数据分发的方法
     */
    private void deliverDataFailure(final Call call, final IOException e, final DataCallBack callBack) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    callBack.requestFailure(call, e);
                }
            }
        });
    }

    private void deliverDataSuccess(final String result, final DataCallBack callBack) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    callBack.requestSuccess(result);
                }
            }
        });
    }


    /******************
     * 对外公布的方法
     *****************/
    /**
     * //同步GET，返回Response类型数据
     * @param url
     * @return
     * @throws IOException
     */
    public static Response getSync(String url) throws IOException {
        return getInstance().p_getSync(url);
    }


    /**
     * //同步GET，返回String类型数据
     * @param url
     * @return
     * @throws IOException
     */
    public static String getSyncAsString(String url) throws IOException {
        return getInstance().p_getSyncAsString(url);
    }

    /**
     * //异步GET请求
     * @param url
     * @param callBack
     */
    public static void getAsync(String url, DataCallBack callBack) {
        getInstance().p_getAsync(url, callBack);
    }

    /**
     * //异步POST请求
     * @param url
     * @param params
     * @param callBack
     */
    public static void postAsync(String url, Map<String, String> params, DataCallBack callBack) {
        getInstance().p_postAsync(url, params ,callBack);
    }

    /**
     * 对外暴露下载文件
     * @param url
     * @param saveDir
     * @param listener
     */
    public static void downLoad(String url, final String saveDir, OnDownloadListener listener){
        getInstance().downloadFile(url,saveDir,listener);
    }

    /**
     * 对外暴露下载图片
     * @param url
     * @param listener
     */
    public static void downLoadBitmap(String url, BitmapCallBack listener){
        getInstance().downloadBitmap(url,listener);
    }

    /**
     * 对外暴露的上传字符串的方法
     * @param url
     * @param s
     * @param callBack
     */
    public static void postStrings(String url, String s,DataCallBack callBack){
        getInstance().postString(url,s,callBack);
    }

    /**
     * 对外暴露的上传json
     * @param url
     * @param json
     * @param callBack
     */
    public static void postJsons(String url, String json,DataCallBack callBack){
        getInstance().postJson(url,json,callBack);
    }


    /**
     * 数据回调接口
     */
    public interface DataCallBack {
        void requestFailure(Call call, IOException e);

        void requestSuccess(String result);
    }


}
