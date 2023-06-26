package com.umulam.fleen.health;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.umulam"})
public class FleenHealthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FleenHealthServiceApplication.class, args);
	}

}
