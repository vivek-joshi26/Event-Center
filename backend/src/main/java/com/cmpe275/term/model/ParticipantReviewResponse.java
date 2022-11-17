package com.cmpe275.term.model;

import com.cmpe275.term.entity.ApprovalStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantReviewResponse {

	public Long id;
	public Long userId;
    private ApprovalStatus approvalStatus;
    private String participantName;
    private Double reviewStar;
    private String reviewText;
}
