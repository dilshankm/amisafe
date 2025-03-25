package com.bcu.amisafe.service;

import com.bcu.amisafe.constants.Constants;
import com.bcu.amisafe.dto.UserRequestDTO;
import com.bcu.amisafe.dto.UserResponseDTO;
import com.bcu.amisafe.entity.User;
import com.bcu.amisafe.exception.ResourceNotFoundException;
import com.bcu.amisafe.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponseDTO createUser(UserRequestDTO request) {
        userRepository.findByEmail(request.getEmail())
                .ifPresent(user -> { throw new IllegalArgumentException(Constants.USER_ALREADY_EXISTS); });
        User user = User.builder().email(request.getEmail()).name(request.getName()).mobile(request.getMobile()).dob(request.getDob()).currentLocation(request.getCurrentLocation()).preferences(request.getPreferences()).build();
        userRepository.save(user);
        return new UserResponseDTO(Constants.SUCCESS, Constants.USER_PROFILE_CREATED_SUCCESSFULLY, user.getEmail());
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(Constants.USER_NOT_FOUND));
    }

    public UserResponseDTO updateUser(String email, UserRequestDTO request) {
        User user = userRepository.findByEmail(email).
                orElseThrow(() -> new ResourceNotFoundException(Constants.USER_NOT_FOUND));
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setMobile(request.getMobile());
        user.setDob(request.getDob());
        user.setCurrentLocation(request.getCurrentLocation());
        user.setPreferences(request.getPreferences());
        userRepository.save(user);
        return new UserResponseDTO(Constants.SUCCESS, Constants.USER_PROFILE_UPDATED_SUCCESSFULLY, user.getEmail());
    }

    public void deleteUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(Constants.USER_NOT_FOUND));
        userRepository.deleteById(user.getId());
        new UserResponseDTO(Constants.SUCCESS, Constants.USER_DELETED_SUCCESSFULLY, email);
    }

}
