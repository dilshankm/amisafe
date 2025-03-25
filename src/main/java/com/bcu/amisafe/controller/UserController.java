package com.bcu.amisafe.controller;

import com.bcu.amisafe.dto.UserRequestDTO;
import com.bcu.amisafe.dto.UserResponseDTO;
import com.bcu.amisafe.entity.User;
import com.bcu.amisafe.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
         this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO request) {
         UserResponseDTO response = userService.createUser(request);
         return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{email}")
    public ResponseEntity<User> getUser(@PathVariable String email) {
        User user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/{email}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable String email, @RequestBody UserRequestDTO request) {
        UserResponseDTO response = userService.updateUser(email, request);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<UserResponseDTO> deleteUser(@PathVariable String email) {
         userService.deleteUser(email);
         return ResponseEntity.noContent().build();
    }

}
