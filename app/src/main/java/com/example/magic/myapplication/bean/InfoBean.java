package com.example.magic.myapplication.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/10/30.
 */

public class InfoBean {

   //图片的标题
    @SerializedName("title")
    private String title;
    public String getTitle(){
        return title;
    }

    //图片的资源id
    @SerializedName("iconId")
    private String iconId;
    public String getIconId(){
        return iconId;
    }

    public InfoBean(String title, String iconId) {
        super();
        this.title = title;
        this.iconId = iconId;
    }
}
