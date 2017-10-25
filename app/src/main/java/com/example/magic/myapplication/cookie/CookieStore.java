package com.example.magic.myapplication.cookie;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**
 * Created by Administrator on 2017/3/29.
 */

public interface CookieStore {

        void add(HttpUrl uri, List<Cookie> cookie);

        List<Cookie> get(HttpUrl uri);

        List<Cookie> getCookies();

        boolean remove(HttpUrl uri, Cookie cookie);

        boolean removeAll();

}
