package com.spring.boot.reactive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;


import reactor.core.publisher.Mono;

@Configuration
public class WebSecurityConfig {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private SecurityContextRepository securityContextRepository;

	@Bean
	public SecurityWebFilterChain securitygWebFilterChain(ServerHttpSecurity http) {
		return http
				.csrf().disable()
				.formLogin().disable()
				.httpBasic().disable()
				
				.authenticationManager(authenticationManager)
				.securityContextRepository(securityContextRepository)
				
				.authorizeExchange()
				.pathMatchers("/create-token").permitAll()
				.anyExchange().authenticated()
				.and().build();
	}
}