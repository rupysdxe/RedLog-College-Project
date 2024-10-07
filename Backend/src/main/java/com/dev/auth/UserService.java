package com.dev.auth;

import com.dev.auth.controller.AuthenticationRequest;
import com.dev.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

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

    public User getUserById(int id){
        Optional<User> optionalUser=  userRepository.findById(id);
        return optionalUser.orElse(null);
    }

    @Transactional
    public void updatePassword(@NotNull String username,@NotNull String oldPassword,@NotNull String newPassword){
        if(StringUtils.isNotEmpty(username)){
            Optional<User> optionalUser = userRepository.findUserByUsername(username);
            if(!optionalUser.isPresent()){
                throw new UsernameNotFoundException("User with username "+username+" does not exist.");
            }
            User user= optionalUser.get();
            if(!passwordEncoder.matches(oldPassword,user.getPassword())){
                throw new RuntimeException("Old password does not match");
            }
            user.setPassword(passwordEncoder.encode(newPassword));
        }
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
    public void deleteUser(int id){
       userRepository.deleteById(id);
    }

    public UserDetails authenticateUser(AuthenticationRequest authenticationRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        return (UserDetails) authentication.getPrincipal();
    }

  }
