package com.cmpe275.term.repository;

import com.cmpe275.term.entity.Event;
import com.cmpe275.term.entity.SignupForum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SignupForumRepository extends JpaRepository<SignupForum, Long> {

    List<SignupForum> findByEvent(Event event);
}
