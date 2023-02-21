package com.audiophileproject.main.filters;

import com.audiophileproject.main.models.User;
import com.audiophileproject.main.services.JwtService;
import com.audiophileproject.main.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtService jwtService;
	private final UserService userService;

	@Override
	protected void doFilterInternal(
			HttpServletRequest request,
			HttpServletResponse response,
			FilterChain filterChain
	) throws ServletException, IOException {
		final String jwt = extractJwtToken(request);
		if (jwt == null || !jwtService.isTokenValid(jwt)) {
			filterChain.doFilter(request, response);
			return;
		}

		final String username = jwtService.extractUsername(jwt);
		User user = userService.findUserByUsername(username).orElse(null);
		if (user == null)
			userService.createUser(username);

		Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, null);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		filterChain.doFilter(request, response);
	}

	private String extractJwtToken(HttpServletRequest request) {
		final String authHeader = request.getHeader("authorization");
		final String authPrefix = "Bearer ";
		if (authHeader == null || !authHeader.startsWith(authPrefix)) {
			return null;
		}

		return authHeader.substring(authPrefix.length());
	}
}