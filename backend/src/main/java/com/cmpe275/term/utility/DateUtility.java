package com.cmpe275.term.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class DateUtility {
	
	public Date convertToDate(String date) throws ParseException {
		Date convertedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(date);
		return convertedDate;

}
	
	public String convertDateToString(Date date) {
		SimpleDateFormat destFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return destFormat.format(date);
	}
}
