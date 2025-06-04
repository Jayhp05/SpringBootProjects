package com.springbootstudy.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SpringbootstudyApp03Application {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootstudyApp03Application.class, args);
	}

}
