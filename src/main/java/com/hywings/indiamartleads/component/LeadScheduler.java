package com.hywings.indiamartleads.component;

import com.hywings.indiamartleads.service.BuyLeadService;
import com.hywings.indiamartleads.util.AppConstant;
import com.hywings.indiamartleads.util.AppUtility;
import com.hywings.indiamartleads.util.FileUtility;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.search.ComparisonTerm;
import jakarta.mail.search.ReceivedDateTerm;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.select.Evaluator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Component
public class LeadScheduler {

    @Autowired
    private BuyLeadService buyLeadService;

    @Autowired
    @Qualifier("buyLeadTaskExecutor")
    private TaskExecutor buyLeadTaskExecutor;

    @Value("${buy.lead.auditFilePath}")
    private String buyLeadAuditFilePath;

    //@Scheduled(fixedDelayString = "${buy.lead.scheduler.fixedDelay}")
    public void buyLeadScheduler() throws IOException {

        buyLeadTaskExecutor.execute(() -> {

            Properties auditProperties = FileUtility.getProperties(buyLeadAuditFilePath);

            buyLeadService.contactAndReplyLeads(auditProperties);

            FileUtility.storeProperties(auditProperties, buyLeadAuditFilePath);

        });


    }

    @Scheduled(cron = "0 0 0 * * *")
    public void resetAuditPropertiesScheduler() {

        Properties auditProperties = FileUtility.getProperties(buyLeadAuditFilePath);

        auditProperties.put(AppConstant.TOTAL_BUY_LEADS, AppConstant.ZERO_STRING);

        FileUtility.storeProperties(auditProperties, buyLeadAuditFilePath);

    }


}
