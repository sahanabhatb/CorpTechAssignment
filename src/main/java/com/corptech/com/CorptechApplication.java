package com.corptech.com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "com.corptech.com.repository")
@SpringBootApplication
public class CorptechApplication extends SpringBootServletInitializer  {

	public static void main(String[] args) {
		SpringApplication.run(CorptechApplication.class, args);
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		  return application.sources(CorptechApplication.class);
		 }
	
}
