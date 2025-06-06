package com.snipe.learning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableCaching
@EnableAsync
public class UplLearningPlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(UplLearningPlatformApplication.class, args);
	}
	
	 @Bean
	    RestTemplate restTemplate() {
	        return new RestTemplate();
	    }

}
