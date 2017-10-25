package com.example.magic.myapplication.cookie;


import android.util.Log;

import com.example.magic.myapplication.common.MyApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

public class CookieJarImpl implements CookieJar {
//    private final PersistentCookieStore cookieStore = new PersistentCookieStore(MyApplication.context);
//
//    @Override
//    public synchronized void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
//        if (cookies != null && cookies.size() > 0) {
//            for (Cookie item : cookies) {
//                cookieStore.add(url, item);
//            }
//        }
//    }
//
//    @Override
//    public synchronized List<Cookie> loadForRequest(HttpUrl url) {
//        return cookieStore.get(url);
//    }

    private CookieStore cookieStore;

    public CookieJarImpl(CookieStore cookieStore)
    {
        if (cookieStore == null) {
            throw new IllegalArgumentException(String.format("cookieStore can not be null.", ""));
        }
        this.cookieStore = cookieStore;
    }

    @Override
    public synchronized void saveFromResponse(HttpUrl url, List<Cookie> cookies)
    {
        cookieStore.add(url, cookies);
    }

    @Override
    public synchronized List<Cookie> loadForRequest(HttpUrl url)
    {
        return cookieStore.get(url);
    }

    public CookieStore getCookieStore()
    {
        return cookieStore;
    }
}
