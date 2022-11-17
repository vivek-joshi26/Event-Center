package com.cmpe275.term.service;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.cmpe275.term.repository.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cmpe275.term.entity.Event;
import com.cmpe275.term.entity.Reviews;
import com.cmpe275.term.entity.User;
import com.cmpe275.term.enums.ReviewEnum;
import com.cmpe275.term.mapper.ReviewMapper;
import com.cmpe275.term.model.ParticipantReviewModel;
import com.cmpe275.term.model.ReviewRequest;
import com.cmpe275.term.model.ReviewResponseModel;
import com.cmpe275.term.repository.EventRepository;
import com.cmpe275.term.repository.ReviewRepository;
import com.cmpe275.term.repository.UserRepository;
import com.cmpe275.term.utility.DateUtility;

@Service
public class ReviewServiceImpl implements ReviewService {

	@Autowired
	private ReviewMapper reviewMapper;

	@Autowired
	private ReviewRepository reviewRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private DateUtility dateUtility;

	@Autowired
	private EventRepository eventRepository;

	@Autowired
	private EmailSenderService emailSenderService;

	@Autowired
	private ParticipantRepository participantRepository;

	@Override
	public ResponseEntity postReviewForParticipant(ReviewRequest request) {
		
		if(request.getReviewText()==null || request.getReviewText().isBlank()) {
			return new ResponseEntity("review cannot be empty" , HttpStatus.BAD_REQUEST);
		}
		if(request.getReviewText().length()>120) {
			return new ResponseEntity("review cannot exceed 120 characters" , HttpStatus.BAD_REQUEST);
		}
		Reviews checkReview = this.reviewRepository.findReviewsByReviewGivenByAndReviewGivenToAndEventIdAndReviewFor(request.getReviewGivenBy(),request.getReviewGivenTo(),request.getEventId(), ReviewEnum.PARTICIPANT);
		if(checkReview!=null) {
			return new ResponseEntity("cannot post more than one review to same participant for same event" , HttpStatus.BAD_REQUEST);
		}
		Event event = eventRepository.getById(request.getEventId());
		Date currentDate = null;
		try {
			currentDate = dateUtility.convertToDate(request.getSysDate());
		} catch (ParseException e) {
			
			return new ResponseEntity("some exception occured!" , HttpStatus.BAD_REQUEST);
		}
		if(currentDate.before(event.getStartDateTime())){
			return new ResponseEntity("cannot post review before event start date" , HttpStatus.BAD_REQUEST);
		}
		 Date endDate = event.getEndDateTime();
         int hours=endDate.getHours();
		 int min = endDate.getMinutes();
         LocalDate maxAllowedDate= event.getEndDateTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusWeeks(1);
         
         Date maxMsgAllowedDate = Date.from(maxAllowedDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
         maxMsgAllowedDate.setHours(hours);
         maxMsgAllowedDate.setMinutes(min);
         if(currentDate.after(maxMsgAllowedDate)) {
        	 return new ResponseEntity("cannot post review after one week of event end date" , HttpStatus.BAD_REQUEST);
         }
		Reviews review = this.reviewMapper.reviewRequestToEntity(request);
		review.setReviewFor(ReviewEnum.PARTICIPANT);
		reviewRepository.save(review);
		// How would this give the user, review given to is participant ID and not user id
		 User user = userRepository.findById(request.getReviewGivenTo()).get();

		//User user = participantRepository.getById(request.getReviewGivenTo()).getUser();

		if(user.getParticipantReputation()==null) {
			user.setParticipantReputation(request.getReviewStar());
		}
		else {
			List<Reviews> reviews = this.reviewRepository.findReviewsByReviewGivenToAndReviewFor(request.getReviewGivenTo(), ReviewEnum.PARTICIPANT);
			if(reviews.size()>0) {
				double sum=0.0;
				for(Reviews r :reviews) {
					sum = sum + r.getReviewStar();
				}
				double avg= sum / reviews.size();
				user.setParticipantReputation(avg);
			}
		}
		userRepository.save(user);

		emailSenderService.sendEmail(user.getEmail(), "You have received a review", "Review Received");


		return new ResponseEntity("successfully posted the review" , HttpStatus.OK);
		
		
	}

	@Override
	public ResponseEntity postReviewForOrganizer(ReviewRequest request){
		
		Event event = eventRepository.getById(request.getEventId());
		if(request.getReviewText()==null || request.getReviewText().isBlank()) {
			return new ResponseEntity("review cannot be empty" , HttpStatus.BAD_REQUEST);
		}
		if(request.getReviewText().length()>120) {
			return new ResponseEntity("review cannot exceed 120 characters" , HttpStatus.BAD_REQUEST);
		}
		Reviews checkReview = this.reviewRepository.findReviewsByReviewGivenByAndReviewGivenToAndEventIdAndReviewFor(request.getReviewGivenBy(),request.getReviewGivenTo(),request.getEventId(), ReviewEnum.ORGANIZER);
		if(checkReview!=null) {
			return new ResponseEntity("cannot post more than one review to same orgnaizer for same event" , HttpStatus.BAD_REQUEST);
		}
		Date currentDate = null;
		try {
			currentDate = dateUtility.convertToDate(request.getSysDate());
		} catch (ParseException e) {
			return new ResponseEntity("some exception occured!" , HttpStatus.BAD_REQUEST);
		}
		if(currentDate.before(event.getStartDateTime())){
			return new ResponseEntity("cannot post review before event start date" , HttpStatus.BAD_REQUEST);
		}
		 Date endDate = event.getEndDateTime();
         int hours=endDate.getHours();
		int min = endDate.getMinutes();
         LocalDate maxAllowedDate= event.getEndDateTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusWeeks(1);
         
         Date maxMsgAllowedDate = Date.from(maxAllowedDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
         maxMsgAllowedDate.setHours(hours);
         maxMsgAllowedDate.setMinutes(min);
         if(currentDate.after(maxMsgAllowedDate)) {
        	 return new ResponseEntity("cannot post review after one week of event end date" , HttpStatus.BAD_REQUEST);
         }
		Reviews review = this.reviewMapper.reviewRequestToEntity(request);
		review.setReviewFor(ReviewEnum.ORGANIZER);
		reviewRepository.save(review);
		User user = userRepository.findById(request.getReviewGivenTo()).get();
		if(user.getOrgnizerReputation()==null) {
			user.setOrgnizerReputation(request.getReviewStar());
		}
		else {
			List<Reviews> reviews = this.reviewRepository.findReviewsByReviewGivenToAndReviewFor(request.getReviewGivenTo(), ReviewEnum.ORGANIZER);
			if(reviews.size()>0) {
				double sum=0.0;
				for(Reviews r :reviews) {
					sum = sum + r.getReviewStar();
				}
				double avg= sum / reviews.size();
				user.setOrgnizerReputation(avg);
			}
		}
		userRepository.save(user);

		emailSenderService.sendEmail(user.getEmail(), "You have received a review", "Review Received");


		return new ResponseEntity("successfully posted the review" , HttpStatus.OK);
		
	}

	@Override
	public ResponseEntity getParticipantRepAndReviews(Long id)  {
		return this.getReviewsRecevied(id, ReviewEnum.PARTICIPANT);
	
	}

	@Override
	public ResponseEntity getMyReviewsAsParticipant(Long id)  {
		return this.getReviewsRecevied(id, ReviewEnum.PARTICIPANT);
		
	}

	@Override
	public ResponseEntity getMyReviewsAsOrganizer(Long id)  {
		List<Reviews> reviews = this.reviewRepository.findReviewsByReviewGivenToAndReviewFor(id, ReviewEnum.ORGANIZER);
		
		
		if(reviews.size()>0) {
			List<ReviewResponseModel> reviewResponse = new ArrayList<>();
			User user = userRepository.findById(id).get();
			Double avgRep = user.getOrgnizerReputation();
			for(Reviews review:reviews) {
				String reviewerName = userRepository.findById(review.getReviewGivenBy()).get().getScreenName();
				String eventName = eventRepository.findById(review.getEventId()).get().getTitle();
				ReviewResponseModel response = new ReviewResponseModel(reviewerName,eventName,review.getReviewStar(),review.getReviewText());
				reviewResponse.add(response);
			}
			
			return new ResponseEntity(new ParticipantReviewModel(avgRep,reviewResponse), HttpStatus.OK);
		}
		
		return new ResponseEntity("no reviews found", HttpStatus.OK);
	}

	@Override
	public ResponseEntity getMyReviewsGivenAsParticipant(Long id)  {
		return this.getReviewsGiven(id,ReviewEnum.ORGANIZER);
	}

	@Override
	public ResponseEntity getMyReviewsGivenAsOrganizer(Long id) {
		List<Reviews> reviews = this.reviewRepository.findReviewsByReviewGivenByAndReviewFor(id,ReviewEnum.PARTICIPANT);
		
		
		if(reviews.size()>0) {
			List<ReviewResponseModel> reviewResponse = new ArrayList<>();
			User user = userRepository.findById(id).get();
			Double avgRep = user.getOrgnizerReputation();
			for(Reviews review:reviews) {
				String reviewerName = userRepository.findById(review.getReviewGivenBy()).get().getScreenName();
				String eventName = eventRepository.findById(review.getEventId()).get().getTitle();
				ReviewResponseModel response = new ReviewResponseModel(reviewerName,eventName,review.getReviewStar(),review.getReviewText());
				reviewResponse.add(response);
			}
			
			return new ResponseEntity(new ParticipantReviewModel(avgRep,reviewResponse), HttpStatus.OK);
		}
		
		return new ResponseEntity("no reviews found", HttpStatus.OK);
	}
	
	public ResponseEntity getReviewsRecevied(Long id,ReviewEnum reviewFor) {
		// You are taking participant id from UI and finding reviews based on participant id, but in db you have by User id

		//Long userId = participantRepository.getById(id).getUser().getId();
		List<Reviews> reviews = this.reviewRepository.findReviewsByReviewGivenToAndReviewFor(id, reviewFor);
		
		
		if(reviews.size()>0) {
			List<ReviewResponseModel> reviewResponse = new ArrayList<>();
			User user = userRepository.findById(id).get();
			Double avgRep = user.getParticipantReputation();
			for(Reviews review:reviews) {
				String reviewerName = userRepository.findById(review.getReviewGivenBy()).get().getScreenName();
				String eventName = eventRepository.findById(review.getEventId()).get().getTitle();
				ReviewResponseModel response = new ReviewResponseModel(reviewerName,eventName,review.getReviewStar(),review.getReviewText());
				reviewResponse.add(response);
			}
			
			return new ResponseEntity(new ParticipantReviewModel(avgRep,reviewResponse), HttpStatus.OK);
		}
		
		return new ResponseEntity("no reviews found", HttpStatus.OK);
	}
	
	public ResponseEntity getReviewsGiven(Long id,ReviewEnum reviewFor) {
		List<Reviews> reviews = this.reviewRepository.findReviewsByReviewGivenByAndReviewFor(id,reviewFor);
		
		
		if(reviews.size()>0) {
			List<ReviewResponseModel> reviewResponse = new ArrayList<>();
			User user = userRepository.findById(id).get();
			Double avgRep = user.getParticipantReputation();
			for(Reviews review:reviews) {
				String reviewerName = userRepository.findById(review.getReviewGivenBy()).get().getScreenName();
				String eventName = eventRepository.findById(review.getEventId()).get().getTitle();
				ReviewResponseModel response = new ReviewResponseModel(reviewerName,eventName,review.getReviewStar(),review.getReviewText());
				reviewResponse.add(response);
			}
			
			return new ResponseEntity(new ParticipantReviewModel(avgRep,reviewResponse), HttpStatus.OK);
		}
		
		return new ResponseEntity("no reviews found", HttpStatus.OK);
	}
	
	
	

}
