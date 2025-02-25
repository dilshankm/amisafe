package com.bcu.amisafe.service;

import com.bcu.amisafe.dto.UserRequestDTO;
import com.bcu.amisafe.dto.UserResponseDTO;
import com.bcu.amisafe.entity.User;

import java.util.Optional;

public interface UserService {
    UserResponseDTO createUser(UserRequestDTO request);
    Optional<User> getUserByEmail(String email);
    UserResponseDTO updateUser(String email, UserRequestDTO request);
}
