package com.suveechi.integration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class JsslIntegrationApplication {

	public static void main(String[] args) {
		SpringApplication.run(JsslIntegrationApplication.class, args);
	}

}
