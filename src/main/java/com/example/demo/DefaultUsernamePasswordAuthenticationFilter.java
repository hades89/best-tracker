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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

public class DefaultUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;

	private JwtUtil jwtUtil;

	private UserDetailsService userDetailsService;

	public DefaultUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil,
			UserDetailsService userDetailsService) {
		super();
		this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil;
		this.userDetailsService = userDetailsService;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
		ObjectMapper mapper = new ObjectMapper();
		UsernamePasswordAuthenticationToken authToken = null;
		try {
			AuthenticationRequest authenticationRequest = mapper.readValue(request.getInputStream(),
					AuthenticationRequest.class);

			authToken = new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
					authenticationRequest.getPassword());
		} catch (IOException e) {
			e.printStackTrace();
		}
		Authentication authResult = authenticationManager.authenticate(authToken);
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authResult.getName());
		final String jwt = jwtUtil.generateToken(userDetails);
		return new UsernamePasswordAuthenticationToken(jwt, null, userDetails.getAuthorities());
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		System.out.println("here 2");
		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
		ObjectMapper objectMapper= new ObjectMapper();
		String jsonString = objectMapper.writeValueAsString((String) authResult.getPrincipal());
		response.getWriter().write(jsonString);
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
