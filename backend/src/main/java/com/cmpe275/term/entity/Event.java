package com.cmpe275.term.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Event {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	private String title;
	private String description;
	private Integer minParticipants;
	private Integer maxParticipants;
	private Integer totalParticipants;
	//private String status;	//open closed
	@Enumerated(EnumType.STRING)
	@Column(name="event_status")
	private EventStatus status;

	@Enumerated(EnumType.STRING)
	@Column(name="signupforum_status")
	private EventStatus signupForumStatus;


	@Enumerated(EnumType.STRING)
	@Column(name="participantforum_status")
	private ParticipantForumStatus participantForumStatus;

	@Embedded
	@AttributeOverrides({
		  @AttributeOverride( name = "street", column = @Column(name = "street")),
		  @AttributeOverride( name = "city", column = @Column(name = "city")),
		  @AttributeOverride( name = "state", column = @Column(name = "state")),
		  @AttributeOverride( name = "zip", column = @Column(name = "zip"))
		})
	private Address address;
	//Many events can be created by one user.
	@ManyToOne
	@JoinColumn(name="USER_ID")
	@JsonBackReference
	private User user;
	private String admissionPolicy;
	private Long fee;
	//@Column(name="start_date_time", columnDefinition = "TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date  startDateTime;
	@Temporal(TemporalType.TIMESTAMP)
	private Date  endDateTime;
	@Temporal(TemporalType.TIMESTAMP)
	private Date  deadline;

	@Temporal(TemporalType.TIMESTAMP)
	private Date  creationDateTime;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "event", fetch=FetchType.EAGER)
	@JsonIgnore
	@OrderBy("id DESC")
	private List<Participant> participants;


	@OneToMany(cascade = CascadeType.ALL, mappedBy = "event", fetch=FetchType.LAZY)
	@JsonIgnore
	@OrderBy("messageDateTime ASC")
	private List<ParticipantForum> participantForumMessages;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "event", fetch=FetchType.LAZY)
	@JsonIgnore
	@OrderBy("messageDateTime ASC")
	private List<SignupForum> signupForumMessages;

}
