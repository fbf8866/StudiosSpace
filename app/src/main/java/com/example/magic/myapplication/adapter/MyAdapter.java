package com.example.magic.myapplication.adapter;

import java.util.List;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.magic.myapplication.R;

public class MyAdapter extends FragmentPagerAdapter {
	private List<Fragment> fragmentList;
	private String[] title;
	private Context mContext;
	private int[] IMAGE;

	public MyAdapter(FragmentManager fm, List<Fragment> fragmentList, String[] title, int[] image,Context context) {
		super(fm);
		this.fragmentList = fragmentList;
		this.title = title;
		this.IMAGE = image;
		this.mContext = context;
	}

	@Override
	public Fragment getItem(int posotion) {

        return fragmentList.get(posotion);  
	}

	@Override
	public int getCount() {
		return fragmentList.size();
	}
	
//	@Override
//	public CharSequence getPageTitle(int position) {
//		Drawable drawable = null;
//		switch (position){
//			case 0:
//				drawable = ContextCompat.getDrawable(mContext, android.R.drawable.ic_delete);
//				break;
//			case 1:
//				drawable = ContextCompat.getDrawable(mContext, android.R.drawable.ic_delete);
//				break;
//			case 2:
//				drawable = ContextCompat.getDrawable(mContext, android.R.drawable.ic_delete);
//				break;
//			case 3:
//				drawable = ContextCompat.getDrawable(mContext, android.R.drawable.ic_delete);
//				break;
//			case 4:
//				drawable = ContextCompat.getDrawable(mContext, android.R.drawable.ic_delete);
//				break;
//		}
//		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
//		ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM);
//		SpannableString spannableString = new SpannableString(" "+title[position]);
//		spannableString.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//		return spannableString;
//	}

	public View getTabView(int position) {
		View v = LayoutInflater.from(mContext).inflate(R.layout.tablayout_title,null);
		TextView tv = (TextView) v.findViewById(R.id.tablayout_text);
		tv.setText(title[position]);
		ImageView img = (ImageView) v.findViewById(R.id.tablayout_img);
		img.setImageResource(IMAGE[position]);
		return v;
	}


}
