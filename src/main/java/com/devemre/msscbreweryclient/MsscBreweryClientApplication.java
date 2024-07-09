package com.devemre.msscbreweryclient;

import com.devemre.msscbreweryclient.web.client.BreweryClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
public class MsscBreweryClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsscBreweryClientApplication.class, args);
	}

}
