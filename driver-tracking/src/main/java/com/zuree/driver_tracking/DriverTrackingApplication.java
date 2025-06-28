package com.zuree.driver_tracking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class DriverTrackingApplication {

	public static void main(String[] args) {
		SpringApplication.run(DriverTrackingApplication.class, args);
	}

}
