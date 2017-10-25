package com.example.magic.teststudiosdemo.urils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;


import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;

import java.io.IOException;

/**
 * Created by Administrator on 2017/5/16.
 */

public class NetUtils {
    public static enum NetType {
        WIFI,  NONE,MOBILE
    }
    /**
     * 判断连接的是什么网络
     * @param context
     * @return
     */
    public static NetType getAPNType(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null) {
            return NetType.NONE;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
                return NetType.MOBILE;
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            return NetType.WIFI;
        }
        return NetType.NONE;
    }

    /**
     * 通过判断wifi连接的时候,是否可以上网
     * @return
     */
    public static boolean isConnectNetwork() {
        HttpClient client = new DefaultHttpClient();
        client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
        client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);
        // HttpGet连接对象使用get方式请求
        HttpGet myget = new HttpGet("http://www.baidu.com/");
        HttpResponse response = null;

        try {
            response = client.execute(myget);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        // 返回值为200，即为服务器成功响应了请求,其余的，则为失败
        if (response.getStatusLine().getStatusCode() == 200) {
            return true;
        }
        return false;
    }

}
