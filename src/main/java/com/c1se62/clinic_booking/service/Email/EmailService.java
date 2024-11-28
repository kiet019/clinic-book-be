package com.c1se62.clinic_booking.service.Email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom("trungkien13042003@gmail.com");
        javaMailSender.send(message);
    }
    public void sendHtmlEmail(String to, String subject, String body) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true); // true indicates multipart

        try {
            messageHelper.setFrom("trungkien13042003@gmail.com");
            messageHelper.setTo(to);
            messageHelper.setSubject(subject);
            messageHelper.setText(body, true); // Set to 'true' to indicate HTML content
            javaMailSender.send(mimeMessage);
            System.out.println("HTML email sent successfully.");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
