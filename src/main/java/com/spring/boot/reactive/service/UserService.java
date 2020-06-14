package com.spring.boot.reactive.service;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Service
public class UserService {
	
	public Mono<UserDetails> findByUsername(String username) {
		
		return Mono.just(User.builder()
							.username("user")
							.password("$2y$12$cH1xAIp6mHuhejmnPsX.H.4js2w5Urx7E9IgGOnJSiaLozc40GYKW")
							.disabled(false)
							.roles("USER")
							.build());
	}
	
}
