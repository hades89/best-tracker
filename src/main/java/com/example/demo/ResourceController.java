package com.example.demo;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/resource")
public class ResourceController {

	
	@PreAuthorize("hasRole('ROLE_ADMIN') and hasAuthority('CREATE')")
	@GetMapping
	public String get() {
		System.out.println("Name: "+SecurityContextHolder.getContext().getAuthentication().getName());
		System.out.println("Principle: "+SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		System.out.println("Credentials: "+SecurityContextHolder.getContext().getAuthentication().getCredentials());
		System.out.println("Authorities: "+SecurityContextHolder.getContext().getAuthentication().getAuthorities());
		return "resource success";
	}
}
