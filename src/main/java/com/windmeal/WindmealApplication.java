package com.windmeal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WindmealApplication {

	public static void main(String[] args) {
		System.out.println("SADFDASFASDFD"+org.hibernate.Version.getVersionString());
		SpringApplication.run(WindmealApplication.class, args);
	}

}
