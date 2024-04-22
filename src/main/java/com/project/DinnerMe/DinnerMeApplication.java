package com.project.DinnerMe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.project.DinnerMe")
public class DinnerMeApplication {

	public static void main(String[] args) {
		SpringApplication.run(DinnerMeApplication.class, args);
	}

}
