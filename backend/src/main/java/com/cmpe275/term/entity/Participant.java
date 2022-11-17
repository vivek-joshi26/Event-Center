package com.cmpe275.term.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name="EVENT_ID")
    @JsonBackReference
    private Event event;

    @ManyToOne
    @JoinColumn(name="USER_ID")
    @JsonBackReference
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name="approval_status")
    private ApprovalStatus approvalStatus;

    private String participantName;

    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDateTime;


}
