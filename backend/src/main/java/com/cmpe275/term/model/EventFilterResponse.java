package com.cmpe275.term.model;

import com.cmpe275.term.entity.EventStatus;

import com.cmpe275.term.entity.ParticipantForumStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventFilterResponse {
	private Long id;
	private String title;
	private String description;
	private Integer minParticipants;
	private Integer maxParticipants;
	private Integer totalParticipants;
	private Address address;
	private String admissionPolicy;
	private EventStatus status;
	private EventStatus signupForumStatus;
	private ParticipantForumStatus participantForumStatus;
	private Long fee;
	private String startDateTime;
	private String endDateTime;
	private String deadline;
	private Long userId;
	private String userName;

	// private String startDateTime;

}
