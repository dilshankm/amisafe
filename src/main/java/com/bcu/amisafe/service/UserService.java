package com.bcu.amisafe.service;

import com.bcu.amisafe.dto.UserRequestDTO;
import com.bcu.amisafe.dto.UserResponseDTO;
import com.bcu.amisafe.entity.User;

public interface UserService {
    UserResponseDTO createUser(UserRequestDTO request);
    User getUserByEmail(String email);
    UserResponseDTO updateUser(String email, UserRequestDTO request);
    void deleteUser(String email);
}
