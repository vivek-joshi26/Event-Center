package com.cmpe275.term.entity;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Address {
	
	private String street;
	private String city;
	private String state;
	private String zip;

}
