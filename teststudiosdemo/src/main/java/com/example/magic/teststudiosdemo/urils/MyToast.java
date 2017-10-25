package com.example.magic.teststudiosdemo.urils;

import com.example.magic.teststudiosdemo.application.MyApplication;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class MyToast {
	private static Toast mToast;
	
	public static void show(Context context,String text){
		int height = ((Activity) context).getWindowManager().getDefaultDisplay().getHeight();
//		int height = 640;
		if(null == mToast){
			mToast = Toast.makeText(MyApplication.context, text, Toast.LENGTH_SHORT);
		}
		mToast.setDuration(Toast.LENGTH_SHORT);
		mToast.setText(text);
		mToast.setGravity(Gravity.TOP, 0, 3*height/4);
		mToast.show();
	}
}
