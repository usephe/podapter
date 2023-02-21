package com.audiophileproject.main.services;

import com.audiophileproject.main.models.User;
import com.audiophileproject.main.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;

	public Optional<User> findUserByUsername(String username) {
		return userRepository.findUserByUsername(username);
	}

	public User createUser(String username) {
		var user = User.builder()
				.username(username)
				.build();

		return userRepository.save(user);
	}
}
