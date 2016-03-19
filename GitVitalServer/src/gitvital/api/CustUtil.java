package gitvital.api;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import weka.core.FastVector;

public class CustUtil {
	public static boolean isWeb = false;
	
	public static LocalDateTime strToDateTime(String dateStr){
		LocalDateTime dateTime = null;
		dateTime = LocalDateTime.parse(dateStr, DateTimeFormatter.ISO_DATE_TIME);
		return dateTime;
	}
	
	public static long daysSince(LocalDateTime dateObj){
		//TODO Move to a 'settings' portion
		LocalDateTime compare = LocalDateTime.of(2016, 3, 1, 0, 0);
		if(isWeb)
			compare = LocalDateTime.now();
		long days = ChronoUnit.DAYS.between(dateObj, compare);
		return days;
	}
	
	public static <T> FastVector listToFv(List<T> list){
		FastVector fv = new FastVector();
		for(T o : list){
			fv.addElement(o);
		}
		return fv;
	}
	
	public static FastVector strArrayToFv(String[] ary){
		FastVector fv = new FastVector();
		for(String s : ary){
			fv.addElement(s);
		}
		return fv;
	}
	
	public static long daysFromStr(String dateStr){
		LocalDateTime dt = strToDateTime(dateStr);
		return daysSince(dt);
	}
}
