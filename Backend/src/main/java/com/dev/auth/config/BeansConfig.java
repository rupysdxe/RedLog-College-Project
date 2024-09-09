package com.dev.auth.config;

import com.dev.auth.User;
import com.dev.auth.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

/**
 * @author Rupesh Dangi
 * @date: 2024/09/08 20/38
 */
@Configuration
@RequiredArgsConstructor
public class BeansConfig {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Bean
    public UserDetailsService userDetailsService()
    {
        return username -> {
            if(username==null || username.isEmpty())throw new UsernameNotFoundException("Username not valid");
            Optional<User> user = userRepository.findUserByUsername(username);
            if(user.isPresent()){
                return user.get().getUserDetails();
            }
            throw new UsernameNotFoundException("User not found.");
        };

    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(){
        return authentication -> authenticationProvider().authenticate(authentication);
    }
}
