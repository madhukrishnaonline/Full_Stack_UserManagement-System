package com.aop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aop.entity.ToDoEntity;

@Repository
public interface TodoRepository extends JpaRepository<ToDoEntity, Long> {
	List<ToDoEntity> findAllByUserId(String userId);
	void deleteByUserId(String userId);
}