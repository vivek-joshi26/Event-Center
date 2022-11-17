package com.cmpe275.term.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cmpe275.term.entity.ApprovalStatus;
import com.cmpe275.term.entity.Event;
import com.cmpe275.term.entity.Participant;
import com.cmpe275.term.entity.ParticipantForum;
import com.cmpe275.term.entity.ParticipantForumStatus;
import com.cmpe275.term.entity.User;
import com.cmpe275.term.mapper.ForumMessageMapper;
import com.cmpe275.term.model.ForumMessageRequest;
import com.cmpe275.term.model.ForumMessageResponse;
import com.cmpe275.term.repository.EventRepository;
import com.cmpe275.term.repository.ParticipantForumRepository;
import com.cmpe275.term.repository.ParticipantRepository;
import com.cmpe275.term.repository.UserRepository;
import com.cmpe275.term.utility.DateUtility;
import com.cmpe275.term.utility.S3Utils;

@Service
public class ParticipantForumServiceImpl implements ParticipantForumService{


    @Autowired
    private ParticipantForumRepository participantForumRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private DateUtility dateUtility;

    @Autowired
    S3Utils s3Utils;

    @Autowired
    ParticipantRepository participantRepository;

    @Autowired
    ForumMessageMapper forumMessageMapper;

    @Autowired
    private EmailSenderService emailSenderService;


    @Override
    public ResponseEntity sendMessage(MultipartFile multipartFile, ForumMessageRequest forumMessageRequest) {

        /*
             i. Only participants and the organizer can view and post in the participant forum.
           ************** ii. It is open for posting until 72 hours after the end of the event, or anytime the organizer manually closes the participant forum after the event finishes.
            iii. A closed participant forum is still readable to the participants and organizer.
            iv. The rendering allowed for participant forums should be similar to the sign-up forums. Mixing of text and images is allowed for participant forums too.
        c. If a forum is closed, its status should be clearly indicated in the rendering, and there should be a brief text description for the reason that the forum has been closed,
         e.g., canceled because of not having enough participants, or the event has finished, or the organizer has closed this participant forum.
         */


        try{




            Event event = eventRepository.getById(forumMessageRequest.getEventId());
            User user = userRepository.getById(forumMessageRequest.getUserId());

            Date currentDate = dateUtility.convertToDate(forumMessageRequest.getSysDate());
            Date deadline = event.getDeadline();
            if(currentDate.before(deadline)) {
              	 return new ResponseEntity("Cannot post messages in participant forum before the deadline of event" ,HttpStatus.BAD_REQUEST);
              }
            
            Date endDate = event.getEndDateTime();
            int hours=endDate.getHours();
			int min = endDate.getMinutes();
            LocalDate maxAllowedDate= event.getEndDateTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(3);
            
            Date maxMsgAllowedDate = Date.from(maxAllowedDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            maxMsgAllowedDate.setHours(hours);
            maxMsgAllowedDate.setMinutes(min);
            if(currentDate.after(maxMsgAllowedDate)) {
             	 return new ResponseEntity("Cannot post messages in participant forum after 72 hours of ending the event." ,HttpStatus.BAD_REQUEST);
             }

            if(event.getParticipantForumStatus().equals(ParticipantForumStatus.CLOSED_TILL_REGISTRATION_DEADLINE))
                return new ResponseEntity("This forum will open once the event registration deadline passes" , HttpStatus.BAD_REQUEST);


            Participant participant = participantRepository.findParticipantByEventAndUserAndApprovalStatus(event, user, ApprovalStatus.APPROVED);
            if(participant == null && user != event.getUser())
                return new ResponseEntity("Only participants and the organizer can view and post in the participant forum" , HttpStatus.BAD_REQUEST);

            if(participant != null && !participant.getApprovalStatus().equals(ApprovalStatus.APPROVED))
                return new ResponseEntity("Your registration for this event has not been approved, you can view and post in the participant forum once your request gets approved" , HttpStatus.BAD_REQUEST);

            if(event.getParticipantForumStatus().equals(ParticipantForumStatus.CANCELLED_NOT_ENOUGH_PARTICIPANTS))
                return new ResponseEntity("Event has been cancelled because not enough participants registered" , HttpStatus.BAD_REQUEST);

            if(event.getParticipantForumStatus().equals(ParticipantForumStatus.CLOSED_BY_ORGANIZER))
                return new ResponseEntity("Participant Forum has been closed by the event owner" , HttpStatus.BAD_REQUEST);

            if(event.getParticipantForumStatus().equals(ParticipantForumStatus.FINISHED))
                return new ResponseEntity("You can not post new messages in this participant Forum, because the event has finished" , HttpStatus.BAD_REQUEST);


            String url;
            ParticipantForum savedMessage = null;

            Date msgDate = dateUtility.convertToDate(forumMessageRequest.getSysDate());

            ParticipantForum participantForum = new ParticipantForum();
            participantForum.setMessage(forumMessageRequest.getMessage());
            participantForum.setEvent(event);
            participantForum.setUser(user);
            participantForum.setSentBy(user.getScreenName());
            //participantForum.setStatus(event.getStatus().toString());
            participantForum.setMessageDateTime(msgDate);
            participantForum.setImageURL(forumMessageRequest.getImageURL());
            if(user == event.getUser())
                participantForum.setEventOwner(true);
            savedMessage = participantForumRepository.save(participantForum);


            if(multipartFile != null) {

                String folderName = String.format("participantforum-%s", savedMessage.getId());
                s3Utils.uploadFile("cmpe275cec", folderName, multipartFile.getInputStream());
                url = s3Utils.getFileURL("cmpe275cec", folderName);
                savedMessage.setImageURL(url);
                savedMessage = participantForumRepository.save(savedMessage);

            }
            ForumMessageResponse temp = forumMessageMapper.entityToParticipantForumMessageResponse(savedMessage);

            User eventCreator = event.getUser();
            emailSenderService.sendEmail(eventCreator.getEmail(), "New message is posted in the Participant Forum of event- " + event.getTitle(), "New Message Received");

            return new ResponseEntity(temp, HttpStatus.OK);
            //return new ResponseEntity(savedMessage, HttpStatus.OK);
        }
        catch (Exception exception){
            exception.printStackTrace();
            System.out.println("Exception in  sendMessage(MultipartFile multipartFile, ForumMessageRequest forumMessageRequest), :  " );
            return null;
        }
    }


    @Override
    public ResponseEntity getMessage(Long eventId, Long userId) throws Exception {
        Event event = eventRepository.getById(eventId);
//        List<ParticipantForum> participantForumMessages = participantForumRepository.findByEvent(event);
//        return new ResponseEntity(participantForumMessages , HttpStatus.OK);
        User user = userRepository.getById(userId);
        Participant participant = participantRepository.findParticipantByEventAndUserAndApprovalStatus(event, user, ApprovalStatus.APPROVED);

        if(participant == null && user != event.getUser())
            return new ResponseEntity("Only participants and the organizer can view and post in the participant forum" , HttpStatus.BAD_REQUEST);

        if(participant != null && !participant.getApprovalStatus().equals(ApprovalStatus.APPROVED))
            return new ResponseEntity("Your registration for this event has not been approved, you can view and post in the participant forum once your request gets approved" , HttpStatus.BAD_REQUEST);

        List<ParticipantForum> participantForumsMessages = event.getParticipantForumMessages();
        List<ForumMessageResponse> response = new ArrayList<>();
        for (ParticipantForum request: participantForumsMessages
        ) {
            ForumMessageResponse temp = forumMessageMapper.entityToParticipantForumMessageResponse(request);
            response.add(temp);
        }

        return new ResponseEntity(response , HttpStatus.OK);

        //return new ResponseEntity(event.getParticipantForumMessages() , HttpStatus.OK);
    }


}
