package com.cmpe275.term.utility;
import java.util.regex.*;

import org.springframework.stereotype.Component;

@Component
public class RegexPattern {
	
	public boolean screenNameRegex(String fullName , String screenName) {
		String pattern = "^"+fullName.toLowerCase()+".*$";
		if(Pattern.matches(pattern, screenName.toLowerCase())) {
			return true;
		}
		return false;
	}

}
