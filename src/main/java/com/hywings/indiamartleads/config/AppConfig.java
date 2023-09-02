package com.hywings.indiamartleads.config;

import com.hywings.indiamartleads.util.AppConstant;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.io.File;
import java.time.Duration;
import java.util.Optional;

@Configuration
public class AppConfig {


    @Value("${chrome.driver.path}")
    private String chromeDriverPath;

    @Value("${buy.lead.reply.whatsapp.temp.phone1}")
    private String tempPhone;

    @Bean("buyLeadWebDriver")
    public ChromeDriver getBuyLeadsChromeDriver() {
        updateChromeDriver();

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments(AppConstant.USER_DATA_DIR + System.getProperty(AppConstant.TMPDIR_PROPERTY) + File.separator + AppConstant.BUY_LEADS);

        /* opening BUY_LEADS_URL */
        ChromeDriver webDriver = new ChromeDriver(chromeOptions);
        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        webDriver.get(AppConstant.BUY_LEADS_URL);

        return webDriver;
    }

    @Bean("whatsAppWebDriver1")
    public ChromeDriver getWhatsAppWebDriver() {
        updateChromeDriver();

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments(AppConstant.USER_DATA_DIR + System.getProperty(AppConstant.TMPDIR_PROPERTY) + File.separator + AppConstant.WHATSAPP);

        ChromeDriver webDriver = new ChromeDriver(chromeOptions);

        webDriver.manage().window().maximize();
        webDriver.get(AppConstant.WHATSAPP_WEB_URL + tempPhone);

        By whatsappInput = By.cssSelector(AppConstant.WHATSAPP_INPUT_CSS_QUERY);
        WebDriverWait webDriverWait = new WebDriverWait(webDriver, Duration.ofSeconds(60));
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(whatsappInput));

        return webDriver;
    }


    private void updateChromeDriver(){
        System.setProperty(AppConstant.WEB_CHROME_DRIVER, Optional.ofNullable(System.getProperty(AppConstant.WEB_CHROME_DRIVER)).orElse(chromeDriverPath));
    }
}
