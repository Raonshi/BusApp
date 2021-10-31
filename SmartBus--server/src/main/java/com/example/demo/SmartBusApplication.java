package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableCaching
@EnableJpaAuditing
@SpringBootApplication
public class SmartBusApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartBusApplication.class, args);
	}

}
