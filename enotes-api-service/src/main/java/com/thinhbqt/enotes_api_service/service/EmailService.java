package com.thinhbqt.enotes_api_service.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.thinhbqt.enotes_api_service.dto.EmailRequest;

import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;

import jakarta.mail.internet.MimeMessage;
@Component
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String mailFrom;

    @Async
    public void sendEmail(EmailRequest emailRequest) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true); 

            helper.setFrom(mailFrom);
            helper.setTo(emailRequest.getTo());
            helper.setSubject(emailRequest.getSubject());
            
        
            helper.setText(emailRequest.getMessage(), true); 

            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}