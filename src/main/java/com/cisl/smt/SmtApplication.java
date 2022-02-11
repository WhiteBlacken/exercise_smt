package com.cisl.smt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class SmtApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(SmtApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(SmtApplication.class, args);
	}
}

//@SpringBootApplication
//public class SmtApplication {
//
//	public static void main(String[] args) {
//		SpringApplication.run(SmtApplication.class, args);
//	}
//}

