package com.cmpe275.term.service;

import java.text.ParseException;

import org.springframework.http.ResponseEntity;

import com.cmpe275.term.model.ReviewRequest;

public interface ReviewService {

	public ResponseEntity postReviewForParticipant(ReviewRequest request);
	public ResponseEntity postReviewForOrganizer(ReviewRequest request);
	public ResponseEntity getParticipantRepAndReviews(Long id);
	public ResponseEntity getMyReviewsAsParticipant(Long id);
	public ResponseEntity getMyReviewsAsOrganizer(Long id);
	public ResponseEntity getMyReviewsGivenAsParticipant(Long id);
	public ResponseEntity getMyReviewsGivenAsOrganizer(Long id);
}
