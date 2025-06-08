package com.aop.dto;

import java.util.Collection;

import lombok.Data;

@Data
public class UserResponse {
	private String id;
	private String username;
	private String password;
	private String fullName;
	private String email;
	private Long mobile;
	private String address;
	private Collection<String> roles;
}