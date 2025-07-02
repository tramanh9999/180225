package com.example.demo.service;

import com.example.demo.model.ChangeRequestRoleUserModel;

import java.util.List;

public interface MailService {
    void sendEmail(String to, String subject, String body);

    void sendBulkEmail(List<ChangeRequestRoleUserModel> recipients, String subject, String body);
}
