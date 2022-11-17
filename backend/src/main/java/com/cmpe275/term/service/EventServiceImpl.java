package com.cmpe275.term.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.cmpe275.term.model.*;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cmpe275.term.entity.ApprovalStatus;
import com.cmpe275.term.entity.Event;
import com.cmpe275.term.entity.EventStatus;
import com.cmpe275.term.entity.Participant;
import com.cmpe275.term.entity.ParticipantForumStatus;
import com.cmpe275.term.entity.Reviews;
import com.cmpe275.term.entity.User;
import com.cmpe275.term.enums.ReviewEnum;
import com.cmpe275.term.mapper.EventMapper;
import com.cmpe275.term.repository.EventRepository;
import com.cmpe275.term.repository.ParticipantRepository;
import com.cmpe275.term.repository.ReviewRepository;
import com.cmpe275.term.repository.UserRepository;
import com.cmpe275.term.specification.EventSpecification;
import com.cmpe275.term.utility.DateUtility;

@Service
public class EventServiceImpl implements EventService {

	@Autowired
	private EventRepository eventRepository;
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private EventMapper eventMapper;
	
	@Autowired
	private EventSpecification eventSpecs;
	
	@Autowired
	private DateUtility dateUtility;

	@Autowired
	private ParticipantRepository participantRepository;
	
	@Autowired
	private ReviewRepository reviewRepository;

	@Autowired
	private EmailSenderService emailSenderService;

	@Override
	public ResponseEntity createEvent(EventRequest request) throws Exception {
		Event event = eventMapper.eventModelToEntity(request);
		try {
			
			Date startDateTime = dateUtility.convertToDate(request.getStartDateTime());
			Date endDateTime = dateUtility.convertToDate(request.getEndDateTime());
			Date deadline = dateUtility.convertToDate(request.getDeadline());
			Date currentDate = dateUtility.convertToDate(request.getSysDate());
			if(currentDate.after(startDateTime)) {
				//throw new Exception("start time cannot be pastdated");
				return new ResponseEntity("start time cannot be pastdated" , HttpStatus.BAD_REQUEST);
			}
			if(startDateTime.after(endDateTime)) {
				//throw new Exception("start time cannot be later than end time");
				return new ResponseEntity("start time cannot be later than end time" , HttpStatus.BAD_REQUEST);
			}
			if(deadline.after(startDateTime)) {
				// throw new Exception("deadline cannot be later than start time");
				return new ResponseEntity("deadline cannot be later than start time" , HttpStatus.BAD_REQUEST);
			}
			if(currentDate.after(deadline)) {
				// throw new Exception("deadline cannot be later than start time");
				return new ResponseEntity("deadline cannot be past dated" , HttpStatus.BAD_REQUEST);
			}
			//User user = this.userRepository.findById(request.getUserId()).get();
			User user = userRepository.getById(request.getUserId());
			event.setUser(user);
			event.setDeadline(deadline);
			event.setEndDateTime(endDateTime);
			event.setStartDateTime(startDateTime);
			event.setTotalParticipants(0);
			event.setStatus(EventStatus.OPEN);
			event.setSignupForumStatus(EventStatus.OPEN);
			event.setParticipantForumStatus(ParticipantForumStatus.CLOSED_TILL_REGISTRATION_DEADLINE);
			event.setCreationDateTime(currentDate);
			eventRepository.save(event);
			//return "event created";
			System.out.println("EVENT DETAILS ******************** : " + event );
			emailSenderService.sendEmail(user.getEmail(), "You have successfully created an event.", "Event created");
			return new ResponseEntity("event created" , HttpStatus.OK);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			// return "cannot create event";
			return new ResponseEntity("Some error occurred please try again later!" , HttpStatus.BAD_GATEWAY);
		}
		
	}

	@Override
	public ResponseEntity getEvents(Long userId) {
		User user = userRepository.getById(userId);
		List<Event> eventList = eventRepository.getByUser(user);
		List<EventFilterResponse> response = new ArrayList<>();
		for (Event event:eventList
			 ) {
			EventFilterResponse res = this.eventMapper.entityToEventFilterResponse(event);
			response.add(res);
		}

		return new ResponseEntity(response, HttpStatus.OK);
	}

	@Override
	public ResponseEntity getEventsParticipatedIn(Long userId, ApprovalStatus approvalStatus) {
		User user = userRepository.getById(userId);
		// List<Participant> participants = participantRepository.f
		List<Participant> participants = user.getParticipants();
		List<EventsParticipatedInResponse> response = new ArrayList<>();
		for (Participant singleParticipant: participants
			 ) {
			if(approvalStatus != null){
				if(singleParticipant.getApprovalStatus().equals(approvalStatus)){
					Event singleEvent = singleParticipant.getEvent();
					EventsParticipatedInResponse eventsParticipatedInResponse = this.eventMapper.entityToEventsParticipatedInResponse(singleEvent);
					eventsParticipatedInResponse.setApprovalStatus(singleParticipant.getApprovalStatus());
					Reviews review = this.reviewRepository.findReviewsByReviewGivenByAndReviewGivenToAndEventIdAndReviewFor(userId,singleEvent.getUser().getId(), singleEvent.getId(), ReviewEnum.ORGANIZER);
					if(review!=null) {
						eventsParticipatedInResponse.setReviewStar(review.getReviewStar());
						eventsParticipatedInResponse.setReviewText(review.getReviewText());;
					}
					response.add(eventsParticipatedInResponse);
				}
			}
			else {
				Event singleEvent = singleParticipant.getEvent();
				EventsParticipatedInResponse eventsParticipatedInResponse = this.eventMapper.entityToEventsParticipatedInResponse(singleEvent);
				eventsParticipatedInResponse.setApprovalStatus(singleParticipant.getApprovalStatus());
				Reviews review = this.reviewRepository.findReviewsByReviewGivenByAndReviewGivenToAndEventIdAndReviewFor(userId,singleEvent.getUser().getId(), singleEvent.getId(), ReviewEnum.ORGANIZER);
				if(review!=null) {
					eventsParticipatedInResponse.setReviewStar(review.getReviewStar());
					eventsParticipatedInResponse.setReviewText(review.getReviewText());;
				}
				response.add(eventsParticipatedInResponse);
			}
		}

		return new ResponseEntity(response, HttpStatus.OK);
	}

	@Override
	public List<EventFilterResponse> getEventsByFilter(EventFilterRequest request) {
		Specification<Event> specification = this.eventSpecs.getEventByFilter(request);
		List<EventFilterResponse> response = new ArrayList<>();
		List<Event> filters = this.eventRepository.findAll(specification);
		filters.forEach((filter)->{
			EventFilterResponse res = this.eventMapper.entityToEventFilterResponse(filter);
			response.add(res);
		});
		return response;
	}

	@Override
	public ResponseEntity updateParticipantForumStatus(Long eventId, ParticipantForumStatus participantForumStatus) {
		Event event = eventRepository.getById(eventId);
		event.setParticipantForumStatus(participantForumStatus);
		Event updatedEvent = eventRepository.save(event);
		EventFilterResponse res = this.eventMapper.entityToEventFilterResponse(updatedEvent);
		return new ResponseEntity(res , HttpStatus.OK);
	}

	@Override
	public ResponseEntity getAllEventsAndAttendeesDataByOrganizer(Long userId) {
		User user = this.userRepository.findById(userId).get();
		List<Event> createdEvents = this.eventRepository.getByUser(user);
		Map<Long,List<Participant>> eventParticipantsMap = new HashMap<>();
		for(Event event:createdEvents) {
			List<Participant> participants = this.participantRepository.findParticipantByEvent(event);
			eventParticipantsMap.put(event.getId(), participants);
			//need to add null check.
		}
		
		return new ResponseEntity(eventParticipantsMap , HttpStatus.OK);
	}

	@Override
	public ResponseEntity getSystemReport(ReportRequest reportRequest) throws ParseException {

		Date creationDate = dateUtility.convertToDate(reportRequest.getSysDate());
		System.out.println("Creation Date: " + creationDate);
		Date beforeDate = DateUtils.addDays(creationDate, -90);
		System.out.println("beforeDate Date: " + beforeDate);
		List<Event> eventList = eventRepository.getEventByCreationDateTimeAfter(beforeDate);

		eventList.forEach(event -> System.out.println(event.getId()));
		// 8-a-1
		Integer totalCreatedEvents = eventList.size();
		// 8-a-1
		Integer totalPaidEvents = (int) eventList.stream().filter(event -> event.getFee() != null && event.getFee() != 0).count();
		System.out.println("Paid events: " + totalPaidEvents);

		List<Event> cancelledEvents = eventList.stream().filter(event -> event.getStatus() == EventStatus.CANCELLED).collect(Collectors.toList());
		// 8-a-2
		Integer totalCancelledEvents = cancelledEvents.size();
		// 8-a-2
		Integer totalMinimumParticipantsInCancelledEvents = cancelledEvents.stream().flatMapToInt( event -> IntStream.of(event.getMinParticipants())).sum();
		// 8-a-2
		Integer totalParticipantRequestForCancelledEvents = cancelledEvents.stream().mapToInt(event -> event.getParticipants().size()).sum();

		System.out.println("Total cancelled events: " + totalCancelledEvents);
		System.out.println("Total minimum participants in cancelled events: " + totalMinimumParticipantsInCancelledEvents);
		System.out.println("Total participant requests for cancelled events: " + totalParticipantRequestForCancelledEvents);

		List<Event> finishedEvents = eventList.stream().filter(event -> event.getStatus() == EventStatus.FINISHED).collect(Collectors.toList());
		// 8-a-3
		Integer totalFinishedEvents = finishedEvents.size();
		// 8-a-3
		Integer totalParticipantsInFinishedEvents = finishedEvents.stream().mapToInt(event -> event.getTotalParticipants()).sum();

		System.out.println("Total finished events: " + totalFinishedEvents);
		System.out.println("Total participants in finished events: " + totalParticipantsInFinishedEvents);

		SystemReportResponse response = new SystemReportResponse();
		response.setTotalEvents(totalCreatedEvents);
		response.setTotalCancelledEvents(totalCancelledEvents);
		response.setTotalFinishedEvents(totalFinishedEvents);
		response.setTotalPaidEvents(totalPaidEvents);
		response.setTotalMinimumParticipantsInCancelledEvents(totalMinimumParticipantsInCancelledEvents);
		response.setTotalParticipantRequestForCancelledEvents(totalParticipantRequestForCancelledEvents);
		response.setTotalParticipantsInFinishedEvents(totalParticipantsInFinishedEvents);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Override
	public ResponseEntity getUserReport(ReportRequest reportRequest) throws ParseException {

		Date creationDate = dateUtility.convertToDate(reportRequest.getSysDate());
		System.out.println("Creation Date: " + creationDate);
		Date beforeDate = DateUtils.addDays(creationDate, -90);
		System.out.println("beforeDate Date: " + beforeDate);
		Long userId = reportRequest.getUserId();
		User currentUser = userRepository.getById(userId);
		List<Participant> participantList = participantRepository.findParticipantByUserAndCreationDateTimeAfter(currentUser, beforeDate);
		// 8-b-i-1
		Integer totalSignupEvents = participantList.size();
		// 8-b-i-2
		Integer totalApprovals = Math.toIntExact(participantList.stream().filter(participant -> participant.getApprovalStatus() == ApprovalStatus.APPROVED).count());
		// 8-b-i-2
		Integer totalRejects = Math.toIntExact(participantList.stream().filter(participant -> participant.getApprovalStatus() == ApprovalStatus.REJECTED).count());
		// 8-b-i-3
		Integer totalFinishedEvents = Math.toIntExact(participantList.stream().filter(participant -> participant.getEvent().getStatus() == EventStatus.FINISHED).count());

		// participation report
		System.out.println("Total totalSignupEvents events: " + totalSignupEvents);
		System.out.println("Total totalApprovals: " + totalApprovals);
		System.out.println("Total totalRejects: " + totalRejects);
		System.out.println("Total totalFinishedEvents: " + totalFinishedEvents);

		ParticipationReport participationReport = new ParticipationReport();
		participationReport.setTotalSignupEvents(totalSignupEvents);
		participationReport.setTotalApprovals(totalApprovals);
		participationReport.setTotalRejects(totalRejects);
		participationReport.setTotalFinishedEvents(totalFinishedEvents);

		// organizer report
		List<Event> createdEventList = eventRepository.getEventByCreationDateTimeAfterAndUser(beforeDate, currentUser);
		// 8-b-ii-1
		Integer totalEventsCreated = createdEventList.size();
		List<Event> paidEventList = createdEventList.stream().filter(event -> event.getFee() != null && event.getFee() != 0).collect(Collectors.toList());
		Integer totalPaidEvents = paidEventList.size();

		// 8-b-ii-2
		List<Event> cancelledEventList = createdEventList.stream().filter(event -> event.getStatus() == EventStatus.CANCELLED).collect(Collectors.toList());
		Integer totalCancelledEvents = cancelledEventList.size();
		Integer totalMinimumParticipantsRequired = cancelledEventList.stream().mapToInt(event -> event.getMinParticipants()).sum();
		Integer totalParticipationRequests = cancelledEventList.stream().mapToInt(event -> event.getParticipants().size()).sum();

		// 8-b-ii-3
		List<Event> finishedEventList = createdEventList.stream().filter(event -> event.getStatus() == EventStatus.FINISHED).collect(Collectors.toList());
		Integer totalParticipantsInFinishedEvents = finishedEventList.stream().mapToInt(event -> event.getTotalParticipants()).sum();
		Integer totalFinishedEventsCreatedByThisUser = finishedEventList.size();

		// 8-b-ii-4
		List<Event> paidEventFinishedList = paidEventList.stream().filter(event -> event.getStatus() == EventStatus.FINISHED).collect(Collectors.toList());
		Integer totalPaidEventThatFinished = paidEventFinishedList.size();
		Integer totalRevenue = paidEventFinishedList.stream().mapToInt(event -> (int) (event.getTotalParticipants() * event.getFee())).sum();

		OrganizerReport organizerReport = new OrganizerReport();
		organizerReport.setTotalRevenue(totalRevenue);
		organizerReport.setTotalPaidEventThatFinished(totalPaidEventThatFinished);
		organizerReport.setTotalFinishedEventsCreatedByThisUser(totalFinishedEventsCreatedByThisUser);
		organizerReport.setTotalParticipantsInFinishedEvents(totalParticipantsInFinishedEvents);
		organizerReport.setTotalParticipationRequests(totalParticipationRequests);
		organizerReport.setTotalMinimumParticipantsRequiredInCancelledEvents(totalMinimumParticipantsRequired);
		organizerReport.setTotalCancelledEvents(totalCancelledEvents);
		organizerReport.setTotalPaidEvents(totalPaidEvents);
		organizerReport.setTotalEventsCreated(totalEventsCreated);

		UserReportResponse userReportResponse = new UserReportResponse();
		userReportResponse.setParticipationReport(participationReport);
		userReportResponse.setOrganizerReport(organizerReport);

		return new ResponseEntity(userReportResponse, HttpStatus.OK);
	}

}
