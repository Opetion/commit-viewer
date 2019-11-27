package com.opetion.seagit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class AppStart {
	private static final Logger logger = LoggerFactory.getLogger(AppStart.class);

	public static void main(String[] args) {
		SpringApplication.run(AppStart.class, args);
	}
}
