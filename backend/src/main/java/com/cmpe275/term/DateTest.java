package com.cmpe275.term;

import java.text.ParseException;
import java.util.regex.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DateTest {

	
	public static void main(String[] args) {
		String d = "2022-05-29 11:43";
		System.out.println(Pattern.matches("^sameer anil joshi.*$", "sameer anil joshikhhh427788hhh"));
		try {
			Date date = DateTest.convertToDate(d);
			int hours=date.getHours();
			int min = date.getMinutes();
			LocalDate local= date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(5);
			Date outputDate = Date.from(local.atStartOfDay(ZoneId.systemDefault()).toInstant());
			outputDate.setHours(hours);
			outputDate.setMinutes(min);
			//System.out.println(outputDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	

	}
	
	public static Date convertToDate(String date) throws ParseException {
		Date convertedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(date);
		return convertedDate;

}
	
	public static String convertDateToString(Date date) {
		SimpleDateFormat destFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return destFormat.format(date);
	}

}

