package com.cmpe275.term.entity;

import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String email;

    @Column(length = 60)
    private String password;
    private String role;
    private boolean enabled = false;
    private String screenName;
	private String gender;
	private String description;
    @Embedded
	@AttributeOverrides({
		  @AttributeOverride( name = "street", column = @Column(name = "street")),
		  @AttributeOverride( name = "city", column = @Column(name = "city")),
		  @AttributeOverride( name = "state", column = @Column(name = "state")),
		  @AttributeOverride( name = "zip", column = @Column(name = "zip"))
		})
	private Address address;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch=FetchType.LAZY)
	@JsonManagedReference
    @OrderBy("id DESC")
	private List<Event> eventss;

    @Enumerated(EnumType.STRING)
    @Column(name="auth_provider")
    private AuthenticationProvider authProvider;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch=FetchType.LAZY)
    @JsonIgnore
    @OrderBy("id DESC")
    private List<Participant> participants;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch=FetchType.LAZY)
    @JsonIgnore
    @OrderBy("messageDateTime ASC")
    private List<ParticipantForum> participantForumMessages;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch=FetchType.LAZY)
    @JsonIgnore
    @OrderBy("messageDateTime ASC")
    private List<ParticipantForum> signupForumMessages;
    
    
    private Double orgnizerReputation;
    private Double participantReputation;


}
