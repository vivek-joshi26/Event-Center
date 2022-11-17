package com.cmpe275.term.repository;

import java.util.Date;
import java.util.List;

import com.amazonaws.services.autoscaling.model.Ebs;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.cmpe275.term.entity.Event;
import com.cmpe275.term.entity.User;

@Repository
public interface EventRepository extends JpaRepository<Event,Long>,JpaSpecificationExecutor<Event>  {

    List<Event> getByUser(User user);
    public List<Event> findAll(Specification<Event> spec);

    List<Event> getEventByCreationDateTimeAfter(Date creationDate);

    List<Event> getEventByCreationDateTimeAfterAndUser(Date creationDate, User user);

}
