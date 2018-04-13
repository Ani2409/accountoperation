package com.dws.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication(scanBasePackages = { "com.dws.controller", "com.dws.repository", "com.dws.service" })
public class MyMVCConfig extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(MyMVCConfig.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(MyMVCConfig.class);
	}
}
