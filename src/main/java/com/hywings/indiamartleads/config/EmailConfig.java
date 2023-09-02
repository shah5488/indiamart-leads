package com.hywings.indiamartleads.config;

import com.hywings.indiamartleads.config.model.EmailReceiverProps;
import com.hywings.indiamartleads.util.AppConstant;
import jakarta.mail.MessagingException;
import jakarta.mail.NoSuchProviderException;
import jakarta.mail.Session;
import jakarta.mail.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class EmailConfig {

    @Autowired
    private EmailReceiverProps emailReceiverProps;

    @Bean
    public Store getStore() throws MessagingException {

        Properties props = new Properties();
        props.setProperty(AppConstant.MAIL_STORE_PROTOCOL, emailReceiverProps.getProtocol());

        Session session = Session.getInstance(props, null);
        Store store = session.getStore();
        store.connect(emailReceiverProps.getHost(), emailReceiverProps.getUsername(), emailReceiverProps.getPassword());

        return store;

    }
}
