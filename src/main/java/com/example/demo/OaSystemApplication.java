package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.example.demo.mapper")
public class OaSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(OaSystemApplication.class, args);
	}

}
