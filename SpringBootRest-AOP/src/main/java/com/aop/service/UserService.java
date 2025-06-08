package com.aop.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.aop.dto.UserRequest;
import com.aop.dto.UserResponse;
import com.aop.exception.UserNotFoundException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface UserService extends UserDetailsService {
	boolean addUser(UserRequest request);

	String updateUser(UserRequest request) throws UserNotFoundException;

	List<UserResponse> getAllUsers();
	
	boolean existsById(String userId);
	
	boolean generateNewToken(HttpServletRequest request,HttpServletResponse response);

	UserResponse findById(String id) throws UserNotFoundException;

	UserResponse findUserByEmail(String email) throws UserNotFoundException;
	
	String deleteUserById(String userId) throws UserNotFoundException;

	boolean existsByUsername(String username);
	
	UserResponse findByUsername(String username) throws UserNotFoundException;
}