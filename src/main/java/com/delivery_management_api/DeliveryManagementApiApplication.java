package com.delivery_management_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DeliveryManagementApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeliveryManagementApiApplication.class, args);
	}

}
