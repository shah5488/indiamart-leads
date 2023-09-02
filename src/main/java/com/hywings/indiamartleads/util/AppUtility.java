package com.hywings.indiamartleads.util;

import jakarta.mail.Address;
import jakarta.mail.BodyPart;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMultipart;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;
import java.util.regex.Pattern;

public class AppUtility {
    AppUtility() {
    }

    /**
     * @param message
     * @return This method is used to get HTML Text from Message
     */
    public static String getHTMLTextFromMimeMultipart(Message message) {

        try {

            MimeMultipart multipart = (MimeMultipart) message.getContent();

            for (int i = 0; i < multipart.getCount(); i++) {

                BodyPart bodyPart = multipart.getBodyPart(i);

                if (Pattern.compile(Pattern.quote(AppConstant.TEXT_HTML_CONTENT_TYPE), Pattern.CASE_INSENSITIVE).matcher(bodyPart.getContentType()).find()) {
                    return (String) bodyPart.getContent();

                }

            }

        } catch (Exception e) {
            return AppConstant.EMPTY;

        }

        return AppConstant.EMPTY;

    }

    public static Date parseISOLocalDateTime(String date) {

        try {

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(AppConstant.ISO_DATE_TIME_FORMAT);
            return simpleDateFormat.parse(date);

        } catch (Exception e) {
            return null;

        }
    }


    /**
     *
     * @param addresses
     * @return
     *          This method is used to first email address
     */
    public static String getFirstFromMail(Address[] addresses){

        try{

            return ((InternetAddress)addresses[0]).getAddress();

        }catch (Exception e){}

        return null;

    }


    /**
     * @param element
     * @return This method is used to get contact number from Element
     */
    public static String getContactNumber(Element element) {

        try {

            String telValue = element.text().replaceAll(AppConstant.NON_NUMERIC_REGEX, AppConstant.EMPTY);
            return telValue.substring(telValue.length() - 10);

        } catch (Exception e) {
            return null;

        }

    }

}
