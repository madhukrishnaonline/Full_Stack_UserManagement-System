package com.aop.controller;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.aop.dto.UserRequest;
import com.aop.dto.UserResponse;
import com.aop.exception.UserNotFoundException;
import com.aop.service.UserService;
import com.aop.shared.Role;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
//@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

	@Autowired
	private UserService service;

//	@Autowired
//	private HttpServletRequest httpServletRequest;

	@GetMapping("/")
	public ResponseEntity<String> home() {
		return ResponseEntity.ok("Welcome to User Management System");
	}

//	@Crypto
	@PostMapping("/register")
	public ResponseEntity<String> registerUser(@RequestBody UserRequest request) {
		/*ObjectMapper mapper = new ObjectMapper();
		UserRequest value = mapper.readValue(httpServletRequest.getInputStream().readAllBytes(),UserRequest.class);
		System.out.println(value);*/
		request.setRoles(new HashSet<>(Arrays.asList(Role.ROLE_USER.name())));
		boolean user = service.addUser(request);
		if (user) {
			return ResponseEntity.status(HttpStatus.CREATED).body("Created");
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Some Exception");
	}

	/*@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody UserLoginRequest request){
		return null;
	}*/

	@GetMapping("/id/{userId}")
	@PreAuthorize("hasRole('ADMIN') or #userId==principal.userId")
	public ResponseEntity<UserResponse> findByUserId(@PathVariable String userId) throws UserNotFoundException {
		UserResponse userResponse = service.findById(userId);
		return userResponse != null ? ResponseEntity.ok(userResponse) : ResponseEntity.notFound().build();
	}

	@GetMapping("/username/{username}")
	@PreAuthorize("hasRole('ADMIN') or #username==principal.username")
	public ResponseEntity<UserResponse> findByUsername(@PathVariable String username) throws UserNotFoundException {
		UserResponse userResponse = service.findByUsername(username);
		return userResponse != null ? ResponseEntity.ok(userResponse) : ResponseEntity.notFound().build();
	}

	@GetMapping("/mail/{mail}")
	@PreAuthorize("hasRole('ADMIN') or #mail==principal.mail")
	public ResponseEntity<UserResponse> findByMail(@PathVariable String mail) throws UserNotFoundException {
		UserResponse userResponse = service.findUserByEmail(mail);
		return userResponse != null ? ResponseEntity.ok(userResponse) : ResponseEntity.notFound().build();
	}

	@PostMapping("/newToken")
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	public ResponseEntity<String> generateNewTokenByUserConsentWithoutReLogin(HttpServletRequest request,
			HttpServletResponse response) {
		boolean isTokenGenerated = service.generateNewToken(request, response);
		return isTokenGenerated ? ResponseEntity.ok().body("You can continue")
				: ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	@PostMapping("/reset")
	public ResponseEntity<String> resetPassword(String oldPassword, String newPassword) {

		return null;
	}

	@GetMapping("/getAllUsers")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<UserResponse>> getAllUsers() {
		List<UserResponse> users = service.getAllUsers();
		return users.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(users);
	}

	@PutMapping("/update")
	@PreAuthorize("hasRole('ADMIN') or #request.userId==principal.userId")
	public ResponseEntity<String> updateUser(@RequestBody UserRequest request) throws Exception {
		String updateUser = service.updateUser(request);
		return ResponseEntity.status(HttpStatus.OK).body(updateUser);
	}

//	@Secured("ROLE_ADMIN")
	@DeleteMapping("/delete/{userId}")
	@PreAuthorize("hasRole('ADMIN') or #userId==principal.userId")
	public ResponseEntity<String> deleteUserData(@PathVariable String userId) throws UserNotFoundException {
		String response = service.deleteUserById(userId);
		return ResponseEntity.status(HttpStatus.GONE).body(response);
	}
}