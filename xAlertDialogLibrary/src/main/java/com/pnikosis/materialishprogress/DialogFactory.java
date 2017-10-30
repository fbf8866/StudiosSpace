package com.pnikosis.materialishprogress;


import android.content.Context;
import android.os.CountDownTimer;


import com.xidian.xalertdialog.R;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DialogFactory {
    private SweetAlertDialog pDialog;
    private static int i = -1;
    private Context mContext;
    public DialogFactory(Context context){
        this.mContext = context;
        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
    }


    public  SweetAlertDialog creatLoadingDialog(String message){
        pDialog.setTitleText(message);
        pDialog.setCancelable(false);
        pDialog.show();
        //这里的5指的是转到最后一次后再重新循环的次数
        new CountDownTimer(800 * 7 * 5, 800) {
            public void onTick(long millisUntilFinished) {
                // you can change the progress bar color by ProgressHelper every 800 millis
                i++;
                switch (i){
                    case 0:
                        pDialog.getProgressHelper().setBarColor(mContext.getResources().getColor(R.color.blue_btn_bg_color));
                        break;
                    case 1:
                        pDialog.getProgressHelper().setBarColor(mContext.getResources().getColor(R.color.material_deep_teal_50));
                        break;
                    case 2:
                        pDialog.getProgressHelper().setBarColor(mContext.getResources().getColor(R.color.success_stroke_color));
                        break;
                    case 3:
                        pDialog.getProgressHelper().setBarColor(mContext.getResources().getColor(R.color.material_deep_teal_20));
                        break;
                    case 4:
                        pDialog.getProgressHelper().setBarColor(mContext.getResources().getColor(R.color.material_blue_grey_80));
                        break;
                    case 5:
                        pDialog.getProgressHelper().setBarColor(mContext.getResources().getColor(R.color.warning_stroke_color));
                        break;
                    case 6:
                        pDialog.getProgressHelper().setBarColor(mContext.getResources().getColor(R.color.success_stroke_color));
                        i = -1;
                        break;
                }
            }
            public void onFinish() {
                i = -1;
            }
        }.start();

        return pDialog;
    }
}
