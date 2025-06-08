package com.aop.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class TodoResponse {
	private Long id;
	private String name;
	private String description;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private boolean completed;
}