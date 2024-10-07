package com.dev.auth.controller;

import com.dev.auth.User;
import com.dev.auth.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Rupesh Dangi
 * @date: 2024/09/08 22/28
 */
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    @PostMapping
    public ResponseEntity<?> postUser(@RequestBody UserRegistrationDTO request){
        int userId = userService.addNewUser(request.getUsername(),request.getPassword(),request.getRole());
        Map<String,String> response = new HashMap<>();
        User createdUser = userService.getUserById(userId);
        response.put("username",createdUser.getUsername());
        return ResponseEntity.ok(response);
    }
    @GetMapping
    public ResponseEntity<List<User>> getUsers(){
        return ResponseEntity.ok(userService.getUsers());
    }
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable @Min(0) int id){
        return ResponseEntity.ok(userService.getUserById(id));
    }
    @PutMapping
    public ResponseEntity<Void> updatePassword(@RequestBody UpdatePasswordDto updatePasswordDto){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        userService.updatePassword(username,updatePasswordDto.oldPassword,updatePasswordDto.newPassword);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable @Min(0) int id){
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}
