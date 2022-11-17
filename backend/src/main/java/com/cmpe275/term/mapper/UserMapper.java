package com.cmpe275.term.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cmpe275.term.entity.User;
import com.cmpe275.term.model.UserModel;

@Component
public class UserMapper {
	
	@Autowired
	private ModelMapper modelMapper;
	
	public User userModelToEntity(UserModel userModel) {
		
		User user = this.modelMapper.map(userModel, User.class);
		return user;
	}
	
	public UserModel entityToUserModel(User user) {
		
		UserModel userModel = this.modelMapper.map(user, UserModel.class);
		return userModel;
	}

}
