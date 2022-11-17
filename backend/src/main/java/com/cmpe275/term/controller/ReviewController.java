package com.cmpe275.term.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cmpe275.term.entity.Reviews;
import com.cmpe275.term.enums.ReviewEnum;
import com.cmpe275.term.model.ReviewRequest;
import com.cmpe275.term.repository.ReviewRepository;
import com.cmpe275.term.service.ReviewService;

@RestController
public class ReviewController {
	
	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private ReviewRepository repo;
	
	@PostMapping("/reviewForParticipants")
    public ResponseEntity postReviewForParticipants(@RequestBody ReviewRequest request)  {
        return reviewService.postReviewForParticipant(request);
    }
	
	@PostMapping("/reviewForOrganizer")
    public ResponseEntity postReviewForOrganizer(@RequestBody ReviewRequest request)  {
        return reviewService.postReviewForOrganizer(request);
    }
	
	@GetMapping("/getParticipantRepAndReviews")
    public ResponseEntity getParticipantRepAndReviews(@RequestParam Long id) {
        return reviewService.getParticipantRepAndReviews(id);
    }
	
	@GetMapping("/getMyReviewsAsParticipant")
    public ResponseEntity getMyReviewsAsParticipant(@RequestParam Long id) {
        return reviewService.getMyReviewsAsParticipant(id);
    }
	
	@GetMapping("/getMyReviewsAsOrganizer")
    public ResponseEntity getMyReviewsAsOrganizer(@RequestParam Long id) {
        return reviewService.getMyReviewsAsOrganizer(id);
    }
	
	@GetMapping("/getMyReviewsGivenAsParticipant")
    public ResponseEntity getMyReviewsGivenAsParticipant(@RequestParam Long id) {
        return reviewService.getMyReviewsGivenAsParticipant(id);
    }
	
	@GetMapping("/getMyReviewsGivenAsOrganizer")
    public ResponseEntity getMyReviewsGivenAsOrganizer(@RequestParam Long id) {
        return reviewService.getMyReviewsGivenAsOrganizer(id);
    }
	


}
