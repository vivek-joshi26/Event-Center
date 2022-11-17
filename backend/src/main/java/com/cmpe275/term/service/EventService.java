package com.cmpe275.term.service;

import java.text.ParseException;
import java.util.List;

import com.cmpe275.term.entity.ApprovalStatus;
import com.cmpe275.term.entity.ParticipantForumStatus;
import com.cmpe275.term.model.*;
import org.springframework.http.ResponseEntity;

public interface EventService {
	
	public ResponseEntity createEvent(EventRequest request) throws Exception;

	public ResponseEntity getEvents(Long userId);

	public ResponseEntity getEventsParticipatedIn(Long userId, ApprovalStatus approvalStatus);

	
	public List<EventFilterResponse> getEventsByFilter(EventFilterRequest request);

	public ResponseEntity updateParticipantForumStatus(Long eventId, ParticipantForumStatus participantForumStatus);
	
	public ResponseEntity getAllEventsAndAttendeesDataByOrganizer(Long userId);

	public ResponseEntity getSystemReport(ReportRequest reportRequest) throws ParseException;

	public ResponseEntity getUserReport(ReportRequest reportRequest) throws ParseException;

}
