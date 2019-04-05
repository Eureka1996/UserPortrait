package com.wufuqiang.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @ author wufuqiang
 * @ date 2019/4/4/004 - 15:06
 **/
public class DateUtils {

//    根据年龄，判断年代
    public static String getYearBaseByAge(String age){
        Calendar calendar = Calendar.getInstance() ;
        calendar.setTime(new Date());
        calendar.add(Calendar.YEAR,-Integer.valueOf(age)) ;
        Date newDate = calendar.getTime() ;
        DateFormat dateFormat = new SimpleDateFormat("yyyy") ;
        String newDateString = dateFormat.format(newDate) ;
        Integer newDateInteger = Integer.valueOf(newDateString) ;

        Integer yearBase = (newDateInteger%100)/10*10 ;

        if(yearBase == 0){
            return "00后" ;
        }

        return yearBase.toString() + "后";
    }

    public static int getDaysBetweenbyStartAndend(String starttime,String endTime,String dateFormatstring) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat(dateFormatstring);
        Date start = dateFormat.parse(starttime);
        Date end = dateFormat.parse(endTime);
        Calendar startcalendar = Calendar.getInstance();
        Calendar endcalendar = Calendar.getInstance();
        startcalendar.setTime(start);
        endcalendar.setTime(end);
        int days = 0;
        while(startcalendar.before(endcalendar)){
            startcalendar.add(Calendar.DAY_OF_YEAR,1);
            days += 1;
        }
        return days;
    }

    public static void main(String[] args){
        System.out.println(getYearBaseByAge("78")) ;
    }
}
