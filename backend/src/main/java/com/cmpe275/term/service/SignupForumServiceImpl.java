package com.cmpe275.term.service;

import com.cmpe275.term.entity.*;
import com.cmpe275.term.mapper.ForumMessageMapper;
import com.cmpe275.term.model.ForumMessageRequest;
import com.cmpe275.term.model.ForumMessageResponse;
import com.cmpe275.term.repository.EventRepository;
import com.cmpe275.term.repository.ParticipantRepository;
import com.cmpe275.term.repository.SignupForumRepository;
import com.cmpe275.term.repository.UserRepository;
import com.cmpe275.term.utility.DateUtility;
import com.cmpe275.term.utility.S3Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SignupForumServiceImpl implements SignupForumService {

    @Autowired
    private SignupForumRepository signupForumRepository;

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
        try{
            Event event = eventRepository.getById(forumMessageRequest.getEventId());
            User user = userRepository.getById(forumMessageRequest.getUserId());
            
            Date currentDate = this.dateUtility.convertToDate(forumMessageRequest.getSysDate());
            Date deadline = event.getDeadline();
            if(currentDate.after(deadline)) {
            	 return new ResponseEntity("Cannot post messages in sign up forum once deadline is passed", HttpStatus.BAD_REQUEST);
            }

            // Place checks to see if user is allowed to send msges
//            Participant participant = participantRepository.findParticipantByEventAndUserAndApprovalStatus(event, user, ApprovalStatus.APPROVED);
//            if(participant == null && user != event.getUser())
//                return new ResponseEntity("Only participants and the organizer can view and post in the participant forum" , HttpStatus.BAD_REQUEST);


            if(!event.getSignupForumStatus().equals(EventStatus.OPEN)) {
                return new ResponseEntity("Closed for posting of new messages", HttpStatus.BAD_REQUEST);
            }

            // Place a check to see if sysdate is after deadline date, then update signupForumStatus to Closed

            String url;
            SignupForum savedMessage = null;
            Date msgDate = dateUtility.convertToDate(forumMessageRequest.getSysDate());

            SignupForum signupForum = new SignupForum();
            signupForum.setMessage(forumMessageRequest.getMessage());
            signupForum.setEvent(event);
            signupForum.setUser(user);
            signupForum.setSentBy(user.getScreenName());
            // signupForum.setStatus(event.getStatus().toString());
            signupForum.setMessageDateTime(msgDate);
            signupForum.setImageURL(forumMessageRequest.getImageURL());
            if(user == event.getUser())
                signupForum.setEventOwner(true);
            savedMessage = signupForumRepository.save(signupForum);



            if(multipartFile != null) {

                String folderName = String.format("signup-%s", savedMessage.getId());
                s3Utils.uploadFile("cmpe275cec", folderName, multipartFile.getInputStream());
                url = s3Utils.getFileURL("cmpe275cec", folderName);
                savedMessage.setImageURL(url);
                savedMessage = signupForumRepository.save(savedMessage);
                System.out.println("Saved msg inside image: " + savedMessage.toString());
            }
            ForumMessageResponse temp = forumMessageMapper.entityToSignupForumMessageResponse(savedMessage);

            User eventCreator = event.getUser();
            emailSenderService.sendEmail(eventCreator.getEmail(), "New message is posted in the Signup Forum of event- " + event.getTitle(), "New Message Received");


            return new ResponseEntity(temp, HttpStatus.OK);
        }
		catch (Exception exception){
			exception.printStackTrace();
			System.out.println("Exception in RoomEntry uploadImage(MultipartFile file, Long id), msg :  " );
			return null;
		}
    }

    @Override
    public ResponseEntity getMessage(Long eventId) throws Exception {
        Event event = eventRepository.getById(eventId);
//        List<SignupForum> signupForumMessages = signupForumRepository.findByEvent(event);
//        return new ResponseEntity(signupForumMessages , HttpStatus.OK);

        //response.setDeadline(this.dateUtility.convertDateToString(event.getDeadline()));

        List<SignupForum> signupForumMessages = event.getSignupForumMessages();
        List<ForumMessageResponse> response = new ArrayList<>();
        for (SignupForum request: signupForumMessages
             ) {
            ForumMessageResponse temp = forumMessageMapper.entityToSignupForumMessageResponse(request);
            response.add(temp);
        }

        return new ResponseEntity(response , HttpStatus.OK);
    }


}