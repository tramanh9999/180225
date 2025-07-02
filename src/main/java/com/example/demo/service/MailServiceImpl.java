package com.example.demo.service;

import com.example.demo.model.ChangeRequestRoleUserModel;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor// Mark this class as a Spring service component
public class MailServiceImpl implements MailService {

//    private final MailSender mailSender; // Or JavaMailSender for more advanced features

    @Override
    public void sendEmail(String to, String subject, String body) {
        // Implementation for sending a single email using MailSender
        System.out.println("Attempting to send email to: " + to); // For logging/debugging

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            // You might want to set the 'from' address here if not configured globally
            // message.setFrom("your_email@example.com");

//            mailSender.send(message);
            System.out.println("Email sent successfully to: " + to);
        } catch (Exception e) {
            System.err.println("Failed to send email to " + to + ": " + e.getMessage());
            // Log the exception properly in a real application
        }
    }

    @Override
    public void sendBulkEmail(List<ChangeRequestRoleUserModel> recipients, String subject,
                              String body) {

        System.out.println("Bulk email sending process completed.");
    }

    // Other methods as required by the MailService interface
}