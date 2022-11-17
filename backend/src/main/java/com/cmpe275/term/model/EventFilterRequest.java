package com.cmpe275.term.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventFilterRequest {
	
	private String city;
	private String keyword;
	private String screenName;
	private String status;
	private String startDateTime;
	private String endDateTime;

}
