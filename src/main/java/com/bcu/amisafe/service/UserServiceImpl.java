package com.bcu.amisafe.service;

import com.bcu.amisafe.dto.UserRequestDTO;
import com.bcu.amisafe.dto.UserResponseDTO;
import com.bcu.amisafe.entity.User;
import com.bcu.amisafe.exception.ResourceNotFoundException;
import com.bcu.amisafe.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
         this.userRepository = userRepository;
    }

    public UserResponseDTO createUser(UserRequestDTO request) {
         if (userRepository.existsById(request.getEmail())) {
             throw new IllegalArgumentException("User already exists");
         }
         User user = new User();
         user.setUserId(request.getEmail());
         user.setEmail(request.getEmail());
         user.setName(request.getName());
         user.setMobile(request.getMobile());
         user.setDob(request.getDob());
         user.setPreferences(request.getPreferences());
         userRepository.save(user);
         return new UserResponseDTO("success", "User profile created successfully", user.getUserId());
    }

    public User getUserById(String userId) {
         Optional<User> user = userRepository.findById(userId);
         return user.orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public User updateUser(String userId, UserRequestDTO request) {
         User user = getUserById(userId);
         user.setName(request.getName());
         user.setMobile(request.getMobile());
         user.setDob(request.getDob());
         user.setPreferences(request.getPreferences());
         return userRepository.save(user);
    }

    public void deleteUser(String userId) {
         userRepository.deleteById(userId);
    }
}
