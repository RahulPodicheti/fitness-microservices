package com.fitness.userservice.services;

import org.springframework.stereotype.Service;

import com.fitness.userservice.dto.RegisterRequest;
import com.fitness.userservice.dto.UserResponse;
import com.fitness.userservice.model.User;
import com.fitness.userservice.repositories.UserRepository;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {
	
	private UserRepository userRepository;

	public UserResponse getUserProfile(String userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(()-> new RuntimeException("User not found"));
		UserResponse userResponse = new UserResponse();
		userResponse.setCreatedAt(user.getCreatedAt());
		userResponse.setEmail(user.getEmail());
		userResponse.setFirstName(user.getFirstName());
		userResponse.setId(user.getId());
		userResponse.setLastName(user.getLastName());
		userResponse.setPassword(user.getPassword());
		userResponse.setUpdatedAt(user.getUpdatedAt());
		return userResponse;
	}

	public UserResponse register(@Valid RegisterRequest request) {
		
		if(userRepository.existsByEmail(request.getEmail())) {
			User existingUser = userRepository.findByEmail(request.getEmail());
			UserResponse userResponse = new UserResponse();
			userResponse.setCreatedAt(existingUser.getCreatedAt());
			userResponse.setEmail(existingUser.getEmail());
			userResponse.setFirstName(existingUser.getFirstName());
			userResponse.setId(existingUser.getId());
			userResponse.setKeycloakId(existingUser.getKeycloakId());
			userResponse.setLastName(existingUser.getLastName());
			userResponse.setPassword(existingUser.getPassword());
			userResponse.setUpdatedAt(existingUser.getUpdatedAt());
			return userResponse;
		}
		User user = new User();
		user.setEmail(request.getEmail());
		user.setPassword(request.getPassword());
		user.setKeycloakId(request.getKeycloakId());
		user.setFirstName(request.getFirstName());
		user.setLastName(request.getLastName());
		
		User savedUser = userRepository.save(user);
		
		UserResponse userResponse = new UserResponse();
		userResponse.setCreatedAt(savedUser.getCreatedAt());
		userResponse.setEmail(savedUser.getEmail());
		userResponse.setFirstName(savedUser.getFirstName());
		userResponse.setKeycloakId(savedUser.getKeycloakId());
		userResponse.setId(savedUser.getId());
		userResponse.setLastName(savedUser.getLastName());
		userResponse.setPassword(savedUser.getPassword());
		userResponse.setUpdatedAt(savedUser.getUpdatedAt());
		return userResponse;
	}

	public boolean validateUser(String userId) {
		log.info("Calling User Validation API for userId: {}"+userId);
		return userRepository.existsByKeycloakId(userId);
	}
}
