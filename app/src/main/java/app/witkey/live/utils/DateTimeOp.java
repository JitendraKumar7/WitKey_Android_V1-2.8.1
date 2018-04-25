package app.witkey.live.utils;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@SuppressLint("SimpleDateFormat")
public class DateTimeOp {


	public static String convertDate(String dateInMilliseconds,
                                     String dateFormat) {
		if (!(dateInMilliseconds instanceof String))
			dateInMilliseconds = "0";
		if (dateInMilliseconds.isEmpty())
			dateInMilliseconds = "0";
		return (new SimpleDateFormat(dateFormat).format(new Date(
				dateStringToLong(dateInMilliseconds))));
	}

	public static String convertDate(String dateInMilliseconds,
                                     String dateFormat, String timeZone) {
		if (!(dateInMilliseconds instanceof String))
			dateInMilliseconds = "0";
		if (dateInMilliseconds.isEmpty())
			dateInMilliseconds = "0";
		SimpleDateFormat df = new SimpleDateFormat(dateFormat);
		df.setTimeZone(TimeZone.getTimeZone(timeZone));
		String strDF = df
				.format(new Date(dateStringToLong(dateInMilliseconds)));
		return (strDF);
	}

	public static long dateStringToLong(String dateInMiliseconds) {
		if (!(dateInMiliseconds instanceof String))
			dateInMiliseconds = "0";
		if (dateInMiliseconds.isEmpty())
			dateInMiliseconds = "0";
		return (Long.parseLong(dateInMiliseconds.replaceAll("[^\\d.]", "")));
	}

	public static Calendar dateMilisecondsToCalendar(long milliSeconds) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(milliSeconds);
		return calendar;
	}

	public static String oneFormatToAnother(String date, String oldFormat,
                                            String newFormat) {
		try {
			DateFormat originalFormat = new SimpleDateFormat(oldFormat,
					Locale.ENGLISH);
			DateFormat targetFormat = new SimpleDateFormat(newFormat);
			Date d = originalFormat.parse(date);
			String formattedDate = targetFormat.format(d);
			return formattedDate;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public static String getCurrentDateTime(String format) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			String currentDateandTime = sdf.format(new Date());
			return currentDateandTime;
		} catch (Exception e) {
			return null;
		}
	}

	public static String getCurrentDateTimeInUTC(String format) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			sdf.setTimeZone(TimeZone.getTimeZone("gmt"));
			String currentDateandTime = sdf.format(new Date());
			return currentDateandTime;
		} catch (Exception e) {
			return null;
		}
	}

	public static Calendar getCalendarFromFormat(String date, String format) {
		Calendar cal = Calendar.getInstance();
		try {
			DateFormat originalFormat = new SimpleDateFormat(format,
					Locale.ENGLISH);
			Date d = originalFormat.parse(date);
			cal.setTime(d);
			return cal;
		} catch (Exception e) {
			return null;
		}
	}



	public static boolean isFirstDateGreater(Date date1, Date date2) {

		long miliSeconds1 = date1.getTime();
		long miliSeconds2 = date2.getTime();

		if (miliSeconds1 > miliSeconds2)
			return true;

		return false;
	}

}
