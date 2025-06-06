package com.snipe.learning.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    
    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@upltutorial.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

    @Async
    public void sendSubmissionConfirmation(String email, String name) {
        String subject = "Your Instructor Registration is Submitted";
        String body = "Dear " + name + ",\n\n" +
                      "Thank you for registering as an instructor.\n" +
                      "Your application has been submitted and is pending admin approval.\n\n" +
                      "You will receive an email once it is approved.\n\n" +
                      "Best regards,\nOnline Tutorial Team";
        sendEmail(email, subject, body);
    }

    @Async
    public void sendApprovalNotification(String email, String name) {
        String subject = "Instructor Account Approved";
        String body = "Dear " + name + ",\n\n" +
                      "Your instructor account has been approved by the admin.\n" +
                      "You can now log in and contribute tutorials.\n\n" +
                      "Regards,\nOnline Tutorial Team";
        sendEmail(email, subject, body);
    }
    
    @Async
    public void sendRejectNotification(String email, String name) {
        String subject = "Instructor Account Rejected";
        String body = "Dear " + name + ",\n\n" +
                      "Your instructor account has been rejected by the admin.\n" +
                      "Regards,\nOnline Tutorial Team";
        sendEmail(email, subject, body);
    }
    @Async
    public void sendApprovalNotificationforCourse(String email, String name) {
        String subject = "Your Course Approved";
        String body = "Dear " + name + ",\n\n" +
                      "Your course  has been approved by the admin.\n" +
                      "You can now contribute tutorials for this course.\n\n" +
                      "Regards,\nAdmin";
        sendEmail(email, subject, body);
    }
    
    @Async
    public void sendRejectNotificationforCourse(String email, String name) {
    	   String subject = "Your Course Rejected";
           String body = "Dear " + name + ",\n\n" +
                         "Your course  has been rejected by the admin.\n" +
                         "Please modify the content.\n\n" +
                         "Regards,\nAdmin";
           sendEmail(email, subject, body);
    }
    @Async
    public void sendApprovalNotificationforTutorial(String email, String name) {
        String subject = "Your Tutorial Approved";
        String body = "Dear " + name + ",\n\n" +
                      "Your tutorial  has been approved by the admin.\n" +
                      "Its now available to all users.\n\n" +
                      "Regards,\nAdmin";
        sendEmail(email, subject, body);
    }
    
    @Async
    public void sendRejectNotificationforTutorial(String email, String name) {
    	 String subject = "Your Tutorial Rejected";
         String body = "Dear " + name + ",\n\n" +
                       "Your tutorial  has been rejected by the admin.\n" +
                       "Please modify the content.\n\n" +
                       "Regards,\nAdmin";
         sendEmail(email, subject, body);
    }
    
    @Async
    public void sendCommentNotification(String email, String name) {
        String subject = "Received Comments";
        String body = "Dear " + name + ",\n\n" +
        		"Your course has received a new comment from a user. Please review it and take any necessary action.\n\n" +
                      "Regards,\nOnline Tutorial Team";
        sendEmail(email, subject, body);
    }
}
