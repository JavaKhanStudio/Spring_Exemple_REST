package com.sbp.rest.server;

import com.sbp.rest.client.ClientApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collections;

@SpringBootApplication
public class ServerApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(ServerApplication.class);
		app.setDefaultProperties(Collections.singletonMap("server.port", "8085"));
		app.run(args);
	}

}
