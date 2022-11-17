package com.cmpe275.term.model;

import com.cmpe275.term.entity.AuthenticationProvider;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {
    private String name;
    private String email;
    private String password;
    private String role;
    private String screenName;
	private String gender;
	private String description;
	private Address address;
    // private AuthenticationProvider provider;
    //private String matchingPassword;
}
