package com.aop.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aop.constants.SecurityConstants;
import com.aop.dto.TodoRequest;
import com.aop.dto.TodoResponse;
import com.aop.entity.ToDoEntity;
import com.aop.entity.UserEntity;
import com.aop.exception.UserNotFoundException;
import com.aop.repository.TodoRepository;
import com.aop.repository.UserRepository;
import com.aop.singleton.SingletonModelMapper;

@Service
public class TodoServiceImpl implements TodoService {

	@Autowired
	private TodoRepository todoRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	public TodoResponse createTodo(TodoRequest request) throws UserNotFoundException {
		UserEntity userEntity = userRepository.findById(request.getUserId())
				.orElseThrow(() -> new UserNotFoundException(SecurityConstants.USER_NOT_FOUND));
		ToDoEntity toDoEntity = SingletonModelMapper.mapData(request, ToDoEntity.class);
		toDoEntity.setUser(userEntity);
		ToDoEntity toDoEntity2 = todoRepository.save(toDoEntity);
		return SingletonModelMapper.mapData(toDoEntity2, TodoResponse.class);
	}

	@Override
	public List<TodoResponse> getAllTodos() {
		List<ToDoEntity> list = todoRepository.findAll();
		List<TodoResponse> responses = new ArrayList<>();
		list.forEach(todoEntity -> responses.add(SingletonModelMapper.mapData(todoEntity, TodoResponse.class)));
		return responses;
	}

	@Override
	public List<TodoResponse> getUsersTodoListById(String userId) throws UserNotFoundException {
		UserEntity userEntity = userRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundException(SecurityConstants.USER_NOT_FOUND));
		List<ToDoEntity> todoEntities = todoRepository.findAllByUserId(userEntity.getId());
		List<TodoResponse> todoResponses = new ArrayList<>();
		todoEntities.forEach(todoEntity -> {
			todoResponses.add(SingletonModelMapper.mapData(todoEntity, TodoResponse.class));
		});
		return todoResponses;
	}

	@Override
	public TodoResponse getTodoById(Long id) {
		Optional<ToDoEntity> toDoEntity = todoRepository.findById(id);
		return toDoEntity.isPresent() ? SingletonModelMapper.mapData(toDoEntity.get(), TodoResponse.class) : null;
	}

	@Override
	public TodoResponse updateTodo(TodoRequest request) {
		ToDoEntity toDoEntity = todoRepository.findById(request.getId()).orElseThrow();
		toDoEntity.setName(request.getName());
		toDoEntity.setDescription(request.getDescription());
		toDoEntity.setEndTime(request.getEndTime());
		toDoEntity.setCompleted(request.isCompleted());
		toDoEntity = todoRepository.save(toDoEntity);
		return SingletonModelMapper.mapData(toDoEntity, TodoResponse.class);
	}

	@Override
	public boolean deleteTodoById(Long id) {
		Optional<ToDoEntity> optional = todoRepository.findById(id);
		if (optional.isPresent()) {
			todoRepository.deleteById(id);
			return true;
		}
		return false;
	}

	@Override
	public boolean deleteUsersTodoById(String id) throws UserNotFoundException {
		UserEntity userEntity = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException(SecurityConstants.USER_NOT_FOUND));
		todoRepository.deleteByUserId(userEntity.getId());
		return true;
	}
}