package com.cmpe275.term.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.cmpe275.term.enums.ReviewEnum;

import lombok.Data;

@Entity
@Data
public class Reviews {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	private Long eventId;
	private Long reviewGivenBy;
	private Long reviewGivenTo;
	private String reviewText;
	private Double reviewStar;
	@Enumerated(EnumType.STRING)
	private ReviewEnum reviewFor;
	 

}
