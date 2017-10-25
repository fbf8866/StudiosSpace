package com.example.magic.myapplication.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;

import com.example.magic.myapplication.R;

/**
 * Created by Administrator on 2017/8/8.
 */

public class DialogUtils {
    private DialogUtils(){}
    public static DialogUtils dialogUtils;
    public static DialogUtils getInstance(){
        if (null == dialogUtils){
            synchronized (DialogUtils.class){
                if (null == dialogUtils){
                    dialogUtils = new DialogUtils();
                }
            }
        }
        return dialogUtils;
    }

    /**
     * 创建点击头像 ,显示拍照还是相册选择的dialog
     * @param context
     * @return
     */
    public Dialog creatIconPeopleDialog(Context context){
        Dialog dialog = new Dialog(context, R.style.dialog_style);
        dialog.setContentView(R.layout.dialog_creat_iconpeople);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    /**
     * 点击返回键不使对话框消失
     * @param d
     */
    public static void onKeyDownNotCancleDialog(Dialog d) {
        d.setCanceledOnTouchOutside(false);
        d.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK
                        && event.getRepeatCount() == 0) {

                    return true;
                } else {
                    return false;
                }
            }
        });
        if (null != d && !d.isShowing()){
            d.show();
        }
    }
}
