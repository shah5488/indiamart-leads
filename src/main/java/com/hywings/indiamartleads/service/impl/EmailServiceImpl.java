package com.hywings.indiamartleads.service.impl;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.hywings.indiamartleads.service.EmailService;
import com.hywings.indiamartleads.service.WhatsAppService;
import com.hywings.indiamartleads.util.AppConstant;
import com.hywings.indiamartleads.util.AppUtility;
import jakarta.mail.*;
import jakarta.mail.search.*;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.openqa.selenium.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private WhatsAppService whatsAppService;

    @Value("${buy.lead.reply.whatsapp.template}")
    private String buyLeadWhatsappTemplate;

    @Value("${call.lead.reply.whatsapp.template}")
    private String callLeadWhatsappTemplate;

    @Value("${enquiry.lead.reply.whatsapp.template}")
    private String enquiryLeadWhatsappTemplate;


    private Date getLastReceivedDate(String key, Properties properties) {

        Date lastReceivedDateValue = AppUtility.parseISOLocalDateTime(properties.getProperty(key));

        if (Objects.nonNull(lastReceivedDateValue)) {
            return lastReceivedDateValue;
        }

        return new Date();
    }

    /**
     * @param folder
     * @throws MessagingException This method is used to read messages form Folder
     */
    @Override
    public void processFolderForBuyLeads(Folder folder, Properties properties, List<String> fromEmails) throws MessagingException {

        String fromEmailAddress = fromEmails.stream().collect(Collectors.joining(AppConstant.HYPHEN));
        SearchTerm[] fromSearchTerms = fromEmails.stream().map(fromEmail -> new FromStringTerm(fromEmail)).toArray(size -> new SearchTerm[size]);

        String receivedDateProperty = folder.getName() + AppConstant.HYPHEN + fromEmailAddress + AppConstant.HYPHEN + AppConstant.RECEIVED_DATE;

        folder.open(Folder.READ_WRITE);

        /* search term for date */
        ReceivedDateTerm startDate = new ReceivedDateTerm(ComparisonTerm.GE, getLastReceivedDate(receivedDateProperty, properties));

        OrTerm fromTerm = new OrTerm(fromSearchTerms);
        FlagTerm unseenFlagTerm = new FlagTerm(new Flags(Flags.Flag.SEEN), Boolean.FALSE);
        SearchTerm[] searchTerms = new SearchTerm[]{fromTerm, startDate, unseenFlagTerm};

        Message[] messages = folder.search(new AndTerm(searchTerms));

        for (Message message : messages) {

            readIndiaMartEmailsAndSendTemplateInWhatsapp(message);

            properties.put(receivedDateProperty, AppConstant.ISO_DATE_SDF.format(message.getReceivedDate()));

        }


    }

    /**
     * @param message This method is used to process Message and send template to Whatsapp
     */
    private void readIndiaMartEmailsAndSendTemplateInWhatsapp(Message message) {

        try {

            String address = AppUtility.getFirstFromMail(message.getFrom()).toLowerCase();

            switch (address) {

                case AppConstant.BUY_LEADS_INDIAMART_DOT_COM:
                    processBuyLeadIndiaMart(message);
                    break;

                case AppConstant.CALL_SUPPORT_INDIAMART_DOT_COM:
                    processCallSupportIndiaMart(message);
                    break;

                case AppConstant.BUYERS_HELP_ENQ_INDIAMART_DOT_COM:
                    processEnquiryIndiaMart(message);
                    break;

            }

        } catch (Exception e) {
            System.out.println("issue with message " + message);

        }
    }

    /**
     * @param message This method is used to process buy lead Message and send template to Whatsapp
     */
    private void processBuyLeadIndiaMart(Message message) {

        try {

            Document document = Jsoup.parse(AppUtility.getHTMLTextFromMimeMultipart(message));

            Element phoneElement = document.selectFirst(AppConstant.HREF_TEL_CSS_QUERY);
            Element phoneParentElement = phoneElement.parent();
            Node nameNode = phoneParentElement.childNode(0);

            String productName = StringUtils.substringBetween(message.getSubject(), AppConstant.DOUBLE_QUOTE, AppConstant.DOUBLE_QUOTE);
            String contactNumber = AppUtility.getContactNumber(phoneElement);
            String name = StringUtils.trimToEmpty(nameNode.toString());

            if (StringUtils.isNoneBlank(productName, contactNumber, name)) {

                String whatsappMsg = buyLeadWhatsappTemplate.replace(AppConstant.NEW_LINE, Keys.chord(Keys.SHIFT, Keys.ENTER))
                        .replace(AppConstant.PRODUCT_NAME, productName)
                        .replace(AppConstant.NAME, name);

                whatsAppService.sendMessage(whatsappMsg, contactNumber);

            }


        } catch (Exception e) {
        }
    }

    /**
     * @param message This method is used to process call support Message and send template to Whatsapp
     */
    private void processCallSupportIndiaMart(Message message) {

        try {

            Document document = Jsoup.parse(AppUtility.getHTMLTextFromMimeMultipart(message));

            /* caller element row */
            Elements callerElementRow = document.selectFirst(AppConstant.CALLER_NAME_IMAGE).parent().parent().children();

            /* getting phone number and name */
            String phoneNumber = StringUtils.trimToEmpty(AppUtility.getContactNumber(callerElementRow.get(1).getElementsByTag(AppConstant.DIV).first()));
            String name = StringUtils.trimToEmpty(callerElementRow.get(5).getElementsByTag(AppConstant.DIV).first().text());

            if (StringUtils.isNoneBlank(name, phoneNumber)) {

                String whatsappMsg = callLeadWhatsappTemplate.replace(AppConstant.NEW_LINE, Keys.chord(Keys.SHIFT, Keys.ENTER))
                        .replace(AppConstant.NAME, name);

                whatsAppService.sendMessage(whatsappMsg, phoneNumber);

            }


        } catch (Exception e) {
        }
    }

    /**
     * @param message This method is used to process enquiry Message and send template to Whatsapp
     */
    private void processEnquiryIndiaMart(Message message) {

        try {

            String subject = message.getSubject();
            String htmlText = AppUtility.getHTMLTextFromMimeMultipart(message);

            String productName = StringUtils.substringBetween(subject, AppConstant.ENQUIRY_FOR, AppConstant.FROM).trim();
            long phoneNumber = PhoneNumberUtil.getInstance().findNumbers(htmlText, AppConstant.IN).iterator().next().number().getNationalNumber();
            String name = StringUtils.substringBetween(subject, AppConstant.FROM, AppConstant.VIA).trim();

            if (StringUtils.isBlank(name)) {

                name = StringUtils.substringAfter(subject, AppConstant.FROM).trim();

            }

            if (StringUtils.isNoneBlank(productName, name)) {

                String whatsappMsg = enquiryLeadWhatsappTemplate.replace(AppConstant.NEW_LINE, Keys.chord(Keys.SHIFT, Keys.ENTER))
                        .replace(AppConstant.PRODUCT_NAME, productName)
                        .replace(AppConstant.NAME, name);

                whatsAppService.sendMessage(whatsappMsg, String.valueOf(phoneNumber));

            }


        } catch (Exception e) {
        }
    }
}
