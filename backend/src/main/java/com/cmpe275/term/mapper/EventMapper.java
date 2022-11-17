package com.cmpe275.term.mapper;

import com.cmpe275.term.model.EventsParticipatedInResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cmpe275.term.entity.Event;
import com.cmpe275.term.entity.User;
import com.cmpe275.term.model.EventFilterResponse;
import com.cmpe275.term.model.EventRequest;
import com.cmpe275.term.utility.DateUtility;

@Component
public class EventMapper {
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private DateUtility dateUtility;
	public Event eventModelToEntity(EventRequest request) {
		
		Event event = this.modelMapper.map(request, Event.class);
		return event;
	}
	
	public EventFilterResponse entityToEventFilterResponse(Event event) {
		
		EventFilterResponse response = this.modelMapper.map(event, EventFilterResponse.class);
		User user = event.getUser();
		response.setUserName(user.getScreenName());
		response.setUserId(user.getId());
		if(event.getDeadline()!=null) {
			response.setDeadline(this.dateUtility.convertDateToString(event.getDeadline()));
		}
		if(event.getEndDateTime()!=null) {
			response.setEndDateTime(this.dateUtility.convertDateToString(event.getEndDateTime()));
		}
		if(event.getStartDateTime()!=null) {
			response.setStartDateTime(this.dateUtility.convertDateToString(event.getStartDateTime()));
		}
		/*
			if(event.getCreationDateTime()!=null) {
			response.setStartDateTime(this.dateUtility.convertDateToString(event.getCreationDateTime()));
		}
		 */

		return response;
	}

	public EventsParticipatedInResponse entityToEventsParticipatedInResponse(Event event) {

		EventsParticipatedInResponse response = this.modelMapper.map(event, EventsParticipatedInResponse.class);
		User user = event.getUser();
		response.setUserName(user.getScreenName());
		response.setUserId(user.getId());
		if(event.getDeadline()!=null) {
			response.setDeadline(this.dateUtility.convertDateToString(event.getDeadline()));
		}
		if(event.getEndDateTime()!=null) {
			response.setEndDateTime(this.dateUtility.convertDateToString(event.getEndDateTime()));
		}
		if(event.getStartDateTime()!=null) {
			response.setStartDateTime(this.dateUtility.convertDateToString(event.getStartDateTime()));
		}

		/*
			if(event.getCreationDateTime()!=null) {
			response.setStartDateTime(this.dateUtility.convertDateToString(event.getCreationDateTime()));
		}
		 */
		return response;
	}

}
