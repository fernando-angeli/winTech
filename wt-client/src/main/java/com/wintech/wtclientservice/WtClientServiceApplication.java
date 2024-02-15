package com.wintech.wtclientservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class WtClientServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WtClientServiceApplication.class, args);
	}

}
