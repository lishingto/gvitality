package test;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.Test;

import gvitality.api.CustUtil;

public class UtilTest {

	@Test
	public void dateTest() {
		LocalDateTime dateObj = CustUtil.strToDateTime("2014-03-31T04:14:05Z");
		if(dateObj == null)
			fail("Date is Null");
		
		System.out.println("Day: " + dateObj.getDayOfMonth() + ", Month: " + dateObj.getMonth() + ", Year: " + dateObj.getYear());
		System.out.println("Days Since: " + CustUtil.daysSince(dateObj));
		assertTrue(true);
	}

}
