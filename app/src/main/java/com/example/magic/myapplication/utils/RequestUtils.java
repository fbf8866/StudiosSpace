package com.example.magic.myapplication.utils;

import android.content.Context;

import java.util.Map;

/**
 * Created by Administrator on 2017/3/24.
 */

public class RequestUtils {
    public static String setUrl( String Method, Map<String, String> params) {

		String URL = "http://111.13.63.1:8181/website/rest/";

        StringBuilder strb = new StringBuilder();
        if (params != null) {
            for (String key : params.keySet()) {
                strb.append("&" + key + "=").append(params.get(key));
            }
        }
        if (strb.length() > 0) {
            strb.deleteCharAt(0);// 删除第一个&
            return URL + Method + "?" + strb;
        } else {
            return URL + Method;
        }
    }
}
