package com.cmpe275.term.utility.scheduler;


import com.cmpe275.term.entity.*;
import com.cmpe275.term.model.MimicTime;
import com.cmpe275.term.repository.EventRepository;
import com.cmpe275.term.repository.ParticipantRepository;
import com.cmpe275.term.service.EmailSenderService;
import com.cmpe275.term.specification.EventSpecification;
import com.cmpe275.term.utility.DateUtility;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class UpdateEventStatus {
    @Autowired
    private EventSpecification eventSpecs;


    @Autowired
    EventRepository eventRepository;

    @Autowired
    DateUtility dateUtility;

    @Autowired
    EmailSenderService emailSenderService;

    @Autowired
    ParticipantRepository participantRepository;

    @Autowired
    MimicTime mimicTime;


    @PostConstruct
    public void onStartup() throws ParseException, InterruptedException {
        cronJobSchedule();
    }

    @Scheduled(cron = "0 0/5 * * * *")      // change 0/5 to run every 5 mins
    public void cronJobSchedule() throws ParseException, InterruptedException {

        String strDate = "";
        if(!mimicTime.isMode()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date now = new Date();
            strDate = sdf.format(now);
        }else {
            strDate = mimicTime.getSysDate();
            Date date = dateUtility.convertToDate(strDate);
            Date newDate = DateUtils.addMinutes(date, 5);       // change amount to 5 if cron is scheduled to run every 5 mins
            strDate = dateUtility.convertDateToString(newDate);
            mimicTime.setSysDate(strDate);
        }
        System.out.println("************************ Running cron Schedule ********************************** " + strDate);
      updateEventStatusCancelled(strDate);
      Thread.sleep(300);
      updateEventStatusClosed(strDate);
        Thread.sleep(300);
      updateEventStatusOngoing(strDate);
        Thread.sleep(300);
      updateEventStatusFinished(strDate);
        Thread.sleep(300);
      updateParticipantForumStatusFINISHED(strDate);
    }


    // check for events whose deadline has passed and status is open and minimum participants has not reached
    // update signupForum to be CANCELLED
    public void updateEventStatusCancelled(String sysDate){
        // String sysDate2 = "2021-06-20 13:00";
        System.out.println("************************ Inside Cancelled **************** " + sysDate);
        Specification<Event> specification = eventSpecs.getEventForPassedDeadline(sysDate);
        List<Event> eventList = eventRepository.findAll(specification);
        List<Event> updatedEventList = new ArrayList<>();
        for (Event event: eventList
             ) {
            System.out.println("Event ID: " + event.getId());
            event.setStatus(EventStatus.CANCELLED);
            event.setParticipantForumStatus(ParticipantForumStatus.CANCELLED_NOT_ENOUGH_PARTICIPANTS);
            event.setSignupForumStatus(EventStatus.CLOSED);
            updatedEventList.add(event);

            // need to add code, to send emails to participants informing them, that this event is cancelled, because not enough participants participated
            // also need to update approvalStatus to cancelled
            List<Participant> participants = event.getParticipants();
            List<String> emailListForApproved = new ArrayList<>();
            List<String> emailListForAll = new ArrayList<>();
            for (Participant singleParticipant:participants
                 ) {
                if(singleParticipant.getApprovalStatus().equals(ApprovalStatus.APPROVED)){
                    User user = singleParticipant.getUser();
                    emailListForApproved.add(user.getEmail());
                }else {
                    User user = singleParticipant.getUser();
                    emailListForAll.add(user.getEmail());
                }
                // to update the approval status to cancelled, as event was cancelled becuase of minimum participants were not there
                singleParticipant.setApprovalStatus(ApprovalStatus.CANCELLED);
            }
            if(emailListForApproved.size() > 0){
             sendEmail(emailListForApproved, "Event: " + event.getTitle() +", is cancelled, not enough participants registered, in case you were charged, the money will be sent back to the source account.", "Event cancelled");
            }

            if(emailListForAll.size() > 0){
                sendEmail(emailListForAll, "Event: " + event.getTitle() +", is cancelled, not enough participants registered.", "Event cancelled");
            }

            if(participants.size() > 0)
                 participantRepository.saveAll(participants);


        }
        if(updatedEventList.size() > 0)
            eventRepository.saveAll(updatedEventList);

    }


    public void updateEventStatusClosed(String sysDate){
        // String sysDate2 = "2021-06-20 13:00";
        System.out.println("************************ Inside Closed **************** " + sysDate);
        Specification<Event> specification = eventSpecs.getEventForPassedDeadlineAndMinimumParticipantsRegistered(sysDate);
        List<Event> eventList = eventRepository.findAll(specification);
        List<Event> updatedEventList = new ArrayList<>();
        for (Event event: eventList
        ) {
            System.out.println("Event ID: " + event.getId());
            event.setStatus(EventStatus.CLOSED);
            event.setParticipantForumStatus(ParticipantForumStatus.OPEN);
            event.setSignupForumStatus(EventStatus.CLOSED);
            updatedEventList.add(event);

            List<Participant> participants = event.getParticipants();
            for (Participant singleParticipant:participants
            ) {
                if(singleParticipant.getApprovalStatus().equals(ApprovalStatus.PENDING)) {
                    // to update the approval status to cancelled for pending requests, as event is closed now
                    singleParticipant.setApprovalStatus(ApprovalStatus.CANCELLED);
                }
            }
            if(participants.size() > 0)
                participantRepository.saveAll(participants);

        }
        if(updatedEventList.size() > 0)
              eventRepository.saveAll(updatedEventList);
    }


    public void updateEventStatusOngoing(String sysDate){
        // String sysDate2 = "2021-06-25 12:05";
        System.out.println("************************ Inside Ongoing **************** " + sysDate);
        Specification<Event> specification = eventSpecs.getEventForClosedStateAndStartTimePassed(sysDate);
        List<Event> eventList = eventRepository.findAll(specification);
        List<Event> updatedEventList = new ArrayList<>();
        for (Event event: eventList
        ) {
            System.out.println("Event ID: " + event.getId());
            event.setStatus(EventStatus.ONGOING);
            updatedEventList.add(event);
            List<Participant> participants = event.getParticipants();
            List<String> emailListForParticipants = new ArrayList<>();
            for (Participant singleParticipant : participants
            ) {
                if (singleParticipant.getApprovalStatus().equals(ApprovalStatus.APPROVED)) {
                    User user = singleParticipant.getUser();
                    emailListForParticipants.add(user.getEmail());
                }
            }
            if (emailListForParticipants.size() > 0) {
                sendEmail(emailListForParticipants, "Event: " + event.getTitle() + ", has started.", "Event started");
            }
        }

        if(updatedEventList.size() > 0)
            eventRepository.saveAll(updatedEventList);
    }

    public void updateEventStatusFinished(String sysDate){
        // String sysDate2 = "2021-06-26 14:05";
      //  System.out.println("****************************************************************** sysDate: " + sysDate);
        System.out.println("************************ Inside Finished **************** " + sysDate);
        Specification<Event> specification = eventSpecs.getEventForOngoingStateAndEndTimePassed(sysDate);
        List<Event> eventList = eventRepository.findAll(specification);
        List<Event> updatedEventList = new ArrayList<>();
        for (Event event: eventList
        ) {
            System.out.println("Event ID: " + event.getId());
            event.setStatus(EventStatus.FINISHED);
            updatedEventList.add(event);
        }
        if(updatedEventList.size() > 0)
            eventRepository.saveAll(updatedEventList);
    }


    public void updateParticipantForumStatusFINISHED(String sysDate) throws ParseException {
        // String sysDate2 = "2021-06-29 14:01";
        System.out.println("************************ Inside ParticipantForumStatusFINISHED **************** " + sysDate);
        Date date = dateUtility.convertToDate(sysDate);
        Date newDate = DateUtils.addHours(date, -72);
        String finalDate = dateUtility.convertDateToString(newDate);

     //   System.out.println("****************************************************************** finalDate: " + finalDate);
        Specification<Event> specification = eventSpecs.getEventForFinishedStateAndEndTimePassedUpdateTheParticipantForumStatus(finalDate);
        List<Event> eventList = eventRepository.findAll(specification);
        List<Event> updatedEventList = new ArrayList<>();
        for (Event event: eventList
        ) {
            // System.out.println("Event ID: " + event.getId());
            event.setParticipantForumStatus(ParticipantForumStatus.FINISHED);
            updatedEventList.add(event);
        }
        if(updatedEventList.size() > 0)
            eventRepository.saveAll(updatedEventList);
    }


    public void sendEmail(List<String> emailIdList, String body, String subject){
        for (String sendTo:emailIdList
             ) {
            emailSenderService.sendEmail(sendTo, body, subject);
        }

    }



}
