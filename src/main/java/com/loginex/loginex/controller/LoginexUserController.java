package com.loginex.loginex.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loginex.loginex.model.LoginexUser;
import com.loginex.loginex.repository.LoginexUserRepository;

@RestController
@RequestMapping({ "/user" })
public class LoginexUserController {


	@Autowired
	private LoginexUserRepository repository;

	@GetMapping
	public List findAll() {
		return repository.findAll();
	}

	@GetMapping(value = "{id}")
	public ResponseEntity<?> findById(@PathVariable long id) {
		return repository.findById(id).map(user -> ResponseEntity.ok().body(user))
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public LoginexUser create(@RequestBody LoginexUser user) {
		user.setPassword(criptografarSenhar(user.getPassword()));
		return repository.save(user);
	}

	@PutMapping(value = "{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> update(@PathVariable long id, @RequestBody LoginexUser user) {
		return repository.findById(id).map(record -> {
			record.setName(user.getName());
			record.setUsername(user.getUsername());
			record.setPassword(criptografarSenhar(user.getPassword()));
			record.setAdmin(false);
			LoginexUser update = repository.save(record);
			return ResponseEntity.ok().body(update);
		}).orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping(path = { "{id}" })
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> delete(@PathVariable long id) {
		return repository.findById(id).map(record -> {
			repository.deleteById(id);
			return ResponseEntity.ok().body("Deletado com Sucesso");
		}).orElse(ResponseEntity.notFound().build());
	}

	private String criptografarSenhar(String password) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String criptPass = passwordEncoder.encode(password);
		return criptPass;
	}
}
