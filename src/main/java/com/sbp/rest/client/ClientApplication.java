package com.sbp.rest.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@SpringBootApplication
public class ClientApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(ClientApplication.class);
		app.setDefaultProperties(Collections.singletonMap("server.port", "8080"));
		app.run(args);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public RestClient restClient() {
		return RestClient.create();
	}

}
