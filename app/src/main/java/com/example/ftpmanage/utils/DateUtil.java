package com.example.ftpmanage.utils;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 */
public class DateUtil {
    /**
     * 指定的日期增加指定的年数
     *
     * @param date 日期
     * @param num  年数
     * @return
     */
    public static Date AddYear(Date date, int num) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.add(Calendar.YEAR, num);
        return ca.getTime();
    }

    /**
     * 指定的日期增加指定的月数
     *
     * @param date 日期
     * @param num  月数
     * @return
     */
    public static Date AddMonth(Date date, int num) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.add(Calendar.MONTH, num);
        return ca.getTime();
    }

    /**
     * 指定的日期增加指定的天数
     *
     * @param date 日期
     * @param num  天数
     * @return
     */
    public static Date AddDay(Date date, int num) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.add(Calendar.DATE, num);
        return ca.getTime();
    }

    /**
     * 指定的日期增加指定的小时数
     *
     * @param date 日期
     * @param num  小时数
     * @return
     */
    public static Date AddHour(Date date, int num) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.add(Calendar.HOUR, num);
        return ca.getTime();
    }

    /**
     * 指定的日期增加指定的分钟数
     *
     * @param date 日期
     * @param num  分钟数
     * @return
     */
    public static Date AddMinute(Date date, int num) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.add(Calendar.MINUTE, num);
        return ca.getTime();
    }

    /**
     * 指定的日期增加指定的秒数
     *
     * @param date 日期
     * @param num  秒数
     * @return
     */
    public static Date AddSecond(Date date, int num) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.add(Calendar.SECOND, num);
        return ca.getTime();
    }

    /**
     * 根据当前日期返回指定格式日期(yyyy-MM-dd HH:mm:ss)
     *
     * @return
     */
    public static Date getDate() {
        return getDate(getDates(), "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 根据日期字符串返回指定格式日期(yyyy-MM-dd HH:mm:ss)，日期格式转换失败则返回null
     *
     * @param s 日期字符串
     * @return
     */
    public static Date getDate(String s) {
        return getDate(s, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 根据日期字符串返回指定格式日期，日期格式转换失败则返回null
     *
     * @param s      日期字符串
     * @param format 指定日期格式
     * @return
     */
    public static Date getDate(String s, String format) {
        if (s == null) {
            return null;
        }
        SimpleDateFormat f = new SimpleDateFormat(format);
        try {
            return f.parse(s);
        } catch (ParseException e) {
        }
        return null;
    }

    /**
     * 返回当前日期指定格式日期字符串(yyyy-MM-dd HH:mm:ss)
     *
     * @return
     */
    public static String getDates() {
        return getDates(new Date());
    }

    /**
     * 根据指定日期返回指定格式日期字符串(yyyy-MM-dd HH:mm:ss)
     *
     * @param d 日期字符串
     * @return
     */
    public static String getDates(String d) {
        return getDates(d, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 根据指定日期返回指定格式日期字符串
     *
     * @param d      日期字符串
     * @param format 日期格式
     * @return
     */
    public static String getDates(String d, String format) {
        if (d == null) {
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            ParsePosition pos = new ParsePosition(0);
            Date dtime = sdf.parse(d, pos);
            return sdf.format(dtime);
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 根据指定日期返回指定格式日期字符串(yyyy-MM-dd HH:mm:ss)
     *
     * @param d 日期字符串
     * @return
     */
    public static String getDates(Date d) {
        return getDates(d, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 根据指定日期返回指定格式日期字符串
     *
     * @param d      日期字符串
     * @param format 日期格式
     * @return
     */
    public static String getDates(Date d, String format) {
        if (d == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(d);
    }

    /**
     * 返回随机日期
     *
     * @return
     */
    public static Date getRandomDate() {
        Date edate = new Date();
        Date sdate = AddYear(edate, -50);
        return getRandomDate(sdate, edate);
    }

    /**
     * 根据时间范围返回随机日期
     *
     * @param beginDate 起始日期
     * @param endDate   结束日期
     * @return
     */
    public static Date getRandomDate(String beginDate, String endDate) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date start = format.parse(beginDate);
            Date end = format.parse(endDate);
            return getRandomDate(start, end);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据时间范围返回随机日期
     *
     * @param beginDate 起始日期
     * @param endDate   结束日期
     * @return
     */
    public static Date getRandomDate(Date beginDate, Date endDate) {
        try {
            if (beginDate.getTime() >= endDate.getTime()) {
                return null;
            }
            long date = randomTime(beginDate.getTime(), endDate.getTime());
            return new Date(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 递归生成时间戳
     *
     * @param begin 开始时间
     * @param end   结束时间
     * @return
     */
    private static long randomTime(long begin, long end) {
        long rtn = begin + (long) (Math.random() * (end - begin));
        if (rtn == begin || rtn == end) {
            return randomTime(begin, end);
        }
        return rtn;
    }

}
