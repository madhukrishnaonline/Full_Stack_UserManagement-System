package com.aop.security;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.aop.service.UserService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SpringSecurityConfig {

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity security, UserService userService,
			BCryptPasswordEncoder bCryptPasswordEncoder) throws Exception {
		AuthenticationManagerBuilder authenticationManagerBuilder = security
				.getSharedObject(AuthenticationManagerBuilder.class);
		authenticationManagerBuilder.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
		AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

		/*AuthenticationFilter filter = new AuthenticationFilter(userService, jwtService);
		filter.setAuthenticationManager(authenticationManager);*/

		return security.cors(cors -> cors.configurationSource(corsConfiguration())).csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(
						request -> request.requestMatchers("/register", "/", "/h2-console/**", "/login/**").permitAll()
								.anyRequest().authenticated())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilter(new AuthenticationFilter(authenticationManager))
				.addFilter(new AuthorizationFilter(authenticationManager)).authenticationManager(authenticationManager)
				.headers(headers -> headers.frameOptions(frames -> frames.sameOrigin())).build();
	}

	@Bean
	CorsConfigurationSource corsConfiguration() {
		CorsConfiguration configurer = new CorsConfiguration();
		configurer.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
		configurer.setAllowedMethods(Arrays.asList("PUT", "GET", "POST", "PATCH", "DELETE", "OPTIONS"));
		configurer.setAllowCredentials(true);
		configurer.setAllowedHeaders(Arrays.asList("*"));
		configurer.setExposedHeaders(Arrays.asList("*"));

		UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
		urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", configurer);
		return urlBasedCorsConfigurationSource;
	}
}