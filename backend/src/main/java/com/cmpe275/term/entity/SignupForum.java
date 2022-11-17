package com.cmpe275.term.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
public class SignupForum {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

//    private String status;  // can be OPEN or CLOSED
    //The sign-up forum becomes closed for posting of new messages once the event registration deadline passes, or the event has been canceled.

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
