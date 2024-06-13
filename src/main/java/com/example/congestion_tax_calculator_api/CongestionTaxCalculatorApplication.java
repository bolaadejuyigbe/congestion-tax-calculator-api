package com.example.congestion_tax_calculator_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class CongestionTaxCalculatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(CongestionTaxCalculatorApplication.class, args);
	}

}
