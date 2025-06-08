package com.aop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aop.entity.RoleEntity;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

	RoleEntity findByName(String name);

}