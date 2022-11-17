package com.cmpe275.term.event.listener;

import com.cmpe275.term.entity.User;
import com.cmpe275.term.event.RegistrationCompleteEvent;
import com.cmpe275.term.service.EmailSenderService;
import com.cmpe275.term.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailSenderService emailSenderService;


    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        // Create the verification token for the user with link
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.saveVerificationTokenForUser(token, user);
        // send mail to user

        // this is the endpoint throgh which the user will be validated
        String url = event.getApplicationUrl() + "/verifyRegistration?token=" + token;
        // call send email method
        emailSenderService.sendEmail(user.getEmail(), "Verify your account by clicking this link: " + url, "Email Verification" );
        System.out.println("Account verification url: " + url);
    }
}
