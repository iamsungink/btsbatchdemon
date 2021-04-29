package kr.co.ktp.bts.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTime {
	public static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";
	public static final Locale DEFAULT_LOCALE = Locale.getDefault();
	
	private static final String[] WEEK_NAMES = {"일", "월", "화", "수", "목", "금", "토"};

	// 각 단위의 백분의 1초 환산값
	public static final int millisPerSecond	= 1000;
	public static final int millisPerMinute		= millisPerSecond * 60;
	public static final int millisPerHour		= millisPerMinute * 60;
	public static final int millisPerDay			= millisPerHour * 24;
	
	/** Don't let anyone instantiate this class */
	private DateTime() {};

	public static Date check(String dateTime, String pattern) throws ParseException {
		if (dateTime == null ) throw new ParseException("date string to check is null", 0);
		if (pattern == null ) throw new ParseException("pattern string to check date is null", 0);

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, DEFAULT_LOCALE);
		Date date = simpleDateFormat.parse(dateTime);
		
		if(!simpleDateFormat.format(date).equals(dateTime)) throw new ParseException("Out of bound date:\"" + dateTime + "\" with pattern \"" + pattern + "\"", 0);

        return date;
	}
	
	public static boolean isDate(String dateTime, String pattern) {
		try {
			check(dateTime, pattern);
		} catch (ParseException pe) {
			return false;
		}
		
		return true;
	}
	
	public static Date toDate(String dateTime, String pattern) {
		Date date = null;
		
		try {
			date = check(dateTime, pattern);
		} catch (ParseException pe) {
			pe.printStackTrace();
		}
		
		return date;
	}

	public static Date toDate(Calendar calendar) {
		return calendar.getTime();
	}

	public static Calendar toCalendar(String dateTime, String pattern) {
		return toCalendar(toDate(dateTime, pattern));
	}
	
	public static Calendar toCalendar(Date date) {
		Calendar calendar = Calendar.getInstance(DEFAULT_LOCALE);
		calendar.setTime(date);

		return calendar;
	}
	
	public static String toString(Date date, String pattern) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

		return simpleDateFormat.format(date);
	}
	
	public static String toString(Calendar calendar, String pattern) {
		return toString(toDate(calendar), pattern);
	}

	public static String getCurrent(String pattern) {
		return toString(new Date(), pattern);
	}
	public static String getCurrent() {
		return getCurrent(DEFAULT_PATTERN);
	}

	public static int getYear(Date date) {
		return getInt(toString(date, "yyyy"));
	}
	public static int getMonth(Date date) {
		return getInt(toString(date, "MM"));
	}
	public static int getDay(Date date) {
		return getInt(toString(date, "dd"));
	}
	
	public static int getYear(Calendar calendar) {
		return getYear(toDate(calendar));
	}
	public static int getMonth(Calendar calendar) {
		return getMonth(toDate(calendar));
	}
	public static int getDay(Calendar calendar) {
		return getDay(toDate(calendar));
	}
	
	public static int getCurrentYear() {
		return getYear(new Date());
	}
	public static int getCurrentMonth() {
		return getMonth(new Date());
	}
	public static int getCurrentDay() {
		return getDay(new Date());
	}
	
	public static int betweenYear(String from, String to, String pattern) {
		Calendar fromCalendar = toCalendar(from, pattern);
		Calendar toCalendar = toCalendar(to, pattern);
		
		return toCalendar.get(Calendar.YEAR) - fromCalendar.get(Calendar.YEAR);
	}
	
	public static int betweenMonth(String from, String to, String pattern) {
		Calendar fromCalendar = toCalendar(from, pattern);
		Calendar toCalendar = toCalendar(to, pattern);
		
		int year = toCalendar.get(Calendar.YEAR) - fromCalendar.get(Calendar.YEAR);
		int month = toCalendar.get(Calendar.MONTH) - fromCalendar.get(Calendar.MONTH);
		
		return year * 12 + month;
	}
	
	public static int betweenDays(String from, String to, String pattern) {
		return (int) (betweenTime(from, to, pattern) / millisPerDay);
	}

	public static long betweenTime(String from, String to, String pattern) {
		Date fromTime = toDate(from, pattern);
		Date toTime = toDate(to, pattern);
		
		return toTime.getTime() - fromTime.getTime();
	}

	public static String addYears(String dateTime, String pattern, int years) {
		Calendar calendar = toCalendar(dateTime, pattern);
		calendar.add(Calendar.YEAR, years);
		
		return toString(calendar, pattern);
	}
	
	public static String addMonths(String dateTime, String pattern, int months) {
		Calendar calendar = toCalendar(dateTime, pattern);
		calendar.add(Calendar.MONTH, months);
		
		return toString(calendar, pattern);
	}
	
	public static String addDays(String dateTime, String pattern, int days) {
		Calendar calendar = toCalendar(dateTime, pattern);
		calendar.add(Calendar.DATE, days);
		
		return toString(calendar, pattern);
	}
	
	public static String addHours(String dateTime, String pattern, int hours) {
		Calendar calendar = toCalendar(dateTime, pattern);
		calendar.add(Calendar.HOUR, hours);
		
		return toString(calendar, pattern);
	}
	
	public static String addMinute(String dateTime, String pattern, int minutes) {
		Calendar calendar = toCalendar(dateTime, pattern);
		calendar.add(Calendar.MINUTE, minutes);
		
		return toString(calendar, pattern);
	}
	
	public static String addSeconds(String dateTime, String pattern, int seconds) {
		Calendar calendar = toCalendar(dateTime, pattern);
		calendar.add(Calendar.SECOND, seconds);
		
		return toString(calendar, pattern);
	}
	
	public static String getLastDayOfMonth(String dateTime, String pattern) {
		Calendar calendar = toCalendar(dateTime, pattern);
		calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		
		return toString(calendar, pattern);
	}
	
	public static int getDayOfWeek(String dateTime, String pattern) {
		return toCalendar(dateTime, pattern).get(Calendar.DAY_OF_WEEK);
	}
	
	public static String dayOfWeek(String dateTime, String pattern) {
		return WEEK_NAMES[getDayOfWeek(dateTime, pattern) - 1];
	}
	public static int getInt(String sourceString) {
		int rtnValue = 0;

		if(sourceString != null && sourceString.length() > 0) {
			rtnValue = Integer.parseInt(sourceString.replaceAll(" ", ""));
		}

		return rtnValue;
	}

}
