package com.hywings.indiamartleads;

import com.hywings.indiamartleads.service.WhatsAppService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.LocalDateTime;

@SpringBootApplication
@EnableScheduling
public class IndiamartLeadsApplication {

	public static void main(String[] args) {

		 ConfigurableApplicationContext cac =SpringApplication.run(IndiamartLeadsApplication.class, args);
		// WhatsAppService whatsAppService = cac.getBean(WhatsAppService.class);
		 //whatsAppService.sendMessage("test messages shared using selenium", "7977750995");
		System.out.println(StringUtils.substringBetween("test 'test1'", "'","'"));
}

}
