package com.share.multikeys.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public final class DateUtil{
	
	public static Date addDays(Date date,Integer days) {
		Calendar calendar= Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, days);
		return calendar.getTime();
	}
	
	public static String toDateString(Date date, String pattern){
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat(pattern);
		return simpleDateFormat.format(date);
	}
	
	public static Date stringToDate(String time, String pattern){
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat(pattern);
		Date date=null;
		try {
			date=simpleDateFormat.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	public static void  main(String args[]) {
		System.out.println(toDateString(addDays(new Date(), -30),"yyyy-MM-dd"));
		System.out.println(DateUtil.stringToDate("2016-10-08 17:08:38", "yyyy-MM-dd HH:mm:ss"));
	}
}