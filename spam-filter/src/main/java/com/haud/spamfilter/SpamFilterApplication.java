package com.haud.spamfilter;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.config.EnableIntegration;

@SpringBootApplication
@EnableIntegration
@IntegrationComponentScan
public class SpamFilterApplication {
	public static void main(final String[] args) {
		new SpringApplicationBuilder(SpamFilterApplication.class).web(WebApplicationType.NONE).run(args);
	}
}
