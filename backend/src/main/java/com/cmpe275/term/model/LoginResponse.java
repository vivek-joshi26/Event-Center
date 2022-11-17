package com.cmpe275.term.model;

import com.cmpe275.term.entity.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
	private User user;
	private String token;
	private String sysDate;
}
