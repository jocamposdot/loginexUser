package com.loginex.loginex.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loginex.loginex.model.LoginexUser;

public interface LoginexUserRepository extends JpaRepository<LoginexUser, Long>{
	LoginexUser findByUsername (String username);
}