package com.aop.entity;

import java.util.Collection;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "UserEntity")
public class UserEntity {
	@Id
	private String id;
	private String username;
	private String password;
	private String fullName;
	private String email;
	private Long mobile;
	private String address;
	
	@ManyToMany(cascade = CascadeType.PERSIST,fetch = FetchType.EAGER)
	@JoinTable(name="User_Roles",joinColumns = @JoinColumn(name = "userId",referencedColumnName = "id"),inverseJoinColumns = @JoinColumn(name="roleId",referencedColumnName = "id"))
	private Collection<RoleEntity> roles;
	
	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "user")
	private List<ToDoEntity> listOfTodos;
}