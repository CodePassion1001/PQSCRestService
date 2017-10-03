package com.proquest.ebd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

/**
* This is the Starter program class for SpringBoot Application
* This has been autocreated by the Spring Boot Initializer.
*
* @author  Purujit Saha
* @version 1.0
* @since   2017-05-10 
*/

@SpringBootApplication
@EnableAsync
public class EbdScbackendApplication {


	public static void main(String[] args) {
		SpringApplication.run(EbdScbackendApplication.class, args);
	}
	
	@Bean
	public MultipartResolver multipartResolver() {
	    return new StandardServletMultipartResolver();
	}
}
