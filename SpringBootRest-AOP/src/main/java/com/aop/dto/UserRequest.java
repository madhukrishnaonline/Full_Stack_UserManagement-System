package com.aop.dto;

import java.io.Serializable;
import java.util.Collection;

import lombok.Data;

@Data
public class UserRequest implements Serializable {

	private static final long serialVersionUID = 1L;
	private String id;
	private String username;
	private String password;
	private String fullName;
	private String email;
	private Long mobile;
	private String address;
	private Collection<String> roles;
}