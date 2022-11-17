package com.cmpe275.term.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantReviewModel {
	
	private Double avgReputation;
	private List<ReviewResponseModel> reviews;

}
