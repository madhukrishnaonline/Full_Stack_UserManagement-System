package com.aop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aop.entity.AuthorityEntity;

public interface AuthorityRepository extends JpaRepository<AuthorityEntity, Long> {

	AuthorityEntity findByName(String name);

}