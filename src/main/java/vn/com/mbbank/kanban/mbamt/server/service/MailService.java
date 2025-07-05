package vn.com.mbbank.kanban.mbamt.server.service;

import vn.com.mbbank.kanban.mbamt.server.model.ChangeRequestRoleUserModel;

import java.util.List;

public interface MailService {
    void sendEmail(String to, String subject, String body);

    void sendBulkEmail(List<ChangeRequestRoleUserModel> recipients, String subject, String body);
}
