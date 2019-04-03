package com.gst.mybaseapp.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Huangz on 18/1/16.
 */

public class DateUtils {
    public static String getYearAndMouth() {
        // SimpleDateFormat
        Date d = new Date();
        SimpleDateFormat ss = new SimpleDateFormat("yyMMdd");// 12小时制
        return ss.format(d);
    }

    public static String getHour() {
        return getHour("HHmmss");
    }


    public static String getHour(String formatString) {
        Date date = new Date();
        SimpleDateFormat sdformat = new SimpleDateFormat(formatString);// 24小时制
        String LgTime = sdformat.format(date);
        return LgTime;
    }


    public static String getYearMouthDay(long time) {
        // SimpleDateFormat
        Date d = new Date(time);
        SimpleDateFormat ss = new SimpleDateFormat("yyMMdd");// 12小时制
        return ss.format(d);
    }

    public static String changeDateFormat(String date, String formatSrc, String formatDes) {
        // SimpleDateFormat
        SimpleDateFormat ss1 = new SimpleDateFormat(formatSrc);
        Date d = new Date();
        try {
            d = ss1.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat ss2 = new SimpleDateFormat(formatDes);// 12小时制
        return ss2.format(d);
    }
}

