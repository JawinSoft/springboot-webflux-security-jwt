package com.spring.boot.reactive;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.spring.boot.reactive.service.JwtService;
import com.spring.boot.reactive.service.UserService;

import reactor.core.publisher.Mono;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ard333
 */
@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {

	@Autowired
	private JwtService jwtUtil;
	
	@Autowired
	private UserService userService;
	
	@Override
	@SuppressWarnings("unchecked")
	public Mono<Authentication> authenticate(Authentication authentication) {
		String authToken = authentication.getCredentials().toString();
		
		try {
			String username = jwtUtil.extractUsername(authToken);
			
			Mono<UserDetails>  user = userService.findByUsername(username);
			
		return	user.flatMap( u -> {
			
			if (!jwtUtil.validateToken(authToken, u)) {
				return Mono.empty();
			}
			Claims claims = jwtUtil.extractAllClaims(authToken);
			List rolesMap = claims.get("role", List.class);
			
			List<GrantedAuthority> authorities = new ArrayList<>();
			
           for(Object obj : rolesMap) {
        	   Map<String, String> map = (Map<String, String>)obj;
        	   map.forEach((k, v) -> {
        			authorities.add(new SimpleGrantedAuthority(v));
        	   });
        	   
           }
			
			
			//Collection<GrantedAuthority>  authorities= (Collection<GrantedAuthority>)rolesMap;
			return Mono.just(new UsernamePasswordAuthenticationToken(username, null, authorities));
		
		
		});
		
		} catch (Exception e) {
			return Mono.empty();
		}
	}
}
