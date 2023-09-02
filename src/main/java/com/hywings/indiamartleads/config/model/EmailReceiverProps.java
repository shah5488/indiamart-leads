package com.hywings.indiamartleads.config.model;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "mail.receiver")
@Data
public class EmailReceiverProps {

    private String protocol;

    private String host;

    private String username;

    private String password;

    private String auditFile;

    private List<String> folders;

}
