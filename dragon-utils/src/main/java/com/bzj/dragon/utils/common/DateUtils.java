package com.bzj.dragon.utils.common;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author baizhijian
 * @since 2015/8/22
 */
public class DateUtils {
    public static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormat.forPattern("yyyy-MM-dd");
    public static final FastDateFormat JAVA_DATE_TIME_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
	public static final FastDateFormat JAVA_DATE_TIME_FORMAT2 = FastDateFormat.getInstance("yyyyMMddHHmmss");
    public static final FastDateFormat JAVA_DATE_HM_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd HH:mm");
    public static final FastDateFormat JAVA_DATE_HM_FORMAT2 = FastDateFormat.getInstance("yyyy.MM.dd HH:mm");
    public static final FastDateFormat JAVA_HM_FORMAT = FastDateFormat.getInstance("HH:mm");
    public static final FastDateFormat JAVA_HMS_FORMAT = FastDateFormat.getInstance("HH:mm:ss");
    public static final FastDateFormat JAVA_DATE_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd");
    public static final FastDateFormat JAVA_DATE_FORMAT2 = FastDateFormat.getInstance("yyyy.MM.dd");
    public static final FastDateFormat JAVA_DATE8_FORMAT = FastDateFormat.getInstance("yyyyMMdd");
    public static final FastDateFormat JAVA_DATEMMDD_FORMAT = FastDateFormat.getInstance("MM-dd");
    public static final FastDateFormat JAVA_DATEMMDD2_FORMAT = FastDateFormat.getInstance("MM.dd");
    public static final FastDateFormat JAVA_MONTH_FORMAT = FastDateFormat.getInstance("yyyyMM");
    public static final long MILLIS_IN_ONE_MINUTE = 1000 * 60;
    public static final long MILLIS_IN_ONE_HOUR = MILLIS_IN_ONE_MINUTE * 60;
    public static final long MILLIS_IN_ONE_DAY = MILLIS_IN_ONE_HOUR * 24;
    public static final long MILLIS_IN_THIRTY_DAY = MILLIS_IN_ONE_DAY * 30;


	/**
	 * 根据给定的日期串 转化成日期
	 */
	public static Date parseDate(String date, String format) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(format);
			Date ret = formatter.parse(date);
			return ret;
		} catch (Exception ex) {
			return new Date();
		}
	}

    /**
     * 当前时区与 GMT/UTC 的差别
     */
    private static final int TIME_OFFSET = Calendar.getInstance().getTimeZone().getRawOffset();

    public static final Pattern DATE_HMS_TIME_PATTERN =
            Pattern.compile("(\\d{4})-(\\d{1,2})-(\\d{1,2})\\s(\\d{1,2}):?(\\d{1,2})?:?(\\d{1,2})?");
    public static final Pattern DATE_TIME_PATTERN =
            // YY或YYYY年的匹配 ((?:19|20)?[03-9][0-9])  年份的第三位数不能是1或2，故2010, 1920之类的年份不合法，因为默认我们要抓的东西都具有时效性
            // YYYY年的匹配 ((?:19|20)[03-9][0-9])  年份的第三位数不能是1或2，故2010, 1920之类的年份不合法，因为默认我们要抓的东西都具有时效性
            // M或MM月份的匹配 (0[1-9]|1[0-2]|[1-9])
            // MM月份的匹配    (0[1-9]|1[0-2])
            // D或DD日的匹配   (0[1-9]|[1-3][0-9]|[1-9])
            // DD日的匹配      (0[1-9]|[1-3][0-9])
            Pattern.compile(
                    // YYYY年MM月DD日 HH:MM:SS(没有年，或者YY年也可)
                    // YYYY年MM月DD日 HH:MM
                    // YYYY-MM-DD HH:MM:SS(没有年，或者YY年也可)
                    // YYYY-MM-DD HH:MM
                    "[^0-9]*(?:(?:(?:((?:19|20)?[03-9][0-9])[\\-/年])?(0[1-9]|1[0-2]|[1-9])[\\-/月])?(0[1-9]|[1-3][0-9]|[1-9])(?:日\\s*|\\s+))?([0-2]?[0-9]):([0-5][0-9])(?::([0-5][0-9])(?:\\.[0-9]{1,3})?)?[^0-9]*" +
                            "|" +
                            // YYYY年MM月DD日(没有年，或者YY年也可)，没有年和月也可以，但如果连时间也没有的话，日期后面必须有“日”字
                            // YYYY-MM-DD
                            // DD日
                            "[^0-9]*(?:(?:((?:19|20)?[03-9][0-9])[\\-/年])?(0[1-9]|1[0-2]|[1-9])[\\-/月])?(0[1-9]|[1-3][0-9]|[1-9])(?:[日]?(?:\\s*([0-2]?[0-9]):([0-5][0-9])(?::([0-5][0-9])(?:\\.[0-9]{1,3})?)?)|日)[^0-9]*" +
                            "|" +
                            // "[^0-9]*(?:((?:19|20)?[03-9][0-9])[\\s]?)?([01]?[0-9])[\\s]?([0123]?[0-9])(?:\\s*([0-2]?[0-9]):([0-5][0-9])(?::([0-5][0-9]))?)?[^0-9]*" +
                            // YYYYMMDD HH:mm:ss 不能没有年份，但月份和日期可以没有开始的0
                            "[^0-9]*((?:19|20)?[03-9][0-9])(0[1-9]|1[0-2]|[1-9])(0[1-9]|[1-3][0-9]|[1-9])(?:\\s*([0-2]?[0-9]):([0-5][0-9])(?::([0-5][0-9])(?:\\.[0-9]{1,3})?)?)?[^0-9]*" +
                            // "[^0-9]*([0-9]{2}|[0-9]{4})\\-([0-9]{1,2})\\-([0-9]{1,2})[^0-9]*" +
                            "|" +
                            // YYYYMMDD HH:mm:ss 可以没有年份，或者是YY的年份
                            "[^0-9]*((?:19|20)?[03-9][0-9])?(0[1-9]|1[0-2])(0[1-9]|[1-3][0-9])(?:\\s*([0-2]?[0-9]):([0-5][0-9])(?::([0-5][0-9])(?:\\.[0-9]{1,3})?)?)?[^0-9]*" +
                            "|" +
                            // YYYY-MM-DD (没有YYYY也可)
                            "[^0-9]*(?:((?:19|20)?[03-9][0-9])[\\-/])?(0[1-9]|1[0-2]|[1-9])[\\-/](0[1-9]|[1-3][0-9]|[1-9])(?:\\s*([0-2]?[0-9]):([0-5][0-9])(?::([0-5][0-9])(?:\\.[0-9]{1,3})?)?)?[^0-9]*" +
                            "|" +
                            // MM-DD-YYYY HH:mm:ss（没有ss也可）
                            "[^0-9]*(0[1-9]|1[0-2]|[1-9])[\\-/](0[1-9]|[1-3][0-9]|[1-9])[\\-/]((?:19|20)[03-9][0-9])(?:\\s*([0-2]?[0-9]):([0-5][0-9])(?::([0-5][0-9])(?:\\.[0-9]{1,3})?)?)[^0-9]*" +
                            "|" +
                            // DD-MMM-YYYY:HH:mm:ss
                            // DD/MMM/YYYY HH:mm:ss
                            // DD/MMM/YYYYTHH:mm:ss
                            "[^0-9]*(0[1-9]|[1-3][0-9]|[1-9])[\\-/](Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)[\\-/]((?:19|20)[03-9][0-9])[\\s:](?:\\s*([0-2]?[0-9]):([0-5][0-9])(?::([0-5][0-9])(?:\\.[0-9]{1,3})?)?)[^0-9]*" +
                            "|" +
                            // MM-DD(YYYY)
                            "[^0-9]*(0[1-9]|1[0-2]|[1-9])[\\-/](0[1-9]|[1-3][0-9]|[1-9])\\(((?:19|20)[03-9][0-9])\\)[^0-9]*"
                    ,
                    Pattern.DOTALL | Pattern.CASE_INSENSITIVE);

    private static Map<String, Integer> monthNameMap = new HashMap<String, Integer>();

    static {
        monthNameMap.put("Jan", 1);
        monthNameMap.put("Feb", 2);
        monthNameMap.put("Mar", 3);
        monthNameMap.put("Apr", 4);
        monthNameMap.put("May", 5);
        monthNameMap.put("Jun", 6);
        monthNameMap.put("Jul", 7);
        monthNameMap.put("Aug", 8);
        monthNameMap.put("Sep", 9);
        monthNameMap.put("Oct", 10);
        monthNameMap.put("Nov", 11);
        monthNameMap.put("Dec", 12);
    }

	/**
	 * 取当天零点零分零秒
	 */
	public static Date getTodayStart() {
		Calendar calendar = Calendar.getInstance();
		//如果没有这种设定的话回去系统的当期的时间
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return new Date(calendar.getTimeInMillis());
	}

	/**
	 * 取当天23点59分59秒
	 */
	public static Date getTodayEnd() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		return new Date(calendar.getTimeInMillis());
	}

    /**
     *
     * @param date
     * @return
     */
    public static String toGMTString(Date date) {
        int offset = TimeZone.getDefault().getRawOffset();
        long time = date.getTime() - offset;
        DateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.US);
        // Fri, 30 Oct 1998 14:19:41 GMT
        // Sat, 06 May 1995 12:00:00 GMT
        // Wed, 09 Feb 1994 22:23:32 GMT
        return dateFormat.format(new Date(time)) + " GMT";
    }

    public static String toGMTString(long timeMilli) {
        int offset = TimeZone.getDefault().getRawOffset();
        long time = timeMilli - offset;
        DateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.US);
        // Fri, 30 Oct 1998 14:19:41 GMT
        // Sat, 06 May 1995 12:00:00 GMT
        // Wed, 09 Feb 1994 22:23:32 GMT
        return dateFormat.format(new Date(time)) + " GMT";
    }

    /**
     * 确保返回不为空的 date ，如果传入date为空，则返回当前时间
     *
     * @param date
     * @return
     */
    public static Date notNullNow(Date date) {
        if (date == null) {
            return new Date();
        }
        return date;
    }

    /**
     * 根据年月返回日期
     *
     * @param year  年，必须为四位数
     * @param month 月，从1~12
     * @return
     */
    public static Date toDate(int year, int month) {
        return new DateTime(year, month, 1, 0, 0, 0, 0).toDate();
    }

    /**
     * 根据年月日返回日期
     *
     * @param year  年，必须为四位数
     * @param month 月，从1~12
     * @param day   日，从1~31
     * @return
     */
    public static Date toDate(int year, int month, int day) {
        return new DateTime(year, month, day, 0, 0, 0, 0).toDate();
    }

    /**
     * 根据年月日小时返回日期
     *
     * @param year  年，必须为四位数
     * @param month 月，从1~12
     * @param day   日，从1~31
     * @param hour  日，从0~23
     * @return
     */
    public static Date toDate(int year, int month, int day, int hour) {
        return new DateTime(year, month, day, hour, 0, 0, 0).toDate();
    }

    /**
     * 根据年月日小时分返回日期
     *
     * @param year  年，必须为四位数
     * @param month 月，从1~12
     * @param day   日，从1~31
     * @param hour  日，从0~23
     * @return
     */
    public static Date toDate(int year, int month, int day, int hour, int minute) {
        return new DateTime(year, month, day, hour, minute, 0, 0).toDate();
    }

    /**
     * 在给定日期上加上或减去若干天
     *
     * @param date  给定日期，如果为null则为当前日期
     * @param years 加上的月数（如果为负数，则为减去的月数）
     * @return
     */
    public static Date plusYears(Date date, int years) {
        if (date == null) {
            date = new Date();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, years);

        return cal.getTime();
    }

    /**
     * 在给定日期上加上或减去若干天
     *
     * @param date   给定日期，如果为null则为当前日期
     * @param months 加上的月数（如果为负数，则为减去的月数）
     * @return
     */
    public static Date plusMonths(Date date, int months) {
        if (date == null) {
            date = new Date();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, months);
        return cal.getTime();

    }

    /**
     * 在给定日期上加上或减去若干天
     *
     * @param date 给定日期，如果为null则为当前日期
     * @param days 加上的天数（如果为负数，则为减去的天数）
     * @return
     */
    public static Date plusDays(Date date, int days) {
        if (date == null) {
            date = new Date();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, days);
        return cal.getTime();

    }

    /**
     * 在给定日期上加上或减去小时
     *
     * @param date  给定日期，如果为null则为当前日期
     * @param hours 加上的小时数（如果为负数，则为减去的小时数）
     * @return
     */
    public static Date plusHours(Date date, int hours) {
        if (date == null) {
            date = new Date();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR_OF_DAY, hours);
        return cal.getTime();

    }

    /**
     * 在给定日期上加上或减去分钟
     *
     * @param date    给定日期，如果为null则为当前日期
     * @param minutes 加上的分钟数（如果为负数，则为减去的分钟数）
     * @return
     */
    public static Date plusMinutes(Date date, int minutes) {
        if (date == null) {
            date = new Date();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, minutes);
        return cal.getTime();

    }

    /**
     * 在给定日期上加上或减去若干秒
     *
     * @param date    给定日期，如果为null则为当前日期
     * @param seconds 加上的秒数（如果为负数，则为减去的秒数）
     * @return
     */
    public static Date plusSeconds(Date date, int seconds) {
        if (date == null) {
            date = new Date();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.SECOND, seconds);
        return cal.getTime();
    }

    /**
     * 在给定日期上加上或减去若干毫秒
     *
     * @param date   给定日期，如果为null则为当前日期
     * @param millis 加上的毫秒数（如果为负数，则为减去的毫秒数）
     * @return
     */
    public static Date plusMillis(Date date, int millis) {
        if (date == null) {
            date = new Date();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MILLISECOND, millis);
        return cal.getTime();

    }

    public static Date plusTime(Date date, int days, int hours, int minutes) {
        if (date == null) {
            date = new Date();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, days);
        cal.add(Calendar.HOUR_OF_DAY, hours);
        cal.add(Calendar.MINUTE, minutes);
        return cal.getTime();
    }

    public static String getDateDescription(Date date) {
        if (date == null) {
            return "";
        }
        return getDateDescription(date.getTime());
    }

    public static String getDateDescription(long time) {
        long beforeNow = System.currentTimeMillis() - time;
        return getTimeDescriptionBy(beforeNow);
    }

    public static String getDateTimeDescription(Date date) {
        return getDateTimeDescription(date, MILLIS_IN_THIRTY_DAY, JAVA_DATE_TIME_FORMAT);
    }

    public static String getDateTimeDescription(long time) {
        return getDateTimeDescription(time, MILLIS_IN_THIRTY_DAY, JAVA_DATE_TIME_FORMAT);
    }

    public static String getDateTime2Des(Date date) {
        return getDateTimeDescription(date, MILLIS_IN_ONE_DAY, JAVA_DATE_HM_FORMAT);
    }

    public static String getDateTime2Des(long time) {
        return getDateTimeDescription(time, MILLIS_IN_ONE_DAY, JAVA_DATE_HM_FORMAT);
    }

    /**
     * @param date
     * @param formatThreshold 当据现在的时间间隔大于等于该值时,则直接format date
     * @return
     * @Param format
     */
    private static String getDateTimeDescription(Date date, long formatThreshold, FastDateFormat format) {
        if (date == null) {
            return "";
        }
        long beforeNow = System.currentTimeMillis() - date.getTime();
        if (beforeNow < formatThreshold) {
            return getTimeDescriptionBy(beforeNow);
        } else {
            return format.format(date);
        }
    }

	/**
	 * yyyyMMddHHmmss格式的日期
	 * @return
	 */
	public static String getDateTimeFamet(Date date){
		return JAVA_DATE_TIME_FORMAT2.format(date);
	}

	/**
	 * yyyyMMddHHmmss格式的日期n分钟后时间
	 * @return
	 */
	public static String getHalfLastDateTime(Date date,int time){
		return JAVA_DATE_TIME_FORMAT2.format(new Date(date.getTime()+(1000*60*time)));
	}

    /**
     * @param time
     * @param formatThreshold 当据现在的时间间隔大于等于该值时,则直接format date
     * @return
     * @Param format
     */
    private static String getDateTimeDescription(long time, long formatThreshold, FastDateFormat format) {
        long beforeNow = System.currentTimeMillis() - time;
        if (beforeNow < formatThreshold) {
            return getTimeDescriptionBy(beforeNow);
        } else {
            return format.format(new Date(time));
        }
    }

    /**
     * 原实现做的乘除法过多,影响效率,且几个方法的代码重复
     *
     * @param intervalMillis 据现在的时间间隔
     * @return
     */
    private static String getTimeDescriptionBy(long intervalMillis) {
        if (intervalMillis < MILLIS_IN_ONE_MINUTE) {
            return intervalMillis / 1000 + "秒前";
        } else if (intervalMillis < MILLIS_IN_ONE_HOUR) {
            return intervalMillis / MILLIS_IN_ONE_MINUTE + "分钟前";
        } else if (intervalMillis < MILLIS_IN_ONE_DAY) {
            return intervalMillis / MILLIS_IN_ONE_HOUR + "小时前";
        } else {
            return intervalMillis / MILLIS_IN_ONE_DAY + "天前";
        }
    }

    public static String getIntervalDescOfDate(Date dateStart, Date dateEnd) {
        String intervalDesc = "";
        if (dateEnd == null || dateStart == null) {
            return intervalDesc;
        }
        long intervalTime = dateEnd.getTime() - dateStart.getTime();
        int intervalDays = (int) (intervalTime / MILLIS_IN_ONE_DAY);
        if (intervalDays > 0) {
            intervalDesc += intervalDays + "天";
            intervalTime = intervalTime % MILLIS_IN_ONE_DAY;
        }
        int intervalHours = (int) (intervalTime / MILLIS_IN_ONE_HOUR);
        if (intervalHours > 0 && intervalHours < 24) {
            intervalDesc += intervalHours + "小时";
            intervalTime = intervalTime % MILLIS_IN_ONE_HOUR;
        }
        int intervalMinutes = (int) (intervalTime / MILLIS_IN_ONE_MINUTE);
        if (intervalMinutes > 0 && intervalMinutes < 60) {
            intervalDesc += intervalMinutes + "分钟";
            intervalTime = intervalTime % MILLIS_IN_ONE_MINUTE;
        }
        int intervalSeconds = (int) (intervalTime / 1000);
        if (intervalSeconds > 0 && intervalSeconds < 60) {
            intervalDesc += intervalSeconds + "秒";
        }
        return intervalDesc;
    }

    public static String transformDateDescription(Date date, DateFormat format) {
        String description = "";
        if (date == null) {
            return description;
        }
        Date now = new Date();
        long beforeNow = now.getTime() - date.getTime();
        int beforeNowDays = (int) (beforeNow / MILLIS_IN_ONE_DAY);
        switch (beforeNowDays) {
            case 0:
                description = "今天";
                break;
            case 1:
                description = "昨天";
                break;
            case 2:
                description = "前天";
                break;
            default:
                description = format.format(date);
        }
        return description;
    }

    /**
     * @param dateTime 要转换的DateTime类
     * @return YYYY-MM-DD HH:MM:SS 格式的时间字符串
     */
    public static String toDateTimeString(DateTime dateTime) {
        String date = null;
        if (dateTime != null) {
            date = dateTime.toString(DATE_TIME_FORMAT);
        }
        return date;
    }

    /**
     * @param dateTime 要转换的DateTime类
     * @return YYYY-MM-DD 格式的日期字符串
     */
    public static String toDateString(DateTime dateTime) {
        String date = null;
        if (dateTime != null) {
            date = dateTime.toString(DATE_FORMAT);
        }
        return date;
    }

    /**
     * @param dateTime 要转换的DateTime类
     * @return YYYY-MM-DD HH:MM:SS 格式的时间字符串
     */
    public static String toDateTimeString(Date dateTime) {
        String date = null;
        if (dateTime != null) {
            date = JAVA_DATE_TIME_FORMAT.format(dateTime);
        }
        return date;
    }

    /**
     * @param dateTime 要转换的DateTime类
     * @return YYYY-MM-DD HH:MM 格式的时间字符串
     */
    public static String toDateTimeHMString(Date dateTime) {
        String date = null;
        if (dateTime != null) {
            date = JAVA_DATE_HM_FORMAT.format(dateTime);
        }
        return date;
    }

    /**
     * @param dateTime 要转换的DateTime类
     * @return YYYY.MM.DD HH:MM 格式的时间字符串
     */
    public static String toDateTimeHMString2(Date dateTime) {
        String date = null;
        if (dateTime != null) {
            date = JAVA_DATE_HM_FORMAT2.format(dateTime);
        }
        return date;
    }

    /**
     * @param dateTime 要转换的DateTime类
     * @return HH:MM 格式的时间字符串
     */
    public static String toTimeHMString(Date dateTime) {
        String date = null;
        if (dateTime != null) {
            date = JAVA_HM_FORMAT.format(dateTime);
        }
        return date;
    }

    /**
     * @param dateTime 要转换的DateTime类
     * @return HH:MM:SS 格式的时间字符串
     */
    public static String toTimeHMSString(Date dateTime) {
        String date = null;
        if (dateTime != null) {
            date = JAVA_HMS_FORMAT.format(dateTime);
        }
        return date;
    }

    /**
     * @param dateTime 要转换的DateTime类
     * @return YYYY-MM-DD 格式的日期字符串
     */
    public static String toDateString(Date dateTime) {
        String date = null;
        if (dateTime != null) {
            date = JAVA_DATE_FORMAT.format(dateTime);
        }
        return date;
    }

    /**
     * @param dateTime 要转换的DateTime类
     * @return YYYY.MM.DD 格式的日期字符串
     */
    public static String toDateString2(Date dateTime) {
        String date = null;
        if (dateTime != null) {
            date = JAVA_DATE_FORMAT2.format(dateTime);
        }
        return date;
    }

    /**
     * @param dateTime 要转换的DateTime类
     * @return YYYYMMDD 格式的日期字符串
     */
    public static String toDate8CharString(Date dateTime) {
        String date = null;
        if (dateTime != null) {
            date = JAVA_DATE8_FORMAT.format(dateTime);
        }
        return date;
    }

    /**
     * @param dateTime 要转换的DateTime类
     * @return YYYYMM 格式的日期字符串
     */
    public static String toMonthString(Date dateTime) {
        String date = null;
        if (dateTime != null) {
            date = JAVA_MONTH_FORMAT.format(dateTime);
        }
        return date;
    }

    /**
     * 将日期转换成 1970-1-1 以来的天数
     *
     * @param date
     * @return
     */
    public static short toShort(Date date) {
        return (short) ((date.getTime() + TIME_OFFSET) / MILLIS_IN_ONE_DAY);
    }

    /**
     * 将 1970-1-1 以来的天数 转换成日期
     *
     * @param dateShort
     * @return
     */
    public static Date toDate(int dateShort) {
        return new Date(dateShort * MILLIS_IN_ONE_DAY - TIME_OFFSET);
    }

    /**
     * 将一个日期的 时分秒毫秒都清零
     *
     * @param date
     * @return
     */
    public static Date truncateDate(Date date) {
        if (date == null) {
            date = new Date();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        truncateCalendar(c);
        return c.getTime();
    }

    /**
     * 将一个Calendar的 时分秒毫秒都清零
     *
     * @param cal
     * @return
     */
    public static Calendar truncateCalendar(Calendar cal) {
        if (cal == null) {
            cal = Calendar.getInstance();
        }
        cal.clear(Calendar.AM_PM);
        cal.clear(Calendar.HOUR);
        cal.clear(Calendar.HOUR_OF_DAY);
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        return cal;
    }

    /**
     * 将一个日期的 分秒毫秒都清零
     *
     * @param date
     * @return
     */
    public static Date truncateDateH(Date date) {
        if (date == null) {
            date = new Date();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.clear(Calendar.MINUTE);
        c.clear(Calendar.SECOND);
        c.clear(Calendar.MILLISECOND);
        return c.getTime();
    }

    /**
     * 将一个日期的 秒毫秒都清零
     *
     * @param date
     * @return
     */
    public static Date truncateDateHM(Date date) {
        if (date == null) {
            date = new Date();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.clear(Calendar.SECOND);
        c.clear(Calendar.MILLISECOND);
        return c.getTime();
    }

    /**
     * 将一个日期的 时分秒毫秒都清零，天数变为该月第一天
     *
     * @param date
     * @return
     */
    public static Date truncateDateD(Date date) {
        if (date == null) {
            date = new Date();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
//        c.set(Calendar.DAY_OF_MONTH, 1);
        c.clear(Calendar.AM_PM);
        c.clear(Calendar.HOUR);
        c.clear(Calendar.HOUR_OF_DAY);
        c.clear(Calendar.MINUTE);
        c.clear(Calendar.SECOND);
        c.clear(Calendar.MILLISECOND);
        return c.getTime();
    }

    private static DateTime setDateTimePart(DateTime src, int partIndex, int value) {
        switch (partIndex) {
            case 0:
                return src.withYear(value);
            case 1:
                return src.withMonthOfYear(value);
            case 2:
                return src.withDayOfMonth(value);
            case 3:
                return src.withHourOfDay(value);
            case 4:
                return src.withMinuteOfHour(value);
            case 5:
                return src.withSecondOfMinute(value);
            default:
                return src;
        }
    }

    /**
     * 解析形如 YYYY-MM-DD 以及 YYYYMMDD 的纯日期字符串, 或者 YYYY-MM-DD hh:mm:ss的时间格式
     *
     * @param dateString 日期字符串，不能为空
     * @return
     */
    public static Date parseSimpleDate(String dateString) {
        if (dateString == null) {
            return null;
        }
        Date date;
        if (dateString.contains(" ") && dateString.contains(":")) {
            try {
                return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(dateString);
            } catch (ParseException e) {
                return null;
            }
        }
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
        } catch (Exception e) {
            try {
                date = new SimpleDateFormat("yyyyMMdd").parse(dateString);
            } catch (Exception e1) {
                return null;
            }
        }
        return date;
    }

    public static Date parseHMSDate(String dateString) {
        Matcher matcher = DATE_HMS_TIME_PATTERN.matcher(dateString);
        if (matcher.matches()) {
            int year = Integer.parseInt(matcher.group(1));
            int month = Integer.parseInt(matcher.group(2));
            int day = Integer.parseInt(matcher.group(3));
            int hour = Integer.parseInt(matcher.group(4));
            int minute = 0;
            if (matcher.group(5) != null) {
                minute = Integer.parseInt(matcher.group(5));
            }
            int second = 0;
            if (matcher.group(6) != null) {
                second = Integer.parseInt(matcher.group(6));
            }
            DateTime dt = new DateTime(year, month, day, hour, minute, second, 0);
            return dt.toDate();
        }
        return null;
    }

    /**
     * 解析形如 YYYY-MM 以及 YYYYMM 的纯日期字符串
     *
     * @param dateString 日期字符串，不能为空
     * @return
     */
    public static Date parseSimpleMonth(String dateString) {
        if (dateString == null) {
            return null;
        }
        Date date;
        try {
            date = new SimpleDateFormat("yyyyMM").parse(dateString);
        } catch (Exception e) {
            try {
                date = new SimpleDateFormat("yyyy-MM").parse(dateString);
            } catch (Exception e1) {
                return null;
            }
        }
        return date;
    }

    /**
     * 根据日期返回所属星座的 index
     * 0  - 白羊座
     * 1  - 金牛座
     * 2  - 双子座
     * 3  - 巨蟹座
     * 4  - 狮子座
     * 5  - 处女座
     * 6  - 天秤座
     * 7  - 天蝎座
     * 8  - 射手座
     * 9  - 摩羯座
     * 10 - 水瓶座
     * 11 - 双鱼座
     * -1 - 不知道啥星座（不应当发生）
     *
     * @param date
     * @return
     */
    public static int getAstrologyByDate(Date date) {
        DateTime dateTime = new DateTime(date);
        int month = dateTime.getMonthOfYear();
        int day = dateTime.getDayOfMonth();
        if (month == 3 && day >= 21 || month == 4 && day <= 20) {
            return 0;
        } else if (month == 4 && day >= 21 || month == 5 && day <= 21) {
            return 1;
        } else if (month == 5 && day >= 22 || month == 6 && day <= 21) {
            return 2;
        } else if (month == 6 && day >= 22 || month == 7 && day <= 22) {
            return 3;
        } else if (month == 7 && day >= 23 || month == 8 && day <= 23) {
            return 4;
        } else if (month == 8 && day >= 24 || month == 9 && day <= 23) {
            return 5;
        } else if (month == 9 && day >= 24 || month == 10 && day <= 23) {
            return 6;
        } else if (month == 10 && day >= 24 || month == 11 && day <= 22) {
            return 7;
        } else if (month == 11 && day >= 23 || month == 12 && day <= 21) {
            return 8;
        } else if (month == 12 && day >= 22 || month == 1 && day <= 20) {
            return 9;
        } else if (month == 1 && day >= 21 || month == 2 && day <= 19) {
            return 10;
        } else if (month == 2 && day >= 20 || month == 3 && day <= 20) {
            return 11;
        } else {
            return -1;
        }
    }

    private static String[] astrologyNames = {
            "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "摩羯座", "水瓶座", "双鱼座"
    };

    public static String getAstrologyNameByDate(Date date) {
        int index = getAstrologyByDate(date);
        if (index < 0 || index > 11) {
            return "未知";
        } else {
            return astrologyNames[index];
        }
    }

    private static String[] animalNames = {
            "鼠", "猪", "狗", "鸡", "猴", "羊", "马", "蛇", "龙", "兔", "虎", "牛"
    };

    public static String getChineseZodiac(int year) {
        Calendar ca = Calendar.getInstance();
        int now = ca.get(Calendar.YEAR);
        int index = (now - year - 1) % 12 - 1;
        if (index > 0) {
            return animalNames[index];
        } else if (index == 0) {
            return animalNames[11];
        } else {
            return animalNames[12 - (-index)];
        }
    }

    /**
     * 用于解析复杂格式的日期和时间字符串，
     * 注意，不能用这个函数来解析无时间部分的日期
     * 如果要解析纯日期，例如 YYYY-MM-DD，请用 parseSimpleDate 函数
     *
     * @param text
     * @return
     */
    public static Date parseDate(String text) {
        DateTime dateTime = parseDateTime(text);
        if (dateTime == null) {
            return null;
        }
        return dateTime.toDate();
    }

	public static long pastTimeLong(String trxt) throws ParseException {
		SimpleDateFormat fame = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date data = fame.parse(trxt);
		return data.getTime();
	}

    public static DateTime parseDateTime(String text) {
        // 将这个日期的时间清零，因为当某一个字段没有形成匹配时，哪个字段会保留下当前时间的值
        // 例如 2008-08-01 9:42 的秒不会形成匹配，如果不清零，时间会是这个 DateTime 实例被创建时候的秒数和毫秒数
        DateTime dateTime = new DateTime().withMillisOfDay(0);
        Matcher matcher;
        if ((matcher = DATE_TIME_PATTERN.matcher(text)).matches()) {
            if (matcher.groupCount() > 0) {
                int replaceStart = 0;
                for (int i = 0; i < 6; i++) {
                    if (StringUtils.isNotEmpty(matcher.group(i + 1))) {
                        replaceStart = matcher.end(i + 1);
                        dateTime = setDateTimePart(dateTime, i, Integer.parseInt(matcher.group(i + 1)));
                    }
                }
                if (replaceStart == 0) {
                    for (int i = 0; i < 6; i++) {
                        if (StringUtils.isNotEmpty(matcher.group(i + 7))) {
                            replaceStart = matcher.end(i + 7);
                            dateTime = setDateTimePart(dateTime, i, Integer.parseInt(matcher.group(i + 7)));
                        }
                    }
                }
                if (replaceStart == 0) {
                    for (int i = 0; i < 6; i++) {
                        if (StringUtils.isNotEmpty(matcher.group(i + 13))) {
                            replaceStart = matcher.end(i + 13);
                            dateTime = setDateTimePart(dateTime, i, Integer.parseInt(matcher.group(i + 13)));
                        }
                    }
                }
                // YYYYMMDD
                if (replaceStart == 0) {
                    for (int i = 0; i < 6; i++) {
                        if (StringUtils.isNotEmpty(matcher.group(i + 19))) {
                            replaceStart = matcher.end(i + 19);
                            dateTime = setDateTimePart(dateTime, i, Integer.parseInt(matcher.group(i + 19)));
                        }
                    }
                }
                if (replaceStart == 0) {
                    for (int i = 0; i < 6; i++) {
                        if (StringUtils.isNotEmpty(matcher.group(i + 25))) {
                            replaceStart = matcher.end(i + 25);
                            dateTime = setDateTimePart(dateTime, i, Integer.parseInt(matcher.group(i + 25)));
                        }
                    }
                }
                // MM-DD-YYYY HH:mm:ss（没有ss也可）
                if (replaceStart == 0) {
                    for (int i = 0; i < 6; i++) {
                        if (StringUtils.isNotEmpty(matcher.group(i + 31))) {
                            if (i == 0) {
                                dateTime = setDateTimePart(dateTime, 1, Integer.parseInt(matcher.group(i + 31)));
                            } else if (i == 1) {
                                dateTime = setDateTimePart(dateTime, 2, Integer.parseInt(matcher.group(i + 31)));
                            } else if (i == 2) {
                                dateTime = setDateTimePart(dateTime, 0, Integer.parseInt(matcher.group(i + 31)));
                            } else {
                                dateTime = setDateTimePart(dateTime, i, Integer.parseInt(matcher.group(i + 31)));
                            }
                            replaceStart = matcher.end(i + 31);
                        }
                    }
                }
                // DD-MMM-YYYY:HH:mm:ss
                // DD/MMM/YYYY HH:mm:ss
                // DD/MMM/YYYYTHH:mm:ss
                if (replaceStart == 0) {
                    for (int i = 0; i < 6; i++) {
                        if (StringUtils.isNotEmpty(matcher.group(i + 37))) {
                            if (i == 0) {
                                dateTime = setDateTimePart(dateTime, 2, Integer.parseInt(matcher.group(i + 37)));
                            } else if (i == 1) {
                                dateTime = setDateTimePart(dateTime, 1, monthNameMap.get(matcher.group(i + 37)));
                            } else if (i == 2) {
                                dateTime = setDateTimePart(dateTime, 0, Integer.parseInt(matcher.group(i + 37)));
                            } else {
                                dateTime = setDateTimePart(dateTime, i, Integer.parseInt(matcher.group(i + 37)));
                            }
                            replaceStart = matcher.end(i + 37);
                        }
                    }
                }
                if (replaceStart == 0) {
                    for (int i = 0; i < 3; i++) {
                        if (StringUtils.isNotEmpty(matcher.group(i + 43))) {
                            if (i == 0) {
                                dateTime = setDateTimePart(dateTime, 1, Integer.parseInt(matcher.group(i + 43)));
                            } else if (i == 1) {
                                dateTime = setDateTimePart(dateTime, 2, Integer.parseInt(matcher.group(i + 43)));
                            } else if (i == 2) {
                                dateTime = setDateTimePart(dateTime, 0, Integer.parseInt(matcher.group(i + 43)));
                            }
                            replaceStart = matcher.end(i + 43);
                        }
                    }
                }
            }
            return dateTime;
        }
        return null;
    }

    /**
     * 根据生日取得当前年龄
     *
     * @param birthDay 出生日期
     * @return
     * @throws Exception
     */
    public static int getAge(Date birthDay) throws Exception {
        Calendar cal = Calendar.getInstance();

        if (cal.before(birthDay)) {
            throw new IllegalArgumentException("生日大于当前日期");
        }

        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(birthDay);

        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {

                if (dayOfMonthNow < dayOfMonthBirth) {
                    age--;
                }
            } else {
                age--;
            }
        }
        return age;
    }


    /**
     * @param date 相差天数
     * @param time 几点
     * @return
     */
    public static Date[] getDateTime(int date, int time) {
        Date[] result = new Date[2];
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, time);
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        Date endDate = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, -date);
        Date beginDate = cal.getTime();
        result[0] = beginDate;
        result[1] = endDate;
        return result;
    }

    /**
     * 取当前日0点和24点
     *
     * @return
     */
    public static Date[] getToday() {
        Date[] dates = getDateTime(-1, 0);
        return new Date[]{dates[1], dates[0]};
    }

    /**
     * 取当前日的和前一天日期
     *
     * @return
     */
    public static Date[] getYesterday() {
        return getDateTime(1, 0);
    }

    public static Date[] getWeekTime() {
        return getWeekTime(new Date());
    }

    private static Calendar getADayOfWeek(Calendar day, int dayOfWeek) {
        int week = day.get(Calendar.DAY_OF_WEEK);
        if (week == dayOfWeek) { return day; }
        int diffDay = dayOfWeek - week;
        if (week == Calendar.SUNDAY) {
            diffDay -= 7;
        } else if (dayOfWeek == Calendar.SUNDAY) {
            diffDay += 7;
        }
        day.add(Calendar.DATE, diffDay);
        return day;
    }

    /**
     * 得到本周的第一天
     *
     * @return
     */
    public static Date getWeekFirstDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Date beginDate = getADayOfWeek(calendar, Calendar.MONDAY).getTime();
        return truncateDate(beginDate);
    }

    /**
     * 得到本周的最后一天
     *
     * @return
     */
    public static Date getWeekLastDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Date endDate = getADayOfWeek(calendar, Calendar.SUNDAY).getTime();
        return truncateDate(plusDays(endDate, 1));
    }

    /**
     * 取当前日期的当前周的第一天和最后一天日期
     *
     * @return
     */
    public static Date[] getWeekTime(Date date) {
        Date[] result = new Date[2];
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Date beginDate = getADayOfWeek(calendar, Calendar.MONDAY).getTime();
        Date endDate = getADayOfWeek(calendar, Calendar.SUNDAY).getTime();
        result[0] = truncateDate(beginDate);
        result[1] = truncateDate(plusDays(endDate, 1));
        return result;

    }

    /**
     * 取当前日期的上周的第一天和最后一天日期
     *
     * @return
     */
    public static Date[] getLastWeekTime() {
        Date[] date = getWeekTime();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date[0]);
        cal.add(Calendar.DAY_OF_MONTH, -7);
        Date beginDate = cal.getTime();
        cal.setTime(date[1]);
        cal.add(Calendar.DAY_OF_MONTH, -7);
        Date endDate = cal.getTime();
        Date[] result = new Date[2];
        result[0] = beginDate;
        result[1] = endDate;
        return result;
    }

    /**
     * 获取上个星期天的最早时刻。
     *
     * @return
     */
    public static Date getStartOfLastSunday() {
        Date[] date = getLastWeekTime();
        Date startOfMonday = date[1];
        return plusDays(startOfMonday, -1);
    }

    /**
     * 得到本月的第一天
     *
     * @return
     */
    public static Date getMonthFirstDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return calendar.getTime();
    }

    /**
     * 得到本月的最后一天
     *
     * @return
     */
    public static Date getMonthLastDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return calendar.getTime();
    }

    /**
     * 根据传入日期取当前月的第一天和下个月的第一天,时分秒毫秒都清零
     *
     * @return
     */
    public static Date[] getMonthTime(Date date) {
        Date[] result = new Date[2];
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        truncateCalendar(cal);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date beginDate = cal.getTime();
        cal.add(Calendar.MONTH, 1);
        Date endDate = cal.getTime();
        result[0] = beginDate;
        result[1] = endDate;
        return result;
    }

    /**
     * 取当前日期的当前月的第一天和下个月的第一天,时分秒毫秒都清零
     *
     * @return
     */
    public static Date[] getMonthTime() {
        return getMonthTime(new Date());
    }

    /**
     * @param dateTime 要转换的DateTime类
     * @return MM-DD格式的时间字符串
     */

    public static String toDateTimeMMDDString(Date dateTime) {
        String date = null;
        if (dateTime != null) {
            date = JAVA_DATEMMDD_FORMAT.format(dateTime);
        }
        return date;
    }

    /**
     * @param dateTime 要转换的DateTime类
     * @return MM-DD格式的时间字符串
     */
    public static String toDateTimeMMDD2String(Date dateTime) {
        String date = null;
        if (dateTime != null) {
            date = JAVA_DATEMMDD2_FORMAT.format(dateTime);
        }
        return date;
    }

    /**
     * @param timeCode 例如：20120207
     * @return
     */
    public static Date dateTimeCode2Date(long timeCode) {
        String timeCodeString = String.valueOf(timeCode);
        int year = Integer.parseInt(timeCodeString.substring(0, 4));
        int month = Integer.parseInt(timeCodeString.substring(4, 6));
        int day = Integer.parseInt(timeCodeString.substring(6, 8));
        int hour = Integer.parseInt(timeCodeString.substring(8, 10));
        int minute = Integer.parseInt(timeCodeString.substring(10, 12));
        return DateUtils.toDate(year, month, day, hour, minute);
    }

    /**
     * @param timeCode 例如：20120207
     * @return
     */
    public static Date dateCode2Date(long timeCode) {
        String timeCodeString = String.valueOf(timeCode);
        int year = Integer.parseInt(timeCodeString.substring(0, 4));
        int month = Integer.parseInt(timeCodeString.substring(4, 6));
        int day = Integer.parseInt(timeCodeString.substring(6, 8));
        return DateUtils.toDate(year, month, day);
    }

    /**
     * @param monthCode 例如：201202
     * @return
     */
    public static Date monthCode2Date(long monthCode) {
        String timeCodeString = String.valueOf(monthCode);
        int year = Integer.parseInt(timeCodeString.substring(0, 4));
        int month = Integer.parseInt(timeCodeString.substring(4, 6));
        return DateUtils.toDate(year, month);
    }

    /**
     * 日期在本年的第几周
     *
     * @param date
     * @return
     */
    public static int getWeekOfYear(Date date) {
        if (date == null) {
            return 0;
        }
        Calendar c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        return c.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * 日期年份
     *
     * @param date
     * @return
     */
    public static int getYear(Date date) {
        if (date == null) {
            return 0;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.YEAR);
    }

    /**
     * 日期月份
     *
     * @param date
     * @return
     */
    public static int getMonth(Date date) {
        if (date == null) {
            return 0;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.MONTH);
    }

    /**
     * 返回日期当月第一个周一日期
     *
     * @param date
     * @return
     */
    public static Date getFisrtMondayOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.setFirstDayOfWeek(Calendar.MONDAY); //设置每周的第一天为星期一
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);//每周从周一开始
        c.set(Calendar.WEEK_OF_MONTH, Calendar.SUNDAY);//每周从周一开始
        c.setMinimalDaysInFirstWeek(7);
        return c.getTime();
    }

    /**
     * 取得日期当月所有周一的日期
     *
     * @param date
     * @return
     */
    public static List<Date> getMondaysOfMonth(Date date) {
        List<Date> result = new ArrayList<Date>();
        Date fisrtMonday = getFisrtMondayOfMonth(date);
        Date secondMonday = plusDays(fisrtMonday, 7);
        Date thirdMonday = plusDays(secondMonday, 7);
        Date fourthMonday = plusDays(thirdMonday, 7);
        result.add(fisrtMonday);
        result.add(secondMonday);
        result.add(thirdMonday);
        result.add(fourthMonday);
        Date fifthMonday = plusDays(fourthMonday, 7);
        int currmonth = getMonth(date);
        int month = getMonth(fifthMonday);
        if (currmonth == month) {
            result.add(fifthMonday);
        }
        return result;

    }

    public static int differMonth(Date start, Date end) {
        int monthday;
        try {
            Calendar starCal = Calendar.getInstance();
            starCal.setTime(start);
            int sYear = starCal.get(Calendar.YEAR);
            int sMonth = starCal.get(Calendar.MONTH);

            Calendar endCal = Calendar.getInstance();
            endCal.setTime(end);
            int eYear = endCal.get(Calendar.YEAR);
            int eMonth = endCal.get(Calendar.MONTH);
            monthday = ((eYear - sYear) * 12 + (eMonth - sMonth));
            monthday = Math.abs(monthday);
        } catch (Exception e) {
            monthday = 0;
        }
        return monthday;
    }

    public static String getDayOfWeek(Calendar calendar) {
        int dayth = calendar.get(Calendar.DAY_OF_WEEK);
        String dayOfWeek = "";
        switch (dayth) {
            case 1:
                dayOfWeek = "星期天";
                break;
            case 2:
                dayOfWeek = "星期一";
                break;
            case 3:
                dayOfWeek = "星期二";
                break;
            case 4:
                dayOfWeek = "星期三";
                break;
            case 5:
                dayOfWeek = "星期四";
                break;
            case 6:
                dayOfWeek = "星期五";
                break;
            case 7:
                dayOfWeek = "星期六";
                break;
        }

        return dayOfWeek;
    }

    /**
     * 获取最近7天的0点
     *
     * @return
     */
    public static List<Date> getLatest7Days() {
        List<Date> days = new ArrayList<Date>();
        for (int date = 0; date < 7; date++) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.clear(Calendar.MINUTE);
            cal.clear(Calendar.SECOND);
            cal.clear(Calendar.MILLISECOND);
            cal.add(Calendar.DAY_OF_MONTH, -date);
            Date beginDate = cal.getTime();
            days.add(beginDate);
        }
        return days;
    }

    public static void main(String[] args) throws UnsupportedEncodingException, ParseException {
        List<Date> days = getLatest7Days();
//		String str = "2010-08-06 23:22:24";
        String str1 = "2012-09-16 23:04:50";
//		Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(str);
        Date date1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(str1);
        Date date = DateUtils.plusDays(date1, -90);
        System.out.println(date);

//		String s  = "J7k$x*U5";
//		String md5Str =MD5Util.md5("aXBZbPd9raR5wK4eyBb11tmK" + s);
//		System.out.println(md5Str);
//		Date date = new Date();
//		long time = date.getTime();
//		System.out.println(time);

    }
}
