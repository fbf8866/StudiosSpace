package com.example.magic.myapplication.cookie;

import android.content.Context;
import android.util.Log;

import com.example.magic.myapplication.common.MyApplication;
import com.example.magic.myapplication.dao.CacheDao;
import com.example.magic.myapplication.utils.MyToastUtils;
import com.example.magic.myapplication.utils.NetsWorkUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * Created by Administrator on 2017/3/29.
 */
/**
 * okhttp请求 的时候的拦截配置
 * 即有网的时候请求为网络,没网的时候走缓存
 * 但是okhttp没有对post请求做缓存,只对get的做缓存了
 * 所以做法是在有网的时候 缓存到本地数据库,没网的时候从数据库读取数据
 */
public class CacheInterceptors implements Interceptor {
    private final String TAG = CacheInterceptors.class.getSimpleName();
    private Context context;
    private String params;
    public CacheInterceptors(Context context){
        this.context = context;
    }
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String url = request.url().toString(); //获取到请求的url地址
        Buffer buffer = new Buffer();
        Response responses = null;
        if (null == request.body()){
//            params = buffer.readString(Charset.forName("UTF-8"));
            params = "";
        }else{
            request.body().writeTo(buffer);
            params = buffer.readString(Charset.forName("UTF-8")); //获取到请求参数
        }
        if (NetsWorkUtils.isNetworkAvailable(context)){
            int maxAge = 60*2; // 在线缓存在2分钟内可读取
            Response response = chain.proceed(request);
            /**  获取MediaType，用于重新构建ResponseBody*/
            MediaType type = response.body().contentType();     //图片image/jpeg
            /**获取body字节即响应，用于存入数据库和重新构建ResponseBody*/
            byte[] bs = response.body().bytes();
            responses =response.newBuilder()
                    .removeHeader("Pragma")//清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, max-age=" + maxAge)
                    .body(ResponseBody.create(type,bs))   /**重新构建body，原因在于body只能调用一次，之后就关闭了*/
                    .build();

            /**将响应插入数据库*/
            CacheDao.getInstance(context).insertResponse(url, params, new String(bs,"UTF-8"));
            Log.i(TAG,"存入数据库--"+ new String(bs, "UTF-8").getBytes().length);
        }else{
            //没网的时候chain.proceed()不会执行
            int maxStale = 60 * 60 * 24 * 7; // 离线时缓存保存7天
            String b = CacheDao.getInstance(context).queryResponse(url); /**读出响应*/
            byte[] bytes;
            if (null != b){
                bytes = b.trim().getBytes();
                //构建一个新的response响应
                responses = new Response.Builder()
                        .removeHeader("Pragma")
                        .removeHeader("Cache-Control")
                        .header("Cache-Control", "public,only-if-cached,max-stale=" + maxStale)
                        .body(ResponseBody.create(MediaType.parse("application/json;charset=utf-8"), bytes))
//                        .body(ResponseBody.create(MediaType.parse("application/octet-stream"), bytes))
                        .request(request)
                        .protocol(Protocol.HTTP_1_1)
                        .code(200)
                        .build();
                Log.i(TAG,"读取数据库 返回"+bytes.length);
            }
        }
        return responses;
    }
}
