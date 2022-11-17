
package com.cmpe275.term.controller;

import java.util.List;

import com.cmpe275.term.entity.ApprovalStatus;
import com.cmpe275.term.entity.ParticipantForumStatus;
import com.cmpe275.term.model.ParticipantApprovalRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cmpe275.term.model.EventFilterRequest;
import com.cmpe275.term.model.EventFilterResponse;
import com.cmpe275.term.model.EventRequest;
import com.cmpe275.term.service.EventService;

@RestController
public class EventController {
	
	@Autowired
	private EventService eventService;

	@PostMapping("/createEvent")
	public ResponseEntity createEvent(@RequestBody EventRequest eventRequest) throws Exception {
//		try {
//			return this.eventService.createEvent(eventRequest);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return ResponseEntity.badRequest();
//		}
		return eventService.createEvent(eventRequest);
	}

	@GetMapping("/event/{userId}")
	public ResponseEntity getEvents(@PathVariable("userId") Long userId){
		return eventService.getEvents(userId);
	}
	
	@PostMapping("/eventsByFilter")
	public List<EventFilterResponse> getEventsByFilter(@RequestBody EventFilterRequest request){
		System.out.println(request.getStartDateTime());
		return this.eventService.getEventsByFilter(request);
	}

	@GetMapping("/event/participant-forum-status/{eventId}")
	public ResponseEntity updateParticipantForumStatus(@PathVariable("eventId") Long eventId, @RequestParam ParticipantForumStatus participantForumStatus){
		return eventService.updateParticipantForumStatus(eventId, participantForumStatus);
	}

	@GetMapping("/event-participated/{userId}")
	public ResponseEntity getEventsParticipatedIn(@PathVariable("userId") Long userId, @RequestParam(value = "approvalStatus", required = false ) ApprovalStatus approvalStatus){
		return eventService.getEventsParticipatedIn(userId, approvalStatus);
	}
	
	@GetMapping("/event-and-attendees-by-organizer/{userId}")
	public ResponseEntity getAllEventsAndAttendeesDataByOrganizer(@PathVariable("userId") Long userId){
		return eventService.getAllEventsAndAttendeesDataByOrganizer(userId);
	}

}
