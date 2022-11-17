package com.cmpe275.term.repository;

import com.cmpe275.term.entity.ApprovalStatus;
import com.cmpe275.term.entity.Event;
import com.cmpe275.term.entity.Participant;
import com.cmpe275.term.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    List<Participant> findParticipantByEventAndApprovalStatus(Event event, ApprovalStatus approvalStatus);

    List<Participant> findParticipantByEvent(Event event);

    List<Participant> findParticipantByUser(User user);

    Participant findParticipantByEventAndUser(Event event, User user);

    Participant findParticipantByEventAndUserAndApprovalStatus(Event event, User user, ApprovalStatus approvalStatus);

    List<Participant> findParticipantByUserAndCreationDateTimeAfter(User user, Date creationDate);

}
