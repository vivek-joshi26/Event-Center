package com.cmpe275.term.controller;

import com.cmpe275.term.entity.ParticipantForum;
import com.cmpe275.term.entity.SignupForum;
import com.cmpe275.term.model.ForumMessageRequest;
import com.cmpe275.term.service.ParticipantForumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/participant-forum")
public class ParticipantForumController {

	@Autowired
	ParticipantForumService participantForumService;


//	@PostMapping
//	public ResponseEntity<SignupForum> sendMessage(@RequestBody MultipartFile file, @RequestHeader(value = "userId", defaultValue = "") Long userId, @RequestHeader(value = "eventId", defaultValue = "") Long eventId, @RequestHeader(value = "sysDate", defaultValue = "") String sysDate, @RequestHeader(value = "message", defaultValue = "") String message){
//		ForumMessageRequest forumMessageRequest = new ForumMessageRequest();
//		forumMessageRequest.setEventId(eventId);
//		forumMessageRequest.setUserId(userId);
//		forumMessageRequest.setSysDate(sysDate);
//		forumMessageRequest.setMessage(message);
//		return participantForumService.sendMessage(file, forumMessageRequest);
//	}

	@RequestMapping( method=RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<SignupForum> sendMessage(@RequestParam(required=false)MultipartFile file, @RequestParam(name = "userId") Long userId , @RequestParam(name = "eventId") Long eventId, @RequestParam(value = "sysDate") String sysDate, @RequestParam(value = "message", required = false) String message){
		ForumMessageRequest forumMessageRequest = new ForumMessageRequest();
		forumMessageRequest.setEventId(eventId);
		forumMessageRequest.setUserId(userId);
		forumMessageRequest.setSysDate(sysDate);
		forumMessageRequest.setMessage(message);
		System.out.println(forumMessageRequest);
		return participantForumService.sendMessage(file, forumMessageRequest);
	}

	@GetMapping("/{eventId}/{userId}")
	public ResponseEntity<List<SignupForum>> getMessages(@PathVariable("eventId") Long eventId, @PathVariable("userId") Long userId ) throws Exception {
		return participantForumService.getMessage(eventId, userId);
	}
}
