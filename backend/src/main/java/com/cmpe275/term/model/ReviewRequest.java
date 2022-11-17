package com.cmpe275.term.model;

import org.springframework.stereotype.Component;

import com.cmpe275.term.enums.ReviewEnum;

import lombok.Data;

@Component
@Data
public class ReviewRequest {
	
	private Long eventId;
	private Long reviewGivenBy;
	private Long reviewGivenTo;
	private String reviewText;
	private Double reviewStar;
	private ReviewEnum reviewFor;
	private String sysDate;

}
