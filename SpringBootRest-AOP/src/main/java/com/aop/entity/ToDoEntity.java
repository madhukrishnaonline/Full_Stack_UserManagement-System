package com.aop.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "Todo_Entity")
public class ToDoEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String name;
	private String description;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private boolean completed;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="User_Id")
	private UserEntity user;
}