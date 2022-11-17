package com.cmpe275.term.model;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.cmpe275.term.entity.EventStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventRequest {

	//private String id;
	private String title;
	private String description;
	private Integer minParticipants;
	private Integer maxParticipants;
	private Integer totalParticipants;
	private Address address;
	private String admissionPolicy;
	private EventStatus status;
	private Long fee;
	private String startDateTime;
	private String endDateTime;
	private String deadline;
	private Long userId;
	private String sysDate;
}
