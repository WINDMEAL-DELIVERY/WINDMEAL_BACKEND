package com.windmeal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class WindmealApplication {

	public static void main(String[] args) {
		SpringApplication.run(WindmealApplication.class, args);
	}

}
