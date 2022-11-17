package com.cmpe275.term.controller;

import com.cmpe275.term.entity.ParticipantForum;
import com.cmpe275.term.entity.SignupForum;
import com.cmpe275.term.model.ForumMessageRequest;
import com.cmpe275.term.service.SignupForumService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/signup-forum")
public class SignupForumController {

    @Autowired
    SignupForumService signupForumService;


    @PostMapping
    public ResponseEntity<SignupForum> sendMessage(@RequestParam(required=false)MultipartFile file, @RequestParam(name = "userId") Long userId , @RequestParam(name = "eventId") Long eventId, @RequestParam(value = "sysDate") String sysDate, @RequestParam(value = "message", required = false) String message){
        ForumMessageRequest forumMessageRequest = new ForumMessageRequest();
        forumMessageRequest.setEventId(eventId);
        forumMessageRequest.setUserId(userId);
        forumMessageRequest.setSysDate(sysDate);
        forumMessageRequest.setMessage(message);
        return signupForumService.sendMessage(file, forumMessageRequest);
    }

//    @PostMapping
//    public ResponseEntity<SignupForum> sendMessage(@RequestBody ForumMessageRequest forumMessageRequest) throws Exception {
//        return signupForumService.sendMessage(forumMessageRequest);
//    }

    @GetMapping("/{eventId}")
    public ResponseEntity<List<SignupForum>> getMessages(@PathVariable("eventId") Long eventId ) throws Exception {
        return signupForumService.getMessage(eventId);
    }

}
