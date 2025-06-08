package com.aop.service;

import java.util.List;

import com.aop.dto.TodoRequest;
import com.aop.dto.TodoResponse;
import com.aop.exception.UserNotFoundException;

public interface TodoService {

	TodoResponse createTodo(TodoRequest request) throws UserNotFoundException;

	List<TodoResponse> getAllTodos();
	
	TodoResponse getTodoById(Long id);

	TodoResponse updateTodo(TodoRequest request);

	boolean deleteTodoById(Long id);

	List<TodoResponse> getUsersTodoListById(String userId) throws UserNotFoundException;

	boolean deleteUsersTodoById(String id) throws UserNotFoundException;
}