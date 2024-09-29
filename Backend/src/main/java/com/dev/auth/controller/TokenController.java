package com.dev.auth.controller;

import com.dev.auth.UserService;
import com.dev.auth.jwt.JWTService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

/**
 * @author Rupesh Dangi
 * @date: 2024/09/08 21/32
 */
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/oauth/token")
public class TokenController {
      private final JWTService jwtService;
      private final UserService userService;
    @PostMapping
    public ResponseEntity<?> createAuthenticationToken(@RequestBody @Validated AuthenticationRequest authenticationRequest){
        log.info("Authentication user: {}", authenticationRequest.getUsername());
        UserDetails userDetails = userService.authenticateUser(authenticationRequest);
        TokenResponse jwtToken = TokenResponse.builder()
                .username(userDetails.getUsername())
                .roles(userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()))
                .jwt(jwtService.generateToken(userDetails))
                .build();
        return ResponseEntity.ok(jwtToken);
    }


}
