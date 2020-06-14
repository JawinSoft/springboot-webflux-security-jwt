package com.spring.boot.reactive.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.spring.boot.reactive.dto.CreateTokenRequest;
//import com.spring.boot.reactive.service.AppSecurityUserDetailsService;
import com.spring.boot.reactive.service.JwtService;
import com.spring.boot.reactive.service.UserService;

import reactor.core.publisher.Mono;

@RestController
public class JwtController {

	
	@Autowired
	private UserService userService;

	@Autowired
	BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private JwtService jwtService;

	@PostMapping("/create-token")
	public Mono<String> createToken(@RequestBody CreateTokenRequest request) {

		Mono<UserDetails> userDetails = userService.findByUsername(request.getUsername());
		
		return userDetails.flatMap(user -> {
			if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {

				return Mono.just(jwtService.generateToken(request.getUsername(), user));
			}

			return Mono.error(new RuntimeException("Invalid User to Login"));

		});
	}
	
	
	@GetMapping("/hi-user")
	@PreAuthorize(value = "USER")
	public String sayHello() {
		return "test test test";
	}
	
	@GetMapping("/hi-admin")
	@PreAuthorize(value = "ADMIN")
	public String sayHelloAdmin() {
		return "test test test";
	}

}
