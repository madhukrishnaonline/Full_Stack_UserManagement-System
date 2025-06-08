package com.aop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.aop.entity.UserEntity;

@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class SpringBootRestAopApplication {

	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/*@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) {
		try {
			return configuration.getAuthenticationManager();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}*/

	@Bean
	UserEntity entity() {
		return new UserEntity();
	}

	@Bean
	SpringApplicationContext springApplicationContext() {
		return new SpringApplicationContext();
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringBootRestAopApplication.class, args);
	}
}