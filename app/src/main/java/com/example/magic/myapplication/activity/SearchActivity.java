package com.example.magic.myapplication.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.example.magic.myapplication.R;
import com.example.magic.myapplication.common.CustomSearchView;
import com.example.magic.myapplication.common.CustomToolBar;
import com.example.magic.myapplication.common.dialog.DialogFactory;
import com.example.magic.myapplication.utils.MyToastUtils;
import com.example.magic.myapplication.utils.NetUtils;
import com.example.magic.myapplication.utils.StoreInfomationUtils;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Administrator on 2017/7/27.
 */
public class SearchActivity extends BaseActivity{
    private static final String TAG = "SearchActivity";
    private CustomToolBar toolbar;
    private CustomSearchView cusrom_searchview;
    private SweetAlertDialog mDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        initData();

        Intent intent = new Intent(this,ScreenShotActivity.class);
        startActivity(intent);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        cusrom_searchview = (CustomSearchView) findViewById(R.id.cusrom_searchview);
        toolbar = (CustomToolBar) findViewById(R.id.toolbar);
        toolbar.rightButtonOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String editTextInput = cusrom_searchview.getEditTextInput();
                if(TextUtils.isEmpty(editTextInput)){
                    MyToastUtils.show(SearchActivity.this,"请输入查询内容");
                     return;
                }
                Log.i(TAG, "onClick:输入内容 "+editTextInput);
                cusrom_searchview.saveHistory(editTextInput);
                cusrom_searchview.setEditTextInputNull();
                mDialog = (SweetAlertDialog)new DialogFactory(SearchActivity.this).creatLoadingDialog("加载中,垃圾费垃圾费粮食局IE打打打");
                if ( editTextInput .equals("哈哈")){
                    //这里是登录失败,登录对话框弹出失败提示
                    mDialog.setTitleText("登陆失败")
                            .setConfirmText("确定")
                            .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                }
            }
        });
        toolbar.leftButtonOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick: 点击左侧内容");
                finish();
            }
        }); //test_again
    }

    @Override
    protected void backhome() {
        finish();
    }

    @Override
    public void isNetAvaiable(NetUtils.NetType apnType) {
        super.isNetAvaiable(apnType);
        if (apnType == NetUtils.NetType.NONE){
            MyToastUtils.show(this,"没有网络");
        }
    }
}
