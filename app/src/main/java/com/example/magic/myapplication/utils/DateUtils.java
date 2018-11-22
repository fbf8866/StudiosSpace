package com.example.magic.myapplication.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;


/**
 * @author Administrator
 *时间转化工具类
 */
public class DateUtils {
	private static final String TAG = "DateUtils";
	/**
	 * 将传的string时间 转化为时间戳
	 * @param time
	 * @return
	 */
	  public static int getTimeToStamp(String time) {
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
	        Date date = new Date();
	        try {
	            date = sdf.parse(time);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        String tmptime = String.valueOf(date.getTime()).substring(0, 10);
		  //因为查询历史轨迹需要时间戳为int,但是int 最大为2147483647 ,现在将long转后的string 再转为int ,可能存在数值大于最大int,会报错
	        int i = 0;
	        try {
	        	i = Integer.parseInt(tmptime);
	        	return i;
			} catch (Exception e) {
				Log.i(TAG,"---数值已超过"+Integer.MAX_VALUE);
				return Integer.MAX_VALUE;
			}
	    }

	  /**
	   * 将long类型的时间戳转化为时间
	   * @param times
	   * @return
	   */
	  public static String getTime(long times){
		  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
		  String time = sdf.format(times);
		  return time;
	  }
	  /**
	   * 获取当前年份
	   * @return
	   */
	  public static int getYear(){
		  Calendar c = Calendar.getInstance();
		  int year = c.get(Calendar.YEAR);
		  return year;
	  }
	  /**
	   * 获取当前几点
	   * @return
	   */
	  public static int getHour(){
		  Calendar c = Calendar.getInstance();
		  int hour = c.get(Calendar.HOUR_OF_DAY);
		  return hour;
	  }
	  /**
	   * 获取当前月份
	   * @return
	   */
	  public static int getMonth(){
		  Calendar c = Calendar.getInstance();
		  int month = c.get(Calendar.MONTH);
		  return month;
	  }
	  /**
	   * 获取当前日期 几号
	   * @return
	   */
	  public static int getDay(){
		  Calendar c = Calendar.getInstance();
		  int date = c.get(Calendar.DATE);
		  return date;
	  }
	  
	  /**
	   * 获取时间差
	   * @param starTime ��2017-2-21 2-2-2
	   * @param endTime
	   * @return
	   */
	 public static String getTimeDifference(String starTime, String endTime) {  
	        String timeString = "";  
	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	        try {  
	            Date parse = dateFormat.parse(starTime);  
	            Date parse1 = dateFormat.parse(endTime);  
	            long diff = parse1.getTime() - parse.getTime();  
	  
	            long day = diff / (24 * 60 * 60 * 1000);  
	            long hour = (diff / (60 * 60 * 1000) - day * 24);  
	            long min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);  
	            long s = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);  
	            long ms = (diff - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000  
	                    - min * 60 * 1000 - s * 1000);  
	            // System.out.println(day + "��" + hour + "Сʱ" + min + "��" + s +  
	            // "��");  
	            long hour1 = diff / (60 * 60 * 1000);  
	            String hourString = hour1 + "";  
	            long min1 = ((diff / (60 * 1000)) - hour1 * 60);  
	            timeString = hour1 + "Сʱ" + min1 + "��";  
	            // System.out.println(day + "��" + hour + "Сʱ" + min + "��" + s +   "��");  
	  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
	        return timeString;  
	    }
	 
	  /**
	   * 时间的累计
	   * @param seconds  开始计时时间  秒
	   * @return
	   */
	  public static String getTimeShowString(int seconds)
	    {
	        String strShow=new String();
	        int hour = seconds / (60*60);
	        int min = (seconds/60) % 60;
	        int s = seconds % 60;
	        String hourStr = (hour >= 10) ? "" + hour : "0" + hour;
	        String minStr = (min >= 10) ? "" + min : "0" + min;
	        String seondStr = (s >= 10) ? "" + s : "0" + s;
	        strShow = hourStr + ":" + minStr + ":" + seondStr;
	        return strShow;
	    }
	  /**
	   * 判断输入的年份是平年还是闰年(leap)
	   * 闰年2月29天 ,平年28天
	   * 返回true 表示是闰年, false 是平年
	   */
	  public static boolean isLeap(String year){
			  if (null != year) {
				int intYear = Integer.parseInt(year);
				if (intYear % 100 == 0) {
					if (intYear % 400 == 0) {
						return true;
					}
				}else {
					if (intYear % 4 == 0) {
						return true;
					}
				}
		    }
			  return false;
	  }
	  
	  
	  /**
	   * 校验输入的时间
	   * @param input_starttime_year
	   * @param input_starttime_month
	   * @param input_starttime_day
	   * @param input_starttime_hour
	   * @param input_endtime_year
	   * @param input_endtime_month
	   * @param input_endtime_day
	   * @param input_endtime_hour
	   * @param context
	   */
	  public static boolean checkTime(String input_starttime_year,
									  String input_starttime_month, String input_starttime_day,
									  String input_starttime_hour, String input_endtime_year,
									  String input_endtime_month, String input_endtime_day,
									  String input_endtime_hour, Context context) {
		  boolean flag = false;
		  if (TextUtils.isEmpty(input_starttime_year)) {
			  MyToastUtils.show(context, "请输入开始年份");
			  return flag;
		  }
		  if (Integer.parseInt(input_starttime_year) > DateUtils.getYear() ) {
			  MyToastUtils.show(context, "请输入正确的年份");
			  return flag;
		  }
		  if (TextUtils.isEmpty(input_starttime_month)) {
			  MyToastUtils.show(context, "请输入开始月份");
			  return flag;
		  }
		  if (input_starttime_month.equals("0")
				  ||Integer.parseInt(input_starttime_month)>12) {
			  MyToastUtils.show(context, "请输入正确的月份");
			  return flag;
		  }
		  if (Integer.parseInt(input_starttime_year) == DateUtils.getYear()) {
			  if (Integer.parseInt(input_starttime_month)>(DateUtils.getMonth()+1)) {
				  MyToastUtils.show(context, "开始月份有点超前了哦");
				  return flag;
			  }
		  }

		  if (TextUtils.isEmpty(input_starttime_day)) {
			  MyToastUtils.show(context, "请输入开始日期");
			  return flag;
		  }
		  if (input_starttime_day.equals("0") ) {
			  MyToastUtils.show(context, "你的日历中有 0 号?");
			  return flag;
		  }
		  if (Integer.parseInt(input_starttime_year) == DateUtils.getYear()) {
			  if (Integer.parseInt(input_starttime_month) == (DateUtils.getMonth()+1)) {
				  if (Integer.parseInt(input_starttime_day) > DateUtils.getDay()) {
					  MyToastUtils.show(context, "你太超前了吧,骚年~");
					  return flag;
				  }
			  }
		  }
		  if (DateUtils.isLeap(input_starttime_year)) { //是闰年
			  if (input_starttime_month.equals("2") || input_starttime_month.equals("02")) {
				  if (Integer.parseInt(input_starttime_day) > 29) {
					  MyToastUtils.show(context, "骚年,"+input_starttime_year+"年是闰年,"+input_starttime_month+"月最多只有29天哦");
					  return flag;
				  }
			  }
		  }else{ //平年
			  if (input_starttime_month.equals("2") || input_starttime_month.equals("02")) {
				  if (Integer.parseInt(input_starttime_day) > 28) {
					  MyToastUtils.show(context, "骚年,"+input_starttime_year+"年是平年,"+input_starttime_month+"月最多只有28天哦");
					  return flag;
				  }
			  }
		  }
		  if (TextUtils.isEmpty(input_starttime_hour)) {
			  MyToastUtils.show(context, "请输入开始小时");
			  return flag;
		  }
		  if (Integer.parseInt(input_starttime_hour) > 24) {
			  MyToastUtils.show(context, "开始小时不能大于24");
			  return flag;
		  }
		  if (Integer.parseInt(input_starttime_year) == DateUtils.getYear()) {
			  if (Integer.parseInt(input_starttime_month) == DateUtils.getMonth()) {
				  if (Integer.parseInt(input_starttime_day) == DateUtils.getDay()) {
					  if (Integer.parseInt(input_starttime_hour) > DateUtils.getHour() ) {
						  MyToastUtils.show(context, "开始时间 :"+input_starttime_hour+"点还没到哦");
						  return flag;
					  }
				  }
			  }
		  }



		  if (TextUtils.isEmpty(input_endtime_year)) {
			  MyToastUtils.show(context, "请输入结束年份");
			  return flag;
		  }
		  if (Integer.parseInt(input_endtime_year) > DateUtils.getYear() ) {
			  MyToastUtils.show(context, "请输入正确的年份");
			  return flag;
		  }
		  if (Integer.parseInt(input_endtime_year) < Integer.parseInt(input_starttime_year)) {
			  MyToastUtils.show(context, "你还准备活回去吗?");
			  return flag;
		  }
		  if (TextUtils.isEmpty(input_endtime_month)) {
			  MyToastUtils.show(context, "请输入结束月份");
			  return flag;
		  }
		  if (input_endtime_month.equals("0")
				  ||Integer.parseInt(input_endtime_month)>12) {
			  MyToastUtils.show(context, "请输入正确的月份");
			  return flag;
		  }
		  if (Integer.parseInt(input_endtime_year) == DateUtils.getYear()) {
			  if (Integer.parseInt(input_endtime_month)>(DateUtils.getMonth()+1)) {
				  MyToastUtils.show(context, "结束月份有点超前了哦");
				  return flag;
			  }
			  if (Integer.parseInt(input_endtime_month) < Integer.parseInt(input_starttime_month)) {
				  MyToastUtils.show(context, "难道你要准备活回去吗?");
				  return flag;
			  }
		  }
		  if (TextUtils.isEmpty(input_endtime_day)) {
			  MyToastUtils.show(context, "请输入结束日期");
			  return flag;
		  }
		  if (input_endtime_day.equals("0")) {
			  MyToastUtils.show(context, "你的日历中有 0 号?");
			  return flag;
		  }
		  if (Integer.parseInt(input_endtime_year) == DateUtils.getYear()) {
			  if (Integer.parseInt(input_endtime_month) == (DateUtils.getMonth()+1)) {
				  if (Integer.parseInt(input_endtime_day) > DateUtils.getDay()) {
					  MyToastUtils.show(context, "你太超前了吧,骚年~");
					  return flag;
				  }
				  if (Integer.parseInt(input_endtime_day) < Integer.parseInt(input_starttime_day)) {
					  MyToastUtils.show(context, "结束日期不能小于开始日期");
					  return flag;
				  }
			  }
		  }
		  if (DateUtils.isLeap(input_endtime_year)) { //是闰年
			  if (input_endtime_month.equals("2") || input_endtime_month.equals("02")) {
				  if (Integer.parseInt(input_starttime_day) > 29) {
					  MyToastUtils.show(context, "骚年,"+input_endtime_year+"年是闰年,"+input_endtime_month+"月最多只有29天哦");
					  return flag;
				  }
			  }
		  }else{ //平年
			  if (input_endtime_month.equals("2") || input_endtime_month.equals("02")) {
				  if (Integer.parseInt(input_endtime_day) > 28) {
					  MyToastUtils.show(context, "骚年,"+input_endtime_year+"年是平年,"+input_endtime_month+"月最多只有28天哦");
					  return flag;
				  }
			  }
		  }
		  if (TextUtils.isEmpty(input_endtime_hour)) {
			  MyToastUtils.show(context, "请输入结束小时");
			  return flag;
		  }
		  if (Integer.parseInt(input_endtime_hour) > 24) {
			  MyToastUtils.show(context, "结束小时还能大于24吗");
			  return flag;
		  }
		  if (Integer.parseInt(input_endtime_year) == DateUtils.getYear()) {
			  if (Integer.parseInt(input_endtime_month) == DateUtils.getMonth()) {
				  if (Integer.parseInt(input_endtime_day) == DateUtils.getDay()) {
					  if (Integer.parseInt(input_endtime_hour) > DateUtils.getHour() ) {
						  MyToastUtils.show(context, "结束时间 :"+input_endtime_hour+"点还没到哦");
						  return flag;
					  }
				  }
			  }
		  }
		  if (Integer.parseInt(input_endtime_year) == Integer.parseInt(input_starttime_year)) {
			  if (Integer.parseInt(input_endtime_month) == Integer.parseInt(input_starttime_month)) {
				  if (Integer.parseInt(input_endtime_day) == Integer.parseInt(input_starttime_day)) {
					  if (Integer.parseInt(input_endtime_hour) < Integer.parseInt(input_starttime_hour)) {
						  MyToastUtils.show(context, "结束小时不能小于开始小时");
						  return flag;
					  }
				  }
			  }
		  }
		  return true;
	  }
}
