package com.hywings.indiamartleads.util;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.List;

public class AppConstant {

    private AppConstant(){}

    /***************************** DATE FORMAT CONSTANTS ********************************/
    public static final String ISO_DATE_TIME_FORMAT                 = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS";


    /***************************** TIMEZONE CONSTANTS *************************/
    public static final String IST                                  = "IST";

    /***************************** SIMPLE DATE FORMAT CONSTANTS *************************/
    public static final SimpleDateFormat ISO_DATE_SDF               = new SimpleDateFormat(ISO_DATE_TIME_FORMAT);

    /***************************** STRING CONSTANTS *************************************/
    public static final String USER_DATA_DIR                        = "--user-data-dir=";
    public static final String BUY_LEADS_INDIAMART_DOT_COM          = "buyleads@indiamart.com";
    public static final String CALL_SUPPORT_INDIAMART_DOT_COM       = "call-support@indiamart.com";
    public static final String BUYERS_HELP_ENQ_INDIAMART_DOT_COM    = "buyershelp+enq@indiamart.com";

    public static final List<String> BUY_LEADS_EMAILS               = List.of(BUY_LEADS_INDIAMART_DOT_COM, BUYERS_HELP_ENQ_INDIAMART_DOT_COM);
    public static final List<String> CALL_LEADS_EMAILS              = List.of(CALL_SUPPORT_INDIAMART_DOT_COM);

    public static final String EMPTY                                = "";
    public static final String TEXT_HTML_CONTENT_TYPE               = "text/html";
    public static final String HYPHEN                               = "-";
    public static final String COMMA                                = ",";
    public static final String RECEIVED_DATE                        = "RECEIVED_DATE";
    public static final String BUY_LEADS                            = "buyLeads";
    public static final String WHATSAPP                             = "whatsapp";
    public static final String NEW_LINE                             = "\n";
    public static final String PRODUCT_NAME                         = "{{PRODUCT_NAME}}";
    public static final String NAME                                 = "{{NAME}}";
    public static final String DOUBLE_QUOTE                         = "\"";
    public static final String MAX_BUY_LEADS                        = "MAX_BUY_LEADS";
    public static final String TOTAL_BUY_LEADS                      = "TOTAL_BUY_LEADS";

    public static final String ZERO_STRING                          = "0";
    public static final String FIVE_STRING                          = "5";

    public static final String WEB_CHROME_DRIVER                     = "webdriver.chrome.driver";

    public static final String ENQUIRY_FOR                          = "Enquiry for";
    public static final String FROM                                  = "from";
    public static final String VIA                                   = "via";
    public static final String IN                                    = "IN";
    public static final String DIV                                   = "div";

    /*************************** SYSTEM PROPERTY CONSTANT *******************************/
    public static final String TMPDIR_PROPERTY                      = "java.io.tmpdir";


    /******************************* URL CONSTANTS **************************************/
    public static final String BUY_LEADS_URL                        = "https://seller.indiamart.com/bltxn/?pref=relevant";
    public static final String WHATSAPP_WEB_URL                     = "https://web.whatsapp.com/send?phone=";


    /******************************* WEB ELEMENT SEARCH CONSTANTS **************************************/
    public static final String BUY_LEADS_ID                        = "bl_listing";
    public static final String PRODUCT_ENQ_XPATH                   = "./child::div[contains(@class, 'Prd_Enq')]";
    public static final String PRODUCT_NAME_XPATH                  = "./child::*//div//div//div";
    public static final String CONTACT_BUYER_BTN_XPATH             = "./descendant::div[contains(@onclick, 'contactbuyernow')]";
    public static final String SEND_REPLY_CLASS                    = "sendreply";

    public static final String SORT_BY_REC_ID                      = "sortByRec";

    public static final String CALLER_NAME_IMAGE                   = "img[src~=/images/caller-name]";;
    public static final String CALLER_NUMBER                       = "//*[@id=':1ob']/div[2]/table[1]/tbody/tr/td[2]/a/div";

    /********************************** MAIL CONSTANTS ***********************************************/
    public static final String MAIL_STORE_PROTOCOL                 = "mail.store.protocol";

    /********************************** CSS QUERY CONSTANTS ***********************************************/
    public static final String HREF_TEL_CSS_QUERY                   = "a[href~=http://indmrt.in/ueclkejsvk]";
    public static final String WHATSAPP_INPUT_CSS_QUERY             = "div[title='Type a message']";


    /********************************** REGEX CONSTANTS ***********************************************/
    public static final String NON_NUMERIC_REGEX                    = "[^0-9]";

}
