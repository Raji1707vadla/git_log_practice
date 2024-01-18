package com.example.Phone.Pay.management;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class PhonePayManagementApplication {
	public static void main(String[] args) {
		SpringApplication.run(PhonePayManagementApplication.class, args);
        final Logger logger = LoggerFactory.getLogger( PhonePayManagementApplication.class );
        logger.info("************************************************************");
        logger.info("* PHONE PAY MANAGEMENT SERVER STARTED *");
        logger.info("************************************************************");
	}
	@Bean
	public BCryptPasswordEncoder cryptPasswordEncoder(){
		return new BCryptPasswordEncoder();
	}
}
