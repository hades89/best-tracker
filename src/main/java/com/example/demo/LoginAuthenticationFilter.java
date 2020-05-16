package com.example.demo;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

public class LoginAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	private JwtUtil jwtUtil;

	private AuthenticationManager authenticationManager;

	private UserDetailsService userDetailsService;

	public LoginAuthenticationFilter(AuthenticationManager authenticationManager, UserDetailsService userDetailsService,
			JwtUtil jwtUtil) {
		super("/api/login");
		this.authenticationManager = authenticationManager;
		this.userDetailsService = userDetailsService;
		this.jwtUtil = jwtUtil;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		ObjectMapper mapper = new ObjectMapper();
		AuthenticationRequest authenticationRequest = mapper.readValue(request.getInputStream(),
				AuthenticationRequest.class);

		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
				authenticationRequest.getPassword()));

		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		final String jwt = jwtUtil.generateToken(userDetails);
		Authentication authentication = new UsernamePasswordAuthenticationToken(jwt, null,
				userDetails.getAuthorities());
		return authentication;
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		System.out.println("here 1");
		response.getWriter().write((String) authResult.getPrincipal());
		response.getWriter().flush();
		response.getWriter().close();
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException {
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		response.getWriter().write("Invalid username or password");
		response.getWriter().flush();
		response.getWriter().close();
	}

}
