package com.share.multikeys.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/** 
* @author weigen.ye 
* @date 创建时间：2016年10月8日 下午2:16:00 
*
*/
public final class TableNameUtil {
	
	public static String getTableNameSuffix(Date date){
		SimpleDateFormat pattern=new SimpleDateFormat("yyyy_MM");
		String result=pattern.format(date);
		String month=result.substring(5, 7);
		return result.substring(0, 5)+"q"+getQuarter(month);
	}
	
	public static Integer getQuarter(String month){
		Integer result=null;
		if(month.startsWith("0")){
			result=Integer.parseInt(month.substring(1));
		}else{
			result=Integer.parseInt(month);
		}
	    return result/4;
	}
	
	public static void main(String args[]){
		System.out.println(getTableNameSuffix(DateUtil.addDays(new Date(), -90)));
		System.out.println(getTableNameSuffix(DateUtil.addDays(new Date(), 0)));
		System.out.println("2016-10".substring(5, 7));
	}
}
