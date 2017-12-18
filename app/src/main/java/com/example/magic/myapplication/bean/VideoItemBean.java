package com.example.magic.myapplication.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/12/18.
 * 视频fragment中的条目的bean
 */

public class VideoItemBean {
    //视频item中的简介
    @SerializedName("introduction")
    private String introduction;
    public String getIntroduction(){
        return introduction;
    }

    //图片的资源id
    @SerializedName("iconId")
    private String iconId;
    public String getIconId(){
        return iconId;
    }

    //视频关注
    @SerializedName("attention")
    private String attention;
    public String getAttention(){
        return attention;
    }

    //视频的连接地址
    @SerializedName("videoUrl")
    private String videoUrl;
    public String getVideoUrl(){
        return videoUrl;
    }

    public VideoItemBean(String introduction,String iconId, String attention,String videoUrl){
        this.introduction = introduction;
        this.iconId  = iconId;
        this.attention = attention;
        this.videoUrl = videoUrl;
    }
}
