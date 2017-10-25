package com.example.magic.myapplication.utils;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.net.URI;

/**
 * Created by Administrator on 2017/6/19.
 */

public class CameraUtils {
    private static final String TAG = "CameraUtils";
    private static CameraUtils cu;
    private static Context mContext;
    private static Uri uri;

    public static CameraUtils getInstance(Context c){
        if (null == cu){
            synchronized (CameraUtils.class){
                if (null == cu){
                    cu = new CameraUtils(c);
                }
            }
        }
        return cu;
    }

    private CameraUtils(Context context){
        mContext = context;
    }

    /**
     * 拍照
     * @param authority 在manifest中的对应一致
     * @return
     */
    public  Uri takePhotos(String authority){
        if (TextUtils.isEmpty(authority)){
            Log.e(TAG, "takePhotos: authority is null");
            return null;
        }
        try {
            CleanCacheUtils cleanCacheUtils = new CleanCacheUtils(mContext);
            String totalCacheSize = cleanCacheUtils.getCacheSize();
            Log.i(TAG, "takePhotos:  拍照---缓存"+totalCacheSize);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //创建file对象,用于存储拍照的图片 (应用关联缓存目录,6.0后,读取sd卡列为危险权限)
        File outputImg = new File(mContext.getExternalCacheDir(),System.currentTimeMillis()+"take_img.jpg");
        Log.i(TAG, "takePhotos: 缓存路径"+outputImg.getAbsolutePath());
        try {
            if (outputImg.exists()) {
                outputImg.delete();
            }
            outputImg.createNewFile();

        } catch (IOException e) {
            e.printStackTrace();
        }
        //7.0之后使用本地真实路径的uri被认为是不安全的,会报异常,所以用FileProvider这个内容提供器来做
        if (Build.VERSION.SDK_INT >= 24){
           uri = FileProvider.getUriForFile(mContext, authority, outputImg);
        }else{
            uri = Uri.fromFile(outputImg);
        }
        Log.i(TAG, "takePhotos: 拍照uri----"+uri);
      return uri;
    }


    /**
     * 4.4版本以上 从相册获取照片
     * @param data
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
//    public Bitmap getImageOnKitKat(Intent data){
    public String getImageOnKitKat(Intent data){
        String imagePath = null;
        Uri dataUri = data.getData();
        if (DocumentsContract.isDocumentUri(mContext,dataUri)){
            //如果是document类型的Uri,则通过document id处理
            String docId = DocumentsContract.getDocumentId(dataUri);
            if ("com.android.providers.media.documents".equals(dataUri.getAuthority())){
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if("com.android.providers.downloads.documents".equals(dataUri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri,null);
            }
        }else if ("content".equalsIgnoreCase(dataUri.getScheme())){
            imagePath = getImagePath(dataUri,null);
        }else if("file".equalsIgnoreCase(dataUri.getScheme())){
            imagePath = dataUri.getPath();
        }
        return imagePath;
        //根据路径来显示图片
//        return displayImage(imagePath);
    }

    /**
     * 4.4之前从相册选图片
     */
//    public Bitmap getImageBeforeKitKat(Intent data){
    public String getImageBeforeKitKat(Intent data){
        Uri dataUri = data.getData();
        String imagePath = getImagePath(dataUri,null);
        return imagePath;
//        return displayImage(imagePath);
    }

    private String getImagePath(Uri uris,String selection){
        String path = null;
        //通过uris和selection来获取真实的图片路径
        Cursor cursor = mContext.getContentResolver().query(uris, null, selection, null, null);
        if (null != cursor){
            if (cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    /**
     * 转化为图片
     * @param path
     * @return
     */
    private Bitmap displayImage(String path){
        Bitmap bitmap;
        if (null != path){
            bitmap = BitmapFactory.decodeFile(path);
        }else{
            return null;
        }
        return bitmap;
    }
}
