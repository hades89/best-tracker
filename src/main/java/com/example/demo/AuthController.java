package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private MyUserDetailService userDetailService;

//	@Autowired
//	private JwtUtil jwtUtil;

	@PostMapping("/api/auth")
	public ResponseEntity<?> createAuthToken(@RequestBody AuthenticationRequest authenticationRequest)
			throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					authenticationRequest.getUsername(), authenticationRequest.getPassword()));
		} catch (BadCredentialsException e) {
			throw new Exception("Invalid username or password");
		}
		final UserDetails userDetails = userDetailService.loadUserByUsername(authenticationRequest.getUsername());
//		final String jwt = jwtUtil.generateToken(userDetails);
		return ResponseEntity.ok("qq");
	}
}
