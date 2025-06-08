package com.aop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.aop.entity.AuthorityEntity;
import com.aop.entity.RoleEntity;
import com.aop.entity.UserEntity;
import com.aop.repository.AuthorityRepository;
import com.aop.repository.RoleRepository;
import com.aop.repository.UserRepository;
import com.aop.security.AppProperties;
import com.aop.shared.Role;
import com.aop.shared.Utils;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class InitialSetUp {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private AuthorityRepository authorityRepository;

	@Autowired
	private BCryptPasswordEncoder encoder;

	@Autowired
	private AppProperties properties;

	@Transactional
	@EventListener
	public void setUpAdmin(ApplicationReadyEvent event) {
		log.info("InitialSetUp.setUpAdmin()" + event.getClass().getName());

		AuthorityEntity readAuthority = saveAuthorities("read_authority");
		AuthorityEntity writeAuthority = saveAuthorities("write_authority");
		AuthorityEntity deleteAuthority = saveAuthorities("delete_authority");

		saveRoles(Role.ROLE_USER.name(), new ArrayList<>(Arrays.asList(readAuthority, writeAuthority)));
		RoleEntity roleAdmin = saveRoles(Role.ROLE_ADMIN.name(),
				new ArrayList<>(Arrays.asList(readAuthority, writeAuthority, deleteAuthority)));

		if (roleAdmin == null) {
			log.error("Admin Role is Null,cannot proceed");
			return;
		}
		UserEntity admin = new UserEntity();
		admin.setId(Utils.generateUserId(15));
		admin.setUsername("madhukrishnaonline");
		admin.setFullName("Madhu Krishna");
		admin.setPassword(encoder.encode(properties.getDefaultAdminPswrd()));
		admin.setEmail(properties.getAdminMail());
		admin.setMobile(9876543210L);
		admin.setAddress("Hyderabad");
		admin.setRoles(new ArrayList<>(Arrays.asList(roleAdmin)));

		if (!userRepository.existsByEmail(admin.getEmail())) {
			userRepository.save(admin);
		}
	}

	private RoleEntity saveRoles(String name, Collection<AuthorityEntity> authorityEntities) {
		RoleEntity roleEntity = roleRepository.findByName(name);
		if (roleEntity == null) {
			roleEntity = new RoleEntity(name);
			roleEntity.setAuthorities(authorityEntities);
			return roleRepository.save(roleEntity);
		}
		return roleEntity;
	}

	private AuthorityEntity saveAuthorities(String name) {
		AuthorityEntity authorityEntity = authorityRepository.findByName(name);
		if (authorityEntity == null) {
			authorityEntity = new AuthorityEntity(name);
			return authorityRepository.save(authorityEntity);
		}
		return authorityEntity;
	}
}