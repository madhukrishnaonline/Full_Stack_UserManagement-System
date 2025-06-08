package com.aop.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aop.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
	Optional<UserEntity> findByEmail(String email);

	Optional<UserEntity> findByUsername(String username);

	boolean existsByEmail(String email);

	boolean existsByUsername(String username);
}