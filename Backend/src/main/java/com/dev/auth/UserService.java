package com.dev.auth;

import com.dev.auth.controller.AuthenticationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Rupesh Dangi
 * @date: 2024/09/08 20/35
 */
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public UserDetails authenticateUser(AuthenticationRequest authenticationRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        return (UserDetails) authentication.getPrincipal();
    }

    public User getUserById(int id){
        return userRepository.findById(id).get();
    }

    public List<User> getUsers(){
        return userRepository.findAll();
    }
    public int addNewUser(String username, String password,Role role) {
        if (userRepository.findUserByUsername(username).isPresent()) {
            throw new RuntimeException("User already exist");
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        return userRepository.save(user).getId();
    }
  }
