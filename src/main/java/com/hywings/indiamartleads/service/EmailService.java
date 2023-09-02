package com.hywings.indiamartleads.service;

import jakarta.mail.Folder;
import jakarta.mail.MessagingException;

import java.util.List;
import java.util.Properties;

public interface EmailService {

    void processFolderForBuyLeads(Folder folder, Properties properties, List<String> fromEmails) throws MessagingException;

}
