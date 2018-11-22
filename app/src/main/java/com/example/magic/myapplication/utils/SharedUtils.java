package com.example.magic.myapplication.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/27.
 */

public class SharedUtils {
    /**
     * 保存在手机里面的文件名
     */
    public static final String FILE_NAME = "share_data";
    public static SharedPreferences sp;
    private static SharedUtils instance = null;
    public static SharedUtils getInstance(Context context){
        if (null == instance){
            synchronized (SharedUtils.class){
                if (null == instance){
                    instance = new SharedUtils();
                }
            }
        }
        if (null == sp){
            sp = context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
        }
        return instance;
    }

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param key
     * @param object
     */
    public  void put( String key, Object object)
    {
        SharedPreferences.Editor editor = sp.edit();

        if (object instanceof String)
        {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer)
        {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean)
        {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float)
        {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long)
        {
            editor.putLong(key, (Long) object);
        } else
        {
            editor.putString(key, object.toString());
        }

        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param key
     * @param defaultObject
     * @return
     */
    public  Object get( String key, Object defaultObject)
    {

        if (defaultObject instanceof String)
        {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer)
        {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean)
        {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float)
        {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long)
        {
            return sp.getLong(key, (Long) defaultObject);
        }

        return null;
    }

    /**
     * 移除某个key值已经对应的值
     * @param key
     */
    public  void remove( String key)
    {
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 清除所有数据
     */
    public  void clear()
    {
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 查询某个key是否已经存在
     * @param key
     * @return
     */
    public  boolean contains( String key)
    {
        return sp.contains(key);
    }

    /**
     * 返回所有的键值对
     *
     * @return
     */
    public  Map<String, ?> getAll()
    {
        return sp.getAll();
    }

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     *
     * @author zhy
     *
     */
    private  static class SharedPreferencesCompat
    {
        private  static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         *
         * @return
         */
        @SuppressWarnings({ "unchecked", "rawtypes" })
        private  static Method findApplyMethod()
        {
            try
            {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e)
            {
            }

            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         *
         * @param editor
         */
        public static void apply(SharedPreferences.Editor editor)
        {
            try
            {
                if (sApplyMethod != null)
                {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (IllegalArgumentException e)
            {
            } catch (IllegalAccessException e)
            {
            } catch (InvocationTargetException e)
            {
            }
            editor.commit();
        }
    }

    public  void putList(String key,List list){
        SharedPreferences.Editor edit = sp.edit();
        try {
            String liststr =ListToString(list);
            edit.putString(key, liststr);
            edit.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public  List getList(String key){
        String liststr = sp.getString(key, "");
        try {
            List list = StringToList(liststr);
            return list;
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public  String ListToString(List list)
            throws IOException {
        //创建ByteArrayOutputStream对象，用来存放字节文件。
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        //得到的字符放到到ObjectOutputStream
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                byteArrayOutputStream);
        // writeObject 方法负责写入特定类的对象的状态，以便相应的 readObject 方法可以还原它
        objectOutputStream.writeObject(list);
        //Base64.encode将字节文件转换成Base64编码存在String中
        String string = new String(Base64.encode(
                byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
        // 关闭Stream
        objectOutputStream.close();
        byteArrayOutputStream.close();
        return string;

    }

    @SuppressWarnings("unchecked")
    public  List StringToList(String string)throws StreamCorruptedException, IOException,ClassNotFoundException {
        byte[] b = Base64.decode(string.getBytes(),Base64.DEFAULT);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                b);
        ObjectInputStream objectInputStream = new ObjectInputStream(
                byteArrayInputStream);
        List list = (List) objectInputStream
                .readObject();
        // 关闭Stream
        objectInputStream.close();
        byteArrayInputStream.close();
        return list;
    }


}
