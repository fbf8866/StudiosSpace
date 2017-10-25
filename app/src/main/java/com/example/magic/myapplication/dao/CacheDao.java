package com.example.magic.myapplication.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/30.
 * 数据库的管理类,增删改查
 */

public class CacheDao {
        private static volatile CacheDao cacheDao;
        private static final String DB_NAME = "okhttpresponse.db";
        private static final int DB_VERSION = 1;
        private MyDbHelper helper;

        private CacheDao(Context context){
            helper = new MyDbHelper(context.getApplicationContext(),DB_NAME,null,DB_VERSION);
        }
        public static CacheDao getInstance(Context context) {
            if (cacheDao == null) {
                synchronized (CacheDao.class) {
                    if (cacheDao == null) {
                        cacheDao = new CacheDao(context);
                    }
                }
            }
            return cacheDao;
        }
        //查
//        public  String queryResponse(String urlKey, String params) {
        public  String queryResponse(String urlKey) {
            SQLiteDatabase db = helper.getWritableDatabase();
            Cursor cursor = db.rawQuery("select * from cache where url=?",new String[]{urlKey.toString()});
//            Cursor cursor = db.rawQuery("select * from cache where url=? and params=?",new String[]{urlKey.toString(), params.toString()});
//            Cursor cursor = db.rawQuery("select * from cache where ?",new String[]{urlKey.toString()});
            String response = null;
            while(cursor.moveToNext()){
                response = cursor.getString(cursor.getColumnIndex("response"));
            }
            return response;
        }
        //增
        public void insertResponse(String urlKey, String params, String value) {
            SQLiteDatabase db = helper.getWritableDatabase();
                db.execSQL("insert into cache (url,params,response) values (?,?,?)",new Object[] { urlKey, params ,value});
            db.close();
        }
        //改
        public void updateResponse(String urlKey, String params, String value) {
            SQLiteDatabase db = helper.getWritableDatabase();
        }
        //删
        public void deleteResponse(String urlKey, String params) {
            SQLiteDatabase db = helper.getWritableDatabase();
        }
}
