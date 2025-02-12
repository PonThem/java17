package com.valuelab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class ValueLabApplication {

	public static void main(String[] args) {
		SpringApplication.run(ValueLabApplication.class, args);
	}

}
