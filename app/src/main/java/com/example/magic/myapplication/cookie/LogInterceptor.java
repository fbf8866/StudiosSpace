package com.example.magic.myapplication.cookie;

import android.util.Log;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/3/29.
 */

/**
 * okhppt的应用拦截,常用的就是请求时候的日志打印
 * 在请求前和请求后各打一次log
 */
public class LogInterceptor implements Interceptor {
    private final String  TAG = LogInterceptor.class.getSimpleName();
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Headers headers = request.headers();
        if (headers != null && headers.size()>0){
            Log.i(TAG,"request:--"+headers.toString());
        }
            Response response = chain.proceed(request);

            for (int i = 0;i <response.headers().size();i++) {
                Log.i(TAG, "response:--" + response.headers().name(i) + "---" + response.headers().value(i));
            }

        return response;
    }
}
