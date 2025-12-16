package com.capgemini.hmsbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class HmsbackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(HmsbackendApplication.class, args);
		//System.out.println("Application is running");
	}
    //code by sarvadnya
}
