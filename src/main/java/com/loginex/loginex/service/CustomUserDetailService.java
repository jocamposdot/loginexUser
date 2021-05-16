package com.loginex.loginex.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.loginex.loginex.model.LoginexUser;
import com.loginex.loginex.repository.LoginexUserRepository;

@Component
public class CustomUserDetailService implements UserDetailsService {

	private final LoginexUserRepository loginexUserRepository;

	@Autowired
	public CustomUserDetailService(LoginexUserRepository loginexUserRepository) {
		this.loginexUserRepository = loginexUserRepository;
	}

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		LoginexUser user = Optional.ofNullable(loginexUserRepository.findByUsername(username))
				.orElseThrow(() -> new UsernameNotFoundException("Usuario não encontrado"));

		List<GrantedAuthority> authorityListAdmin = AuthorityUtils.createAuthorityList("ROLE_USER", "ROLE_ADMIN");
		List<GrantedAuthority> authorityListUser = AuthorityUtils.createAuthorityList("ROLE_USER");

		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				user.isAdmin() ? authorityListAdmin : authorityListUser);

	}
}
