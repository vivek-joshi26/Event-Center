package com.cmpe275.term.service;

import com.cmpe275.term.model.ForumMessageRequest;
import com.cmpe275.term.model.ParticipateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface ParticipantForumService {

    public ResponseEntity sendMessage(MultipartFile multipartFile,ForumMessageRequest forumMessageRequest);

    public ResponseEntity getMessage(Long eventId,  Long userId) throws Exception;

}
