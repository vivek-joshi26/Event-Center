package com.cmpe275.term.service;

import com.cmpe275.term.model.ForumMessageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface SignupForumService {

    public ResponseEntity sendMessage(MultipartFile multipartFile,ForumMessageRequest forumMessageRequest);

    public ResponseEntity getMessage(Long eventId) throws Exception;

}
