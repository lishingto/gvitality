package gvitality.api;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Locale;

public class CustUtil {
	public static LocalDateTime strToDateTime(String dateStr){
		LocalDateTime dateTime = null;
		dateTime = LocalDateTime.parse(dateStr, DateTimeFormatter.ISO_DATE_TIME);
		return dateTime;
	}
	
	public static long daysSince(LocalDateTime dateObj){
		//TODO Move to a 'settings' portion
		long days = ChronoUnit.DAYS.between(dateObj, LocalDateTime.of(2016, 3, 1, 0, 0));
		return days;
	}
	
	public static long daysFromStr(String dateStr){
		LocalDateTime dt = strToDateTime(dateStr);
		return daysSince(dt);
	}
}
