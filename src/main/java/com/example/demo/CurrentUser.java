package com.example.demo;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class CurrentUser extends User {

	private static final long serialVersionUID = 4187413978380955525L;
	private String email;

	public CurrentUser(String username, String password, Collection<? extends GrantedAuthority> authorities,
			String email) {
		super(username, password, authorities);
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
