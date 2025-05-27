package com.snipe.learning.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.snipe.learning.service.UserDetailsServiceImpl;
import com.snipe.learning.utility.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
    private UserDetailsServiceImpl userServiceImpl;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		final String authHeader =  request.getHeader("Authorization");
		
		String email =  null;
		String Jwt = null;
		
		if(authHeader != null && authHeader.startsWith("Bearer ")) {
			Jwt = authHeader.substring(7);
			email = jwtUtil.extractEmail(Jwt);
		}
		
		if(email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			
			UserDetails userDetails = userServiceImpl.loadUserByUsername(email);
			if(jwtUtil.validateToken(Jwt, userDetails)) {
				UsernamePasswordAuthenticationToken authToken =   new UsernamePasswordAuthenticationToken
						(userDetails, null, userDetails.getAuthorities());
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);

			}
		}
		filterChain.doFilter(request, response);
	}
	

}
