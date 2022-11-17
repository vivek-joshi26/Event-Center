package com.cmpe275.term.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cmpe275.term.model.MimicTime;
import com.cmpe275.term.utility.oauth.OAuth2LoginSuccessHandler;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import com.cmpe275.term.entity.User;
import com.cmpe275.term.entity.VerificationToken;
import com.cmpe275.term.event.RegistrationCompleteEvent;
import com.cmpe275.term.model.LoginRequest;
import com.cmpe275.term.model.LoginResponse;
import com.cmpe275.term.model.UserModel;
import com.cmpe275.term.service.EmailSenderService;
import com.cmpe275.term.service.UserService;
import com.cmpe275.term.utility.JWTUtility;

import java.io.IOException;
import java.security.Principal;
import java.util.List;


@RestController
//@RequestMapping("/user")
public class RegistrationController {

    @Autowired
    private UserService userService;

    // To send an email to the user once the account is created
    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private EmailSenderService emailSenderService;
    
    @Autowired
    private JWTUtility jwtUtility;
    
    @Autowired
    private AuthenticationManager manager;


    @Autowired
    private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    @Value( "${service.frontendURL}" )
    private String frontendURL;

    // oauth google login testing
    @GetMapping
	public Principal oauthLogin2(Principal principal) {
		return principal;
	}



    @GetMapping("/user/{email}")
    public ResponseEntity getUserByEmail(@PathVariable("email") String email) throws Exception {
        System.out.println("Inside get user");
        // ResponseEntity<LoginResponse> user = userService.loginGoogle(email);
        return userService.loginGoogle(email);
    }

    @DeleteMapping("/user/{email}")
    public ResponseEntity deleteUserByEmail(@PathVariable("email") String email) throws Exception {
       // User user = userService.deleteUser(email);
        System.out.println("Inside delete user");
        return ResponseEntity.ok(userService.deleteUser(email));
    }


    @PostMapping("/register")
    public ResponseEntity registerUser(@RequestBody UserModel userModel, final HttpServletRequest request){
        ResponseEntity<User> user = userService.registerUser(userModel);
        System.out.println("INSIDE registerUser, RESTCONTROLLER, created user: " + user);
        if(user.getStatusCode().is2xxSuccessful()){
            publisher.publishEvent(new RegistrationCompleteEvent(user.getBody(), applicationUrl(request)));
            return user;
        }
        return user;

    }

//    @PostMapping("/registerGoogle")
//    public ResponseEntity registerGoogleUser(@RequestBody UserModel userModel, final HttpServletRequest request){
//        ResponseEntity<User> user = userService.registerGoogleUser(userModel);
//        System.out.println("INSIDE registerUser, RESTCONTROLLER, created user: " + user);
//        publisher.publishEvent(new RegistrationCompleteEvent(user.getBody(), applicationUrl(request)));
//        return user;
//    }

    @PostMapping("/registerGoogle")
    public ResponseEntity registerGoogleUser(@RequestBody UserModel userModel, final HttpServletRequest request){
        ResponseEntity<User> user = userService.registerGoogleUser(userModel);
        System.out.println("INSIDE registerUser, RESTCONTROLLER, created user: " + user);
        if(user.getStatusCode().is2xxSuccessful()) {
            publisher.publishEvent(new RegistrationCompleteEvent(user.getBody(), applicationUrl(request)));
            return user;
        }
        return user;
    }



    @GetMapping("/unauthorized")
    public String test() {
        return "User is unauthorized";
    }


    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequest request) throws Exception {
//    	try {
//    		LoginResponse response = userService.login(request);
//    		return response;
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//
//			System.out.println(e.getMessage());
//			return null;
//		}
        return userService.login(request);
    }

    @GetMapping("/verifyRegistration")
    public String verifyRegistration(@RequestParam("token") String token, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String result = userService.validateVerificationToken(token);
        if(result.equalsIgnoreCase("valid")){
            // use send redirect here to route to frontend login page
            response.sendRedirect(frontendURL);
            return "User Verified Successfully";
        }
        return "Verification token expired";
    }


    // regenrate verification token, based on userId, can be changed to email address too
    @GetMapping("/resendVerifyToken")
    public String resendVerificationToken(@RequestParam("userId") Long userId, HttpServletRequest request){
        VerificationToken verificationToken = userService.generateNewVerificationToken(userId);
        User user = verificationToken.getUser();
        resendVerificationTokenMail(user, applicationUrl(request), verificationToken.getToken());
        return "Verification Link sent";
    }

    private void resendVerificationTokenMail(User user, String applicationUrl, String token) {
        String url = applicationUrl + "/verifyRegistration?token=" + token;
        // this is the endpoint throgh which the user will be validated

        // call send email method
        emailSenderService.sendEmail(user.getEmail(), "Verify your account by clicking this link: " + url, "Email Verification" );
        System.out.println("Account Reverification url: " + url);
    }


    private String applicationUrl(HttpServletRequest request){
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }




}
