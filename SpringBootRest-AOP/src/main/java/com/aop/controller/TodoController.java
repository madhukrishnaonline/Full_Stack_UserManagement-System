package com.aop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aop.dto.TodoRequest;
import com.aop.dto.TodoResponse;
import com.aop.exception.UserNotFoundException;
import com.aop.service.TodoService;

@RestController
@RequestMapping("/todo/")
public class TodoController {

	@Autowired
	private TodoService service;

	@PostMapping("create")
//	@PostAuthorize("hasRole('ADMIN')") // response should only seen by ADMIN
	@PreAuthorize("hasAnyRole('ADMIN','USER')") // Any one can create to do
	public ResponseEntity<TodoResponse> createTodo(@RequestBody TodoRequest request, Authentication authentication)
			throws UserNotFoundException {
		TodoResponse todoResponse = service.createTodo(request);
		boolean isAdmin = authentication.getAuthorities().stream()
				.anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));
		if (todoResponse == null) {
			ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Some Error");
		}
		return isAdmin ? ResponseEntity.status(HttpStatus.CREATED).body(todoResponse)
				: ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@GetMapping("getAllTodos")
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	public ResponseEntity<List<TodoResponse>> getAllTodos() {
		List<TodoResponse> list = service.getAllTodos();
		return list.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.status(HttpStatus.OK).body(list);
	}

	@GetMapping("getAllTodos/{userId}")
	public ResponseEntity<List<TodoResponse>> getUserTodosById(@PathVariable String userId)
			throws UserNotFoundException {
		List<TodoResponse> usersTodoList = service.getUsersTodoListById(userId);
		return usersTodoList.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(usersTodoList);
	}

	@GetMapping("getTodo/{id}")
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	public ResponseEntity<TodoResponse> getTodoById(@PathVariable Long id) {
		TodoResponse todoResponse = service.getTodoById(id);
		return todoResponse != null ? ResponseEntity.ok(todoResponse) : ResponseEntity.notFound().build();
	}

	@PutMapping("update")
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	public ResponseEntity<TodoResponse> updateTodo(@RequestBody TodoRequest request) {
		TodoResponse todoResponse = service.updateTodo(request);
		return todoResponse != null ? ResponseEntity.ok(todoResponse)
				: ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}

	@DeleteMapping("delete/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> deleteTodoById(@PathVariable Long id) {
		boolean response = service.deleteTodoById(id);
		return response ? ResponseEntity.ok("Deleted") : ResponseEntity.notFound().build();
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("delete/todos/{userId}")
	public ResponseEntity<String> deleteUserTodoListById(@PathVariable String userId) throws UserNotFoundException {
		boolean response = service.deleteUsersTodoById(userId);
		return response ? ResponseEntity.ok("Deleted") : ResponseEntity.notFound().build();
	}
}
/*getAll todo's(everyone),create todo (request can be sent only by admin and 
 * logged in user,response can be only viewed by only admin),
 * delete todo(only admin ),update todo(logged in user and admin)*/