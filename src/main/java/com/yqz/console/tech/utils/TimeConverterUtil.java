package com.yqz.console.tech.utils;

import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 时间格式转换工具类(utc时间和本地时间两者的转换)
 *
 * @author guoyangyang
 */
public class TimeConverterUtil {

    private static Logger logger = LoggerFactory.getLogger(TimeConverterUtil.class);

    public static void main(String[] args) {

        String str = "2016-08-09T10:01:54.123Z";
        Preconditions.checkState(utc2Local(str, "yyyy-MM-dd HH:mm:ss.SSS").equals("2016-08-09 18:01:54.123"));

          str = "2016-08-09T10:01:54.123+8:00";
        Preconditions.checkState(utc2Local(str, "yyyy-MM-dd HH:mm:ss.SSS").equals("2016-08-09 10:01:54.123"));

        str = "2016-08-09T10:01:54.123+08:00";
        Preconditions.checkState(utc2Local(str, "yyyy-MM-dd HH:mm:ss.SSS").equals("2016-08-09 10:01:54.123"));

        str = "2016-08-09T10:01:54.123-8:00";
        Preconditions.checkState(utc2Local(str, "yyyy-MM-dd HH:mm:ss.SSS").equals("2016-08-10 02:01:54.123"));

        str = "2016-08-09T10:01:54.123-08:00";
        Preconditions.checkState(utc2Local(str, "yyyy-MM-dd HH:mm:ss.SSS").equals("2016-08-10 02:01:54.123"));

          str = "2016-08-09 10:01:54.123";
        Preconditions.checkState(utc2Local(str, "yyyy-MM-dd HH:mm:ss.SSS").equals("2016-08-09 10:01:54.123"));
        
        str = "2016-08-09T10:01:54.123";
         System.out.println(utc2Local(str, "yyyy-MM-dd HH:mm:ss.SSS"));
    }

    /**
     * 函数功能描述:UTC时间转本地时间格式
     *
     * @param utcTime          UTC时间
     * @param localTimePattern 本地时间格式(要转换的本地时间格式)
     * @return 本地时间格式的时间
     */
    public static String utc2Local(String utcTime, String localTimePattern) {
        boolean isUTC = false;
        String utcTimePattern = "yyyy-MM-dd";
        String subTime = utcTime.substring(10);//UTC时间格式以 yyyy-MM-dd 开头,将utc时间的前10位截取掉,之后是含有多时区时间格式信息的数据

        //处理当后缀为:+8:00时,转换为:+08:00 或 -8:00转换为-08:00
        if (subTime.indexOf("+") != -1) {
            subTime = changeUtcSuffix(subTime, "+");
        } else if (subTime.indexOf("-") != -1) {
            subTime = changeUtcSuffix(subTime, "-");
        }
        utcTime = utcTime.substring(0, 10) + subTime;

        //依据传入函数的utc时间,得到对应的utc时间格式
        //步骤一:处理 T
        if (utcTime.indexOf("T") != -1) {
            utcTimePattern = utcTimePattern + "'T'";
        }

        //步骤二:处理毫秒SSS
        if (utcTime.indexOf(".") != -1) {
            utcTimePattern = utcTimePattern + "HH:mm:ss.SSS";
        } else {
            utcTimePattern = utcTimePattern + "HH:mm:ss";
        }

        //步骤三:处理时区问题
        if (subTime.indexOf("+") != -1 || subTime.indexOf("-") != -1) {
            utcTimePattern = utcTimePattern + "XXX";
            isUTC = true;
        } else if (subTime.indexOf("Z") != -1) {
            utcTimePattern = utcTimePattern + "'Z'";
            isUTC = true;
        }


        SimpleDateFormat utcFormater = new SimpleDateFormat(utcTimePattern);
        if (isUTC)
            utcFormater.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date gpsUtcDate = null;
        try {
            gpsUtcDate = utcFormater.parse(utcTime);
        } catch (Exception e) {
            logger.error("utcTime converter localTime failed!!!", e);
            return utcTime;
        }
        SimpleDateFormat localFormater = new SimpleDateFormat(localTimePattern);
        localFormater.setTimeZone(TimeZone.getDefault());
        String localTime = localFormater.format(gpsUtcDate.getTime());
        return localTime;
    }

    /**
     * 函数功能描述:修改时间格式后缀
     * 函数使用场景:处理当后缀为:+8:00时,转换为:+08:00 或 -8:00转换为-08:00
     *
     * @param subTime
     * @param sign
     * @return
     */
    private static String changeUtcSuffix(String subTime, String sign) {
        String timeSuffix = null;
        String[] splitTimeArrayOne = subTime.split("[" + sign + "]");
        String[] splitTimeArrayTwo = splitTimeArrayOne[1].split(":");
        if (splitTimeArrayTwo[0].length() < 2) {
            timeSuffix = sign + "0" + splitTimeArrayTwo[0] + ":" + splitTimeArrayTwo[1];
            subTime = splitTimeArrayOne[0] + timeSuffix;
            return subTime;
        }
        return subTime;
    }

    /**
     * 函数功能描述:获取本地时区的表示(比如:第八区-->+08:00)
     *
     * @return
     */
    public static String getTimeZoneByNumExpress() {
        Calendar cal = Calendar.getInstance();
        TimeZone timeZone = cal.getTimeZone();
        int rawOffset = timeZone.getRawOffset();
        int timeZoneByNumExpress = rawOffset / 3600 / 1000;
        String timeZoneByNumExpressStr = "";
        if (timeZoneByNumExpress > 0 && timeZoneByNumExpress < 10) {
            timeZoneByNumExpressStr = "+" + "0" + timeZoneByNumExpress + ":" + "00";
        } else if (timeZoneByNumExpress >= 10) {
            timeZoneByNumExpressStr = "+" + timeZoneByNumExpress + ":" + "00";
        } else if (timeZoneByNumExpress > -10 && timeZoneByNumExpress < 0) {
            timeZoneByNumExpress = Math.abs(timeZoneByNumExpress);
            timeZoneByNumExpressStr = "-" + "0" + timeZoneByNumExpress + ":" + "00";
        } else if (timeZoneByNumExpress <= -10) {
            timeZoneByNumExpress = Math.abs(timeZoneByNumExpress);
            timeZoneByNumExpressStr = "-" + timeZoneByNumExpress + ":" + "00";
        } else {
            timeZoneByNumExpressStr = "Z";
        }
        return timeZoneByNumExpressStr;
    }

}