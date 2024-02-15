package com.wintech.wtuser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class WtUserApplication {

	public static void main(String[] args) {
		SpringApplication.run(WtUserApplication.class, args);
	}



}
