package com.cmpe275.term.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
public class ParticipantForum {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

//    private String status;  // can be OPEN or CLOSED
    // should be created when event passes registration deadline, that is event is in closed status
    // and should be available after 72 hrs after the end of event, or if the owner closed the forum
    // only participant and owner can post and view the forum

    private String sentBy;

    private String message;

    private boolean eventOwner;


    @Column(length = 520)
    private String imageURL;

    @Temporal(TemporalType.TIMESTAMP)
    private Date messageDateTime;

    @ManyToOne
    @JoinColumn(name="EVENT_ID")
    @JsonBackReference
    private Event event;

    @ManyToOne
    @JoinColumn(name="USER_ID")
    @JsonBackReference
    private User user;

}
