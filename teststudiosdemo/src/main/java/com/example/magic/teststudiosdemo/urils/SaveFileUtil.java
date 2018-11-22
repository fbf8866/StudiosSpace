package com.example.magic.teststudiosdemo.urils;


import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by Administrator on 2017/5/31.
 */

public class SaveFileUtil {
    private static final String TAG = "SaveFileUtil";
    private static SaveFileUtil saveFileUtil;
    private static Context mContext;

    public SaveFileUtil(Context context) {
        this.mContext = context;
    }

    public static SaveFileUtil getInstance(Context c){
        if (null == saveFileUtil){
            synchronized (SaveFileUtil.class){
                if (null == saveFileUtil){
                    saveFileUtil = new SaveFileUtil(c);
                }
            }
        }
        return saveFileUtil;
    }

    /**
     * 按照流文件进行保存数据
     * 写入数据
     * @param fileName 保存的文件名称
     * @param text 要保存的文件内容
     */
    public  static void saveAsFile(String fileName,String text){
        BufferedWriter writer = null;
        try {
            FileOutputStream fileOutputStream = mContext.openFileOutput(fileName, Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
            writer.write(text);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }finally {
            if (null != writer){
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 按照流文件获取数据
     * 读取数据
     * @param fileName 文件名称
     * @return 返回写入的文件内容
     */
    public String  getAsFile(String fileName){
        BufferedReader reader = null;
        StringBuffer buffer = new StringBuffer();
        try {
            FileInputStream fileInputStream = mContext.openFileInput(fileName);
            reader = new BufferedReader(new InputStreamReader(fileInputStream));
            String line = "";
            while((line = reader.readLine()) != null){
                buffer.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(null != reader){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return buffer.toString();
    }

}
