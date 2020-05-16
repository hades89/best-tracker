package com.example.demo;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailService implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		CurrentUser user = getByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("User not found");
		}
		return user;
	}

	private CurrentUser getByUsername(String username) {
		Set<CurrentUser> users = new HashSet<>();
		users.add(new CurrentUser("admin", "password", Arrays
				.asList(new SimpleGrantedAuthority("ROLE_" + Role.ROLE_ADMIN), new SimpleGrantedAuthority("CREATE")),
				"admin@gmail.com"));
		users.add(new CurrentUser("kenny", "pw1", Arrays.asList(new SimpleGrantedAuthority("ROLE_" + Role.ROLE_USER),
				new SimpleGrantedAuthority("CREATE")), "kenny@gmail.com"));
		users.add(new CurrentUser("smith", "pw2", Arrays.asList(new SimpleGrantedAuthority("ROLE_" + Role.ROLE_USER)),
				"smith@hotmail.com"));

		for (CurrentUser user : users) {
			if (user.getUsername().equals(username)) {
				return user;
			}
		}
		return null;
	}

}
