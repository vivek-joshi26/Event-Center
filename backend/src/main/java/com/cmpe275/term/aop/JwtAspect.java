package com.cmpe275.term.aop;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.cmpe275.term.utility.JWTUtility;

@Aspect
@Component
public class JwtAspect {
	
	@Autowired
	HttpServletRequest request;
	@Autowired
	HttpServletResponse response;
	@Autowired
	private JWTUtility jwtUtility;
	
	@Pointcut("execution(public * com.cmpe275.term.controller.EventController.*(..)) || execution(public * com.cmpe275.term.controller.ParticipantController.*(..)) || execution(public * com.cmpe275.term.controller.ParticipantForumController.*(..)) || execution(public * com.cmpe275.term.controller.ReportingController.*(..)) || execution(public * com.cmpe275.term.controller.ReviewController.*(..)) || execution(public * com.cmpe275.term.controller.SignupForumController.*(..))")
	public void executionPoint(){}
	@Around("executionPoint()")
	public 	Object checkToken(ProceedingJoinPoint joinpoint)   {
	
		
		try {
			String auth = request.getHeader("Authorization");
			if(auth==null ||auth.isBlank()) {
				//System.out.println("hiiii");
				throw new Exception();
			}
			jwtUtility.verify(auth);
//			var obj = joinpoint.proceed();
//			return obj;
			
			
		}catch (Throwable exception){
			System.out.println(exception.getClass());
			return new ResponseEntity("not authorized" , HttpStatus.UNAUTHORIZED);
		}
		try {
			return joinpoint.proceed();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity("some error occurred! Please try again later" , HttpStatus.BAD_REQUEST);
		}
	
		}

}


