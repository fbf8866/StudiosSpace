package com.example.magic.myapplication.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/3/30.
 * 创建的本地数据库,用于缓存网络请求时候的数据
 *
 */

public class MyDbHelper extends SQLiteOpenHelper {
    static final String CACHE_TABLE  = "cache";
    public MyDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table " +CACHE_TABLE + " (url text, params text, response text)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + CACHE_TABLE;
        db.execSQL(sql);
        onCreate(db);
    }
}
