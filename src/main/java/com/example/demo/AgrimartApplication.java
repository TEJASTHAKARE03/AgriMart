package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

// Exclude only the specific auto-configuration that creates the default user.
// This prevents the security conflict while allowing our custom SecurityConfig to function correctly.
@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class})
public class AgrimartApplication {

	public static void main(String[] args) {
		SpringApplication.run(AgrimartApplication.class, args);
	}

}
