package com.hywings.indiamartleads.service.impl;

import com.hywings.indiamartleads.service.WhatsAppService;
import com.hywings.indiamartleads.util.AppConstant;
import jakarta.annotation.PostConstruct;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class WhatsAppServiceImpl implements WhatsAppService {

    @Autowired
    @Qualifier("whatsAppWebDriver1")
    private ChromeDriver webDriver1;

    private static ChromeDriver[] allWebDrivers;
    private static AtomicInteger currentWebDriver = new AtomicInteger(-1);

    @PostConstruct
    public void init() {
        allWebDrivers = new ChromeDriver[]{webDriver1};

    }

    /**
     * @param message
     * @param phone   This method is used to send whatsapp message
     */
    @Override
    public void sendMessage(String message, String phone) {

        currentWebDriver.incrementAndGet();

        WebDriver webDriver = allWebDrivers[currentWebDriver.get() > allWebDrivers.length-1 ? 0 : currentWebDriver.get()];

        webDriver.get(AppConstant.WHATSAPP_WEB_URL + phone);

        By whatsappInput = By.cssSelector(AppConstant.WHATSAPP_INPUT_CSS_QUERY);
        WebDriverWait webDriverWait = new WebDriverWait(webDriver, Duration.ofSeconds(60));

        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(whatsappInput));

        WebElement inputElement = webDriver.findElement(whatsappInput);

        message = message.replace(AppConstant.NEW_LINE, Keys.chord(Keys.SHIFT, Keys.ENTER));

        inputElement.sendKeys(message);
        inputElement.sendKeys(Keys.ENTER);

    }
}
