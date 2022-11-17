package com.cmpe275.term.controller;

import com.cmpe275.term.model.EventFilterRequest;
import com.cmpe275.term.model.EventFilterResponse;
import com.cmpe275.term.model.ReportRequest;
import com.cmpe275.term.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/report")
public class ReportingController {

    @Autowired
    EventService eventService;

    @PostMapping("/system")
    public ResponseEntity getSystemReport(@RequestBody ReportRequest reportRequest) throws ParseException {
        // System.out.println(request.getStartDateTime());
        return this.eventService.getSystemReport(reportRequest);
    }

    @PostMapping("/user")
    public ResponseEntity getUserReport(@RequestBody ReportRequest reportRequest) throws ParseException {
        // System.out.println(request.getStartDateTime());
        return this.eventService.getUserReport(reportRequest);
    }


}
