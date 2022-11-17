package com.cmpe275.term.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(String toEmail, String body, String subject){
        // Sometimes timeout happens, can add retry logic here
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom("ceccmpe275@gmail.com");
            simpleMailMessage.setTo(toEmail);
            simpleMailMessage.setText(body);
            simpleMailMessage.setSubject(subject);
            javaMailSender.send(simpleMailMessage);

            System.out.println("Mail send Successfully");
        }catch (Exception exception){
            System.out.println("********** Exception in sending email: " + exception.getMessage());
        }
    }
}
