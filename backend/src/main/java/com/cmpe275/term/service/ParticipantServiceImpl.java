package com.cmpe275.term.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cmpe275.term.entity.ApprovalStatus;
import com.cmpe275.term.entity.Event;
import com.cmpe275.term.entity.EventStatus;
import com.cmpe275.term.entity.Participant;
import com.cmpe275.term.entity.Reviews;
import com.cmpe275.term.entity.User;
import com.cmpe275.term.enums.ReviewEnum;
import com.cmpe275.term.model.AttendeesResponse;
import com.cmpe275.term.model.ParticipantApprovalRequest;
import com.cmpe275.term.model.ParticipantReviewResponse;
import com.cmpe275.term.model.ParticipateRequest;
import com.cmpe275.term.repository.EventRepository;
import com.cmpe275.term.repository.ParticipantRepository;
import com.cmpe275.term.repository.ReviewRepository;
import com.cmpe275.term.repository.UserRepository;
import com.cmpe275.term.utility.DateUtility;

@Service
public class ParticipantServiceImpl implements ParticipantService{


    @Autowired
    ParticipantRepository participantRepository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    UserRepository userRepository;
    
    @Autowired
	private DateUtility dateUtility;
    
    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    private EmailSenderService emailSenderService;

    @Override
    public ResponseEntity registerParticipant(ParticipateRequest participateRequest) throws Exception {
        Event event = eventRepository.getById(participateRequest.getEventId());
        User user = userRepository.getById(participateRequest.getUserId());

        Participant existingParticipant = participantRepository.findParticipantByEventAndUser(event, user);
        System.out.println("existingParticipant: " + existingParticipant);

        // can add one more check to see if system date is after the deadline date, in that case user can't signup
        
        Date currentDate = this.dateUtility.convertToDate(participateRequest.getSysDate());
        Date deadline = event.getDeadline();
        if(currentDate.after(deadline)) {
			
			return new ResponseEntity("Cannot sign up to event as deadline is already passed" , HttpStatus.BAD_REQUEST);
		}
        // when user is already registered for that event
        if(existingParticipant != null)
            return new ResponseEntity("You have already signed up for this event" ,HttpStatus.BAD_REQUEST);

        if(user == event.getUser())
            return new ResponseEntity("You can not signup for your own event" ,HttpStatus.BAD_REQUEST);

        if(event.getStatus() != EventStatus.OPEN)
            // return can't sign you up as event is now closed or cancelled
            return new ResponseEntity("You can not signup for this event, this event has been closed or cancelled" ,HttpStatus.BAD_REQUEST);

        if(event.getMaxParticipants() == event.getTotalParticipants())
            // return can't sign you up as event is now full
            return new ResponseEntity("You can not signup for this event, the event is full" ,HttpStatus.BAD_REQUEST);


        if(user.getRole().equals("ORG"))
            // return can't sign you up as event is now full
            return new ResponseEntity("An organization cannot participate in events" ,HttpStatus.BAD_REQUEST);


        Participant participant = new Participant();
        participant.setEvent(event);
        participant.setUser(user);
        participant.setParticipantName(user.getScreenName());


        if(event.getAdmissionPolicy().equals("first-come-first-served")){
                participant.setApprovalStatus(ApprovalStatus.APPROVED);
                event.setTotalParticipants(event.getTotalParticipants() + 1);
//                if(event.getTotalParticipants() == event.getMaxParticipants())
//                    event.setStatus(EventStatus.CLOSED);
                eventRepository.save(event);
                emailSenderService.sendEmail(user.getEmail(), "You have signed up for an event with Title: " + event.getTitle(), "Event Signup received");
                emailSenderService.sendEmail(user.getEmail(), "Your signup request for the event- " + event.getTitle() + ", has been approved.", "Signup for the event is approved");
        } else
            participant.setApprovalStatus(ApprovalStatus.PENDING);

        participant.setCreationDateTime(currentDate);
        participantRepository.save(participant);
        System.out.println(participant);
        emailSenderService.sendEmail(user.getEmail(), "You have signed up for an event with Title: " + event.getTitle(), "Event Signup received");
        return new ResponseEntity<>(participant, HttpStatus.OK);
    }

    @Override
    public ResponseEntity updateApprovalStatus(ParticipantApprovalRequest request) throws Exception {
        Participant participant = participantRepository.findById(request.getParticipantId()).get();


        Event event = participant.getEvent();

        Date currentDate = this.dateUtility.convertToDate(request.getSysDate());
        Date deadline = event.getDeadline();
        if(currentDate.after(deadline)) {
			
			return new ResponseEntity("Cannot approve/reject as deadline is already passed" , HttpStatus.BAD_REQUEST);
		}
        if(request.getApprovalStatus() == ApprovalStatus.CANCELLED || request.getApprovalStatus() == ApprovalStatus.REJECTED){
            participant.setApprovalStatus(request.getApprovalStatus());
            participantRepository.save(participant);
            User user = participant.getUser();
            emailSenderService.sendEmail(user.getEmail(), "Your signup request for the event- " + event.getTitle() + ", has been rejected.", "Signup for the event is rejected");

        }
        else if(request.getApprovalStatus() == ApprovalStatus.PENDING){
            participant.setApprovalStatus(request.getApprovalStatus());
            participantRepository.save(participant);
        }
        else {

            if (event.getStatus() != EventStatus.OPEN) {
                // return can't sign this user as the event is now closed or cancelled
                participant.setApprovalStatus(ApprovalStatus.CANCELLED);
                participantRepository.save(participant);
                return new ResponseEntity("You can not signup this user for this event, this event has been " + event.getStatus(), HttpStatus.BAD_REQUEST);
            }

            if (event.getMaxParticipants() == event.getTotalParticipants()) {
                // return can't sign this user  as event is now full
                participant.setApprovalStatus(ApprovalStatus.CANCELLED);
                participantRepository.save(participant);
                return new ResponseEntity("You can not signup this user for this event, the event is full", HttpStatus.BAD_REQUEST);
            }

            if (event.getStatus() == EventStatus.OPEN && request.getApprovalStatus() == ApprovalStatus.APPROVED) {
                event.setTotalParticipants(event.getTotalParticipants() + 1);
//                if (event.getTotalParticipants() == event.getMaxParticipants())
//                    event.setStatus(EventStatus.CLOSED);
                eventRepository.save(event);
                participant.setApprovalStatus(request.getApprovalStatus());
            }

            participantRepository.save(participant);
            User user = participant.getUser();
            emailSenderService.sendEmail(user.getEmail(), "Your signup request for the event- " + event.getTitle() + ", has been approved.", "Signup for the event is approved");
        }
        return new ResponseEntity<>(participant, HttpStatus.OK);
    }


    // Provide Reviews response
    @Override
    public ResponseEntity findPendingApproval(Long eventId, ApprovalStatus approvalStatus) throws Exception {
        Event event = eventRepository.getById(eventId);
        String startDateTime = this.dateUtility.convertDateToString(event.getStartDateTime());
        String endDateTime = this.dateUtility.convertDateToString(event.getEndDateTime());
        List<Participant> participantList = new ArrayList<>();
        List<ParticipantReviewResponse> resList = new ArrayList<>();
        if(approvalStatus == null){
            //participantList = participantRepository.findParticipantByEvent(event);
            participantList = event.getParticipants();
            if(participantList.size()>0) {
            	for(Participant p:participantList) {
            		ParticipantReviewResponse res = new ParticipantReviewResponse();
            		Long userId = p.getUser().getId();
            		res.setId(p.getId());
            		res.setUserId(userId);
            		res.setApprovalStatus(p.getApprovalStatus());
            		res.setParticipantName(p.getParticipantName());
            		Reviews review = this.reviewRepository.findReviewsByReviewGivenByAndReviewGivenToAndEventIdAndReviewFor(event.getUser().getId(),userId,eventId,ReviewEnum.PARTICIPANT);
            		if(review!=null) {
            			res.setReviewStar(review.getReviewStar());
            			res.setReviewText(review.getReviewText());
            		}
            		resList.add(res);
            	}
            }
        }else {

            participantList  = participantRepository.findParticipantByEventAndApprovalStatus(event, approvalStatus);
            if(participantList.size()>0) {
            	for(Participant p:participantList) {
            		ParticipantReviewResponse res = new ParticipantReviewResponse();
            		Long userId = p.getUser().getId();
            		res.setId(p.getId());
            		res.setApprovalStatus(p.getApprovalStatus());
            		res.setParticipantName(p.getParticipantName());
            		Reviews review = this.reviewRepository.findReviewsByReviewGivenByAndReviewGivenToAndEventIdAndReviewFor(event.getUser().getId(),userId,eventId,ReviewEnum.PARTICIPANT);
            		if(review!=null) {
            			res.setReviewStar(review.getReviewStar());
            			res.setReviewText(review.getReviewText());
            		}
            		resList.add(res);
            	}
            }
        }

        System.out.println(participantList);
//        return new ResponseEntity<>(participantList, HttpStatus.OK);
        return new ResponseEntity<>(new AttendeesResponse(resList,startDateTime,endDateTime), HttpStatus.OK);
    }


}
