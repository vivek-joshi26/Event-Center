package com.cmpe275.term.specification;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.cmpe275.term.entity.Event;
import com.cmpe275.term.entity.EventStatus;
import com.cmpe275.term.model.EventFilterRequest;
import com.cmpe275.term.utility.DateUtility;

@Component
public class EventSpecification {
	@Autowired
	private DateUtility dateUtility;
	
	public Specification<Event> getEventByFilter(EventFilterRequest request){
		 return (root, query, criteriaBuilder) -> {
	            List<Predicate> predicates = new ArrayList<>();
	            if (request.getCity() != null) {
	            	predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("address").get("city")), request.getCity().toLowerCase()));
	            }
	            if (request.getKeyword() != null) {
	            	Predicate title = criteriaBuilder.like(criteriaBuilder.lower(root.get("title")),
	                        "%" + request.getKeyword().toLowerCase() + "%");
	            	
	            	Predicate description = criteriaBuilder.like(criteriaBuilder.lower(root.get("description")),
	                        "%" + request.getKeyword().toLowerCase() + "%");
	            	predicates.add(criteriaBuilder.or(title,description));
	            }
	            
	            if (request.getScreenName() != null) {
	            	predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("user").get("screenName")),
	                        "%" + request.getScreenName().toLowerCase() + "%"));
	            	
	            }
	            if (request.getStatus() != null) {
	            	if(request.getStatus().equalsIgnoreCase("ACTIVE")) {
	            		predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("status").as(String.class)), "OPEN"));
	            	}
	            	
	            	else if(request.getStatus().equalsIgnoreCase("OPEN")) {
	            		Predicate open = criteriaBuilder.equal(criteriaBuilder.lower(root.get("status").as(String.class)), "OPEN");
	            		Predicate close = criteriaBuilder.equal(criteriaBuilder.lower(root.get("status").as(String.class)), "CLOSED");
	            		predicates.add(criteriaBuilder.or(open,close));
	            		
	            	}
	            	else if(request.getStatus().equalsIgnoreCase("ALL")) {
	            		Expression<String> exp = root.get("status");
	            		List<EventStatus> statuses = Arrays.asList(EventStatus.values());
	            		Predicate pred = exp.in(statuses);
	            		predicates.add(pred);
	            	}
	            	
	            	
	            }
	            if(request.getStartDateTime() != null && !request.getStartDateTime().equals("Invalid date") && request.getEndDateTime()==null) {
	            	try {
						predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("startDateTime"), this.dateUtility.convertToDate(request.getStartDateTime())));
					} catch (ParseException e) {
						// NEED TO ADD RESPONSE ENTITY.
						e.printStackTrace();
					}
	            }
	            
	            if(request.getStartDateTime() != null && request.getEndDateTime()!=null) {
	            	try {
						Predicate start = criteriaBuilder.greaterThanOrEqualTo(root.get("startDateTime"), this.dateUtility.convertToDate(request.getStartDateTime()));
						Predicate end = criteriaBuilder.lessThanOrEqualTo(root.get("endDateTime"), this.dateUtility.convertToDate(request.getEndDateTime()));
						predicates.add(criteriaBuilder.and(start,end));
					} catch (ParseException e) {
						 // NEED TO ADD RESPONSE ENTITY.
						e.printStackTrace();
					}
	            }
	            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
	        };
	}


	// when minimum participants are not registered, event will be cancelled
	public Specification<Event> getEventForPassedDeadline(String systemDate){
		return (root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();

			Predicate open = criteriaBuilder.equal(criteriaBuilder.lower(root.get("status").as(String.class)), "OPEN");
			predicates.add(open);
			try {
				Predicate start = criteriaBuilder.lessThan(root.get("deadline"), this.dateUtility.convertToDate(systemDate));
				predicates.add(start);
				Predicate minParticipants = criteriaBuilder.lessThan(root.get("totalParticipants") ,root.get("minParticipants"));
				predicates.add(minParticipants);
			} catch (Exception e) {
				e.printStackTrace();
			}

			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		};
	}



	// when minimum participants are registered, event will be closed, signupForum will be closed, participantForum will be opened
	public Specification<Event> getEventForPassedDeadlineAndMinimumParticipantsRegistered(String systemDate){
		return (root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();

			Predicate open = criteriaBuilder.equal(criteriaBuilder.lower(root.get("status").as(String.class)), "OPEN");
			predicates.add(open);
			try {
				Predicate start = criteriaBuilder.lessThan(root.get("deadline"), this.dateUtility.convertToDate(systemDate));
				predicates.add(start);
				Predicate minParticipants = criteriaBuilder.greaterThanOrEqualTo(root.get("totalParticipants") ,root.get("minParticipants"));
				predicates.add(minParticipants);
			} catch (Exception e) {
				e.printStackTrace();
			}

			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		};
	}


	// when event is in closed state and starttime has passed, event will be moved to ongoing state
	public Specification<Event> getEventForClosedStateAndStartTimePassed(String systemDate){
		return (root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();

			Predicate open = criteriaBuilder.equal(criteriaBuilder.lower(root.get("status").as(String.class)), "CLOSED");
			predicates.add(open);
			try {
				Predicate start = criteriaBuilder.lessThan(root.get("startDateTime"), this.dateUtility.convertToDate(systemDate));
				predicates.add(start);
			} catch (Exception e) {
				e.printStackTrace();
			}

			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		};
	}


	// when event is in ONGOING state and endtime has passed, event will be moved to FINISHED state
	public Specification<Event> getEventForOngoingStateAndEndTimePassed(String systemDate){
		return (root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();

			Predicate open = criteriaBuilder.equal(criteriaBuilder.lower(root.get("status").as(String.class)), "ONGOING");
			predicates.add(open);
			try {
				Predicate start = criteriaBuilder.lessThan(root.get("endDateTime"), this.dateUtility.convertToDate(systemDate));
				predicates.add(start);
			} catch (Exception e) {
				e.printStackTrace();
			}

			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		};
	}

	// when event is in FINISHED state and endtime + 72 hrs has passed, paricipantForumStatus will be moved to FINISHED state
	public Specification<Event> getEventForFinishedStateAndEndTimePassedUpdateTheParticipantForumStatus(String systemDate){
		return (root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();

			Predicate open = criteriaBuilder.equal(criteriaBuilder.lower(root.get("status").as(String.class)), "FINISHED");
			predicates.add(open);
			try {
				Predicate start = criteriaBuilder.lessThan(root.get("endDateTime")  , this.dateUtility.convertToDate(systemDate));
				predicates.add(start);
			} catch (Exception e) {
				e.printStackTrace();
			}

			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		};
	}

}
