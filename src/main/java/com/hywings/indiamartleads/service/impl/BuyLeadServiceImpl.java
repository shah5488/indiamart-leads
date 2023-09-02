package com.hywings.indiamartleads.service.impl;

import com.hywings.indiamartleads.service.BuyLeadService;
import com.hywings.indiamartleads.util.AppConstant;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

@Service
public class BuyLeadServiceImpl implements BuyLeadService {

    @Autowired
    @Qualifier("buyLeadWebDriver")
    private ChromeDriver webDriver;

    private static List<String> buyLeadKeywords = new ArrayList<>();
    private static long lastModifiedBuyLeadsKeywords;

    @Value("${buy.lead.keywordsFilePath}")
    private String buyLeadKeywordsFilePath;

    @Override
    public void contactAndReplyLeads(Properties auditProperties) {

        try {

            updateBuyLeadKeywords();

            webDriver.manage().window().maximize();
            webDriver.navigate().refresh();

            JavascriptExecutor javascriptExecutor = (JavascriptExecutor)webDriver;
            javascriptExecutor.executeScript("window.scrollTo(0,0)");

            By sortByDescId = By.id(AppConstant.SORT_BY_REC_ID);
            WebDriverWait webDriverWait = new WebDriverWait(webDriver, Duration.ofSeconds(60));
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(sortByDescId));

            TimeUnit.SECONDS.sleep(2);

            WebElement sortByDescElement = webDriver.findElement(sortByDescId);
            sortByDescElement.click();

            TimeUnit.SECONDS.sleep(1);

            /* getting an element of but leads */
            WebElement blListingElement = webDriver.findElement(By.id(AppConstant.BUY_LEADS_ID));

            /* getting all products which has class 'Prd_Enq' */
            List<WebElement> productEnqLst = blListingElement.findElements(By.xpath(AppConstant.PRODUCT_ENQ_XPATH));
            findProductWithKeywordsAndReply(productEnqLst, webDriver, auditProperties);

        } catch (Exception e) {
            System.out.println(e.getMessage());

        }

    }

    /**
     * @param productEnqLst
     * @param webDriver     This method is used to find products from List<WebElement> by checking keywords
     *                      & then replying
     */
    private void findProductWithKeywordsAndReply(List<WebElement> productEnqLst, WebDriver webDriver, Properties auditProperties) throws IOException {

        int maxBuyLeads = Integer.parseInt(String.valueOf(auditProperties.getOrDefault(AppConstant.MAX_BUY_LEADS, AppConstant.FIVE_STRING)));
        int totalBuyLeads = Integer.parseInt(String.valueOf(auditProperties.getOrDefault(AppConstant.TOTAL_BUY_LEADS, AppConstant.ZERO_STRING)));

        if(totalBuyLeads >= maxBuyLeads){
            return;
        }

        for (WebElement productEnq : productEnqLst) {

            try {

                /* getting product name */
                String productName = productEnq.findElement(By.xpath(AppConstant.PRODUCT_NAME_XPATH)).getText();

                /* checking if product matched from keywords */
                boolean isProductMatched = buyLeadKeywords.stream().map(StringUtils::trim).anyMatch(keyword -> StringUtils.containsIgnoreCase(productName, keyword));

                if (isProductMatched) {

                    /* if required product found then finding & clicking contact buyer button */
                    WebElement contactBuyerButton = productEnq.findElement(By.xpath(AppConstant.CONTACT_BUYER_BTN_XPATH));
                    contactBuyerButton.click();

                    webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

                    /* finding & clicking send reply button */
                    WebElement sendReplyButton = webDriver.findElement(By.className(AppConstant.SEND_REPLY_CLASS));
                    sendReplyButton.click();

                    if(++totalBuyLeads >= maxBuyLeads){
                        break;

                    }

                }

            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        }

        auditProperties.put(AppConstant.TOTAL_BUY_LEADS, String.valueOf(totalBuyLeads));
        auditProperties.put(AppConstant.MAX_BUY_LEADS, String.valueOf(maxBuyLeads));

    }

    /**
     * This method is used to update buyLeadKeywords & lastModifiedBuyLeadsKeywords
     */
    private void updateBuyLeadKeywords() throws Exception {

        File file = new File(buyLeadKeywordsFilePath);

        /* if  lastModifiedBuyLeadsKeywords not matching with current lastModified date then updating keywords*/
        if (lastModifiedBuyLeadsKeywords != file.lastModified()) {

            try (BufferedReader buyLeadsKeywordsBR = new BufferedReader(new FileReader(file))) {

                buyLeadKeywords.clear();

                String line = null;

                while ((line = buyLeadsKeywordsBR.readLine()) != null) {

                    buyLeadKeywords.addAll(List.of(line.split(AppConstant.COMMA)));

                }

                lastModifiedBuyLeadsKeywords = file.lastModified();

            } catch (Exception e) {
                throw e;

            }
        }
    }

}
