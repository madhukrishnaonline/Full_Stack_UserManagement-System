package com.aop.security;

import java.io.IOException;
import java.util.StringTokenizer;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.aop.SpringApplicationContext;
import com.aop.constants.SecurityConstants;
import com.aop.dto.UserRequest;
import com.aop.dto.UserResponse;
import com.aop.exception.UserNotFoundException;
import com.aop.service.UserService;
import com.aop.shared.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	public AuthenticationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		try {
			UserRequest userRequest = new ObjectMapper().readValue(request.getInputStream(), UserRequest.class);
			return getAuthenticationManager().authenticate(
					new UsernamePasswordAuthenticationToken(userRequest.getUsername(), userRequest.getPassword()));
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		String username = ((UserPrincipal) authResult.getPrincipal()).getUsername();
		UserService userService = SpringApplicationContext.fetchBean("userServiceImpl", UserService.class);
		UserResponse userResponse = null;
		try {
			userResponse = userService.findByUsername(username);
		} catch (UserNotFoundException e) {
			e.printStackTrace();
		}
		String token = SpringApplicationContext.fetchBean("jwtUtils", JwtUtils.class)
				.generateToken(userResponse.getUsername());

		StringTokenizer tokenizer = new StringTokenizer(userResponse.getFullName(), " ");
		String firstName = tokenizer.nextToken();

		response.setHeader(SecurityConstants.AUTH_HEADER, SecurityConstants.TOKEN_TYPE + token);
		response.setHeader(SecurityConstants.USER_ID, userResponse.getId());
		response.setHeader(SecurityConstants.USER_FIRST_NAME, firstName);
	}
}