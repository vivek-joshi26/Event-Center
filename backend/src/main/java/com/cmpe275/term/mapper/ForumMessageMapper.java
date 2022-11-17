package com.cmpe275.term.mapper;

import com.cmpe275.term.entity.Event;
import com.cmpe275.term.entity.ParticipantForum;
import com.cmpe275.term.entity.SignupForum;
import com.cmpe275.term.entity.User;
import com.cmpe275.term.model.EventFilterResponse;
import com.cmpe275.term.model.ForumMessageResponse;
import com.cmpe275.term.utility.DateUtility;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ForumMessageMapper {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private DateUtility dateUtility;

    public ForumMessageResponse entityToSignupForumMessageResponse(SignupForum signupForum) {
        ForumMessageResponse response = this.modelMapper.map(signupForum, ForumMessageResponse.class);
        response.setId(signupForum.getId());
        response.setSentBy(signupForum.getSentBy());
        response.setMessage(signupForum.getMessage());
        response.setEventOwner(signupForum.isEventOwner());
        response.setImageURL(signupForum.getImageURL());
        response.setMessageDateTime(this.dateUtility.convertDateToString(signupForum.getMessageDateTime()));
        return response;
    }

    public ForumMessageResponse entityToParticipantForumMessageResponse(ParticipantForum participantForum) {
        ForumMessageResponse response = this.modelMapper.map(participantForum, ForumMessageResponse.class);
        response.setId(participantForum.getId());
        response.setSentBy(participantForum.getSentBy());
        response.setMessage(participantForum.getMessage());
        response.setEventOwner(participantForum.isEventOwner());
        response.setImageURL(participantForum.getImageURL());
        response.setMessageDateTime(this.dateUtility.convertDateToString(participantForum.getMessageDateTime()));
        return response;
    }

}
