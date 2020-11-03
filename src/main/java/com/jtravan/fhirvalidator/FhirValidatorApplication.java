package com.jtravan.fhirvalidator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Spring boot entry point for the FHIR Validator API Application
 */
@ComponentScan(value = {"com.jtravan"})
@SpringBootApplication
public class FhirValidatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(FhirValidatorApplication.class, args);
	}

}

