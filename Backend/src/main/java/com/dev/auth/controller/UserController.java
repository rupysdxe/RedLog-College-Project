package com.dev.auth.controller;

import com.dev.auth.User;
import com.dev.auth.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
   // private final UserService userService;
    List<UserRegistrationDTO> users = new ArrayList<>();
    @PostMapping
    public ResponseEntity<?> postUser(@RequestBody UserRegistrationDTO request){
//        int userId = userService.addNewUser(request.getUsername(),request.getPassword(),request.getRole());
//        Map<String,String> response = new HashMap<>();
//        User createdUser = userService.getUserById(userId);
//        response.put("username",createdUser.getUsername());
       // return ResponseEntity.ok(response);
        users.add(request);
        return ResponseEntity.ok(request);
    }
    @GetMapping
    public ResponseEntity<?> getUsers(){
      //  return ResponseEntity.ok(userService.getUsers());
        return ResponseEntity.ok(users);
    }
}
