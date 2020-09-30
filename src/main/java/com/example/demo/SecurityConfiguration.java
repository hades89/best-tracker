package com.example.demo;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Order(1)
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableAsync
@EnableScheduling
@EnableCaching
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Qualifier("myUserDetailService")
	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Autowired
	private AccessDeniedHandler accessDeniedHandler;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}

	@Bean
	public JwtUtil jwtUtil() {
		return new JwtUtil();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder builder) throws Exception {
		builder.userDetailsService(userDetailsService);
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().cors().and().authorizeRequests()
				.antMatchers("/oauth/authorize**", "/login**", "/error**", "/swagger-ui/**", "/api/resource/subscribe/**")
        		.permitAll()
//				.antMatchers("/api/resource")
//				.hasRole(Role.ROLE_ADMIN)
				.anyRequest().authenticated()
        		.and()
				.exceptionHandling()
				.accessDeniedHandler(accessDeniedHandler).and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				.addFilterBefore(new DefaultUsernamePasswordAuthenticationFilter(authenticationManagerBean(), jwtUtil(),
						userDetailsService), UsernamePasswordAuthenticationFilter.class)
				.addFilterAfter(jwtRequestFilter, DefaultUsernamePasswordAuthenticationFilter.class);
	}
	
	@Bean
	public CorsFilter corsFilter() {
	    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    final CorsConfiguration config = new CorsConfiguration();
	    config.setAllowedOrigins(Collections.singletonList("*")); // Provide list of origins if you want multiple origins
	    config.setAllowedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Access-Control-Allow-Headers", "X-Requested-With", "Authorization"));
	    config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "OPTIONS", "DELETE", "PATCH"));
	    config.setAllowCredentials(true);
	    source.registerCorsConfiguration("/**", config);
	    return new CorsFilter(source);
	}

	@Bean
	public CacheManager cacheManager(){
		return new ConcurrentMapCacheManager();
	}

	/**
	 * old
	 */
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		http.csrf().disable().authorizeRequests().antMatchers("/api/auth").permitAll().antMatchers("/api/resource")
//				.hasRole(Role.ROLE_ADMIN).anyRequest().authenticated().and().exceptionHandling()
//				.accessDeniedHandler(accessDeniedHandler).and().sessionManagement()
//				.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
////				.addFilterBefore(
////						new LoginAuthenticationFilter(authenticationManagerBean(), userDetailsService, jwtUtil()),
////						UsernamePasswordAuthenticationFilter.class)
//				.addFilterBefore(new DefaultUsernamePasswordAuthenticationFilter(authenticationManagerBean(), jwtUtil(),
//						userDetailsService), UsernamePasswordAuthenticationFilter.class)
//				.addFilterAfter(jwtRequestFilter, DefaultUsernamePasswordAuthenticationFilter.class);
//	}
}
