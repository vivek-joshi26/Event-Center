package com.cmpe275.term.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponseModel {
	
	private String reviewerName;
	private String eventName;
	private Double reviewStar;
	private String reviewText;

}
