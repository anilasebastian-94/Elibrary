package com.ElibraryDevelopment.Elibrary.Service.Impl;

import com.ElibraryDevelopment.Elibrary.Service.EmailService;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }


    @Override
    @Async
    public boolean send(String to, String email) {
        try{
            MimeMessage mimeMessage =  mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,"utf-8");
            helper.setText(email,true);
            helper.setTo(to);
            helper.setSubject(" Message");
            helper.setFrom("anilasebastian94@gmail.com");
            mailSender.send(mimeMessage);
            return true;
        }
        catch (MessagingException e){
            throw new IllegalStateException("failed to send");
        }
        }
}
