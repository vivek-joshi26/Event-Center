package com.cmpe275.term.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cmpe275.term.entity.Reviews;
import com.cmpe275.term.entity.User;
import com.cmpe275.term.model.ReviewRequest;
import com.cmpe275.term.model.UserModel;

@Component
public class ReviewMapper {
	@Autowired
	private ModelMapper modelMapper;
	
	public Reviews reviewRequestToEntity(ReviewRequest request) {
		
		Reviews review = this.modelMapper.map(request, Reviews.class);
		return review;
	}
	
	

	

}
