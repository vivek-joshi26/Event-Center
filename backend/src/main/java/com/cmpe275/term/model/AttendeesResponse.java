package com.cmpe275.term.model;

import java.util.List;

import com.cmpe275.term.entity.Participant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendeesResponse {

	List<ParticipantReviewResponse> participantList;
	private String startDateTime;
	private String endDateTime;
}
