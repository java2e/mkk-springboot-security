package com.example.mkksecurity.controller;


import com.example.mkksecurity.models.User;
import com.example.mkksecurity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	/*
	Sadece admin alınması
	 */

	@GetMapping("/list")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getUserList(){

		List<User> list = userRepository.findAll();

		return ResponseEntity.ok(list);

	}

	@GetMapping("/userInfo")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> getUserInfoById(@RequestParam Long id) {
		User user = userRepository.findById(id).get();
		return ResponseEntity.ok(user);
	}

}
