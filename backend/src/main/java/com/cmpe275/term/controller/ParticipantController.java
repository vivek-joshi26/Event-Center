package com.cmpe275.term.controller;

import com.cmpe275.term.entity.ApprovalStatus;
import com.cmpe275.term.model.EventRequest;
import com.cmpe275.term.model.ParticipantApprovalRequest;
import com.cmpe275.term.model.ParticipateRequest;
import com.cmpe275.term.service.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ParticipantController {

    @Autowired
    ParticipantService participantService;

    @PostMapping("/participate")
    public ResponseEntity createEvent(@RequestBody ParticipateRequest participateRequest) throws Exception {
        return participantService.registerParticipant(participateRequest);
    }

    @PostMapping("/participate/update-status")
    public ResponseEntity updateApprovalStatus(@RequestBody ParticipantApprovalRequest participantApprovalRequest) throws Exception {
        return participantService.updateApprovalStatus(participantApprovalRequest);
    }

    @GetMapping("/participate/status/{eventId}")
    public ResponseEntity updateApprovalStatus(@PathVariable("eventId") Long eventId, @RequestParam(required = false) ApprovalStatus approvalStatus ) throws Exception {
        return participantService.findPendingApproval(eventId, approvalStatus);
    }

}
