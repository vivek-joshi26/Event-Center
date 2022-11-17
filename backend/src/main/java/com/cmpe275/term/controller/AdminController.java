package com.cmpe275.term.controller;

import com.cmpe275.term.model.EventRequest;
import com.cmpe275.term.model.MimicTime;
import com.cmpe275.term.utility.scheduler.UpdateEventStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AdminController {

    @Autowired
    UpdateEventStatus updateEventStatus;

    @Autowired
    MimicTime time;

    @PostMapping("/update-time")
    public ResponseEntity createEvent(@RequestBody MimicTime mimicTime) throws Exception {
        System.out.println("System Date Received From FrontEnd: " + mimicTime.getSysDate());
        time.setSysDate(mimicTime.getSysDate());
        time.setMode(true);
        updateEventStatus.updateEventStatusCancelled(mimicTime.getSysDate());
        Thread.sleep(300);
        updateEventStatus.updateEventStatusClosed(mimicTime.getSysDate());
        Thread.sleep(300);
        updateEventStatus.updateEventStatusOngoing(mimicTime.getSysDate());
        Thread.sleep(300);
        updateEventStatus.updateEventStatusFinished(mimicTime.getSysDate());
        Thread.sleep(300);
        updateEventStatus.updateParticipantForumStatusFINISHED(mimicTime.getSysDate());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/update-time")
    public ResponseEntity resetTime() throws Exception {
        time.setMode(false);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
