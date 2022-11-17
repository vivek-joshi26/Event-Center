package com.cmpe275.term.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cmpe275.term.entity.Reviews;
import com.cmpe275.term.enums.ReviewEnum;

@Repository
public interface ReviewRepository extends JpaRepository<Reviews,Long> {
	
	List<Reviews> findReviewsByReviewGivenToAndReviewFor(Long id, ReviewEnum reviewFor);
	List<Reviews> findReviewsByReviewGivenByAndReviewFor(Long id, ReviewEnum reviewFor);
	Reviews findReviewsByReviewGivenByAndReviewGivenToAndEventIdAndReviewFor(Long id1,Long id2,Long eventId,ReviewEnum reviewFor);

}
