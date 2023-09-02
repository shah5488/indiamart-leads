package com.hywings.indiamartleads.component;

import com.hywings.indiamartleads.config.model.EmailReceiverProps;
import com.hywings.indiamartleads.service.EmailService;
import com.hywings.indiamartleads.util.AppConstant;
import com.hywings.indiamartleads.util.AppUtility;
import com.hywings.indiamartleads.util.FileUtility;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.search.ComparisonTerm;
import jakarta.mail.search.ReceivedDateTerm;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.util.Properties;

@Component
public class EmailScheduler {

    @Autowired
    private Store store;

    @Autowired
    private EmailReceiverProps emailReceiverProps;

    @Autowired
    @Qualifier("emailLeadTaskExecutor")
    private TaskExecutor emailLeadTaskExecutor;

    @Autowired
    private EmailService emailService;

    @Scheduled(cron = "${email.lead.scheduler.cron}")
    public void emailLeadScheduler() {

        emailLeadTaskExecutor.execute(() -> {

            Properties auditProperties = FileUtility.getProperties(emailReceiverProps.getAuditFile());

            for (String folder : emailReceiverProps.getFolders()) {

                try {

                    emailService.processFolderForBuyLeads(store.getFolder(folder), auditProperties, AppConstant.BUY_LEADS_EMAILS);

                } catch (Exception e) {
                    System.out.println(e.getMessage());

                }

                FileUtility.storeProperties(auditProperties, emailReceiverProps.getAuditFile());

            }
        });
    }

    //@Scheduled(fixedDelayString = "${email.call.lead.scheduler.fixedDelay}")
    public void emailCallLeadScheduler() {

        Properties auditProperties = FileUtility.getProperties(emailReceiverProps.getAuditFile());

        for (String folder : emailReceiverProps.getFolders()) {

            try {

                emailService.processFolderForBuyLeads(store.getFolder(folder), auditProperties, AppConstant.CALL_LEADS_EMAILS);

            } catch (Exception e) {
                System.out.println(e.getMessage());

            }

            FileUtility.storeProperties(auditProperties, emailReceiverProps.getAuditFile());

        }
    }
}
