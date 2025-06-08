
package com.aop.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.aop.SpringApplicationContext;
import com.aop.constants.SecurityConstants;
import com.aop.dto.UserRequest;
import com.aop.dto.UserResponse;
import com.aop.entity.RoleEntity;
import com.aop.entity.UserEntity;
import com.aop.exception.UserNotFoundException;
import com.aop.repository.RoleRepository;
import com.aop.repository.UserRepository;
import com.aop.security.UserPrincipal;
import com.aop.shared.JwtUtils;
import com.aop.singleton.SingletonModelMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private BCryptPasswordEncoder encoder;

	@Override
	public boolean addUser(UserRequest request) {
		UserEntity entity = SingletonModelMapper.mapData(request, UserEntity.class);
		entity.setId(UUID.randomUUID().toString());
		String passowrd = encoder.encode(request.getPassword());
		entity.setPassword(passowrd);

		Collection<RoleEntity> roleEntities = new ArrayList<RoleEntity>();
		request.getRoles().forEach(role -> {
			RoleEntity roleEntity = roleRepository.findByName(role);
			if (roleEntity != null) {
				roleEntities.add(roleEntity);
			}
		});
		entity.setRoles(roleEntities);
		UserEntity userEntity = userRepository.save(entity);
		return userEntity != null;
	}

	@Override
	public boolean generateNewToken(HttpServletRequest request, HttpServletResponse response) {
		JwtUtils jwtService = SpringApplicationContext.fetchBean("jwtUtils", JwtUtils.class);
		String authHeader = request.getHeader(SecurityConstants.AUTH_HEADER);
		String username = null;
		String oldToken = null;
		if (authHeader != null && authHeader.startsWith(SecurityConstants.TOKEN_TYPE)) {
			oldToken = authHeader.substring(SecurityConstants.TOKEN_TYPE.length()).trim();
			username = jwtService.extractUsername(oldToken);
		} else {
			return false;
		}
		if (username == null || oldToken == null || !existsByUsername(username)) {
			return false;
		}
		Date expirationTime = jwtService.extractExpirationTime(oldToken);
		if (!(expirationTime.before(new Date()))) {
			final String token = jwtService.generateToken(username);
			response.setHeader(SecurityConstants.AUTH_HEADER, SecurityConstants.TOKEN_TYPE + token);
			return true;
		}
		return false;
	}

	@Override
	public boolean existsByUsername(String username) {
		return userRepository.existsByUsername(username);
	}

	@Override
	public UserResponse findByUsername(String username) throws UserNotFoundException {
		UserEntity userEntity = userRepository.findByUsername(username)
				.orElseThrow(() -> new UserNotFoundException(SecurityConstants.USER_NOT_FOUND));
		return SingletonModelMapper.mapData(userEntity, UserResponse.class);
	}

	@Override
	public boolean existsById(String userId) {
		return userRepository.existsById(userId);
	}

	@Override
	public UserResponse findById(String id) throws UserNotFoundException {
		UserEntity entity = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException(SecurityConstants.USER_NOT_FOUND));

		Collection<String> roles = new HashSet<>();
		entity.getRoles().forEach(roleEntity -> roles.add(roleEntity.getName()));
		UserResponse userResponse = new UserResponse();
		userResponse.setRoles(roles);

		return SingletonModelMapper.mapData(entity, userResponse);
	}

	@Override
	public UserResponse findUserByEmail(String email) throws UserNotFoundException {
		UserEntity entity = userRepository.findByEmail(email)
				.orElseThrow(() -> new UserNotFoundException(SecurityConstants.USER_NOT_FOUND));
		Collection<String> roles = new HashSet<>();
		entity.getRoles().forEach(roleEntity -> roles.add(roleEntity.getName()));

		UserResponse userResponse = new UserResponse();
		userResponse.setRoles(roles);
		return SingletonModelMapper.mapData(entity, userResponse);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity entity = userRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException(SecurityConstants.USER_NOT_FOUND));
		return new UserPrincipal(entity);
	}

	@Override
//	@ExecutionTime
	public List<UserResponse> getAllUsers() {
		List<UserEntity> list = userRepository.findAll();
		if (!list.isEmpty()) {
			List<UserResponse> responses = new ArrayList<>();
			for (UserEntity entity : list) {
				Collection<String> roles = new HashSet<>();
				Collection<RoleEntity> roleEntities = entity.getRoles();
				roleEntities.forEach(roleEntity -> roles.add(roleEntity.getName()));

				UserResponse userResponse = new UserResponse();
				userResponse.setRoles(roles);

				responses.add(SingletonModelMapper.mapData(entity, userResponse));
			}
			return responses;
		}
		throw new RuntimeException(SecurityConstants.DATA_NOT_FOUND);
	}

	@Override
	public String updateUser(UserRequest request) throws UserNotFoundException {
		UserEntity entity = userRepository.findById(request.getId())
				.orElseThrow(() -> new UserNotFoundException(SecurityConstants.USER_NOT_FOUND));
		SingletonModelMapper.mapData(request, entity);
		userRepository.save(entity);
		return SecurityConstants.USER_UPDATED;
	}

	@Override
	public String deleteUserById(String userId) throws UserNotFoundException {
		UserEntity entity = userRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundException(SecurityConstants.USER_NOT_FOUND));
		userRepository.delete(entity);
		return "User Deleted " + userId;
	}
}