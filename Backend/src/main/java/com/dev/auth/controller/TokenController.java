package com.dev.auth.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Rupesh Dangi
 * @date: 2024/09/08 21/32
 */
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/oauth/token")
public class TokenController {
//    private final JWTService jwtService;
//    private final UserService userService;
    @PostMapping
    public ResponseEntity<?> createAuthenticationToken(@RequestBody @Validated AuthenticationRequest authenticationRequest){
//        log.info("Authentication user: {}", authenticationRequest.getUsername());
//        UserDetails userDetails = userService.authenticateUser(authenticationRequest);
//        TokenResponse jwtToken = TokenResponse.builder()
//                .username(userDetails.getUsername())
//                .roles(userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()))
//                .jwt(jwtService.generateToken(userDetails))
//                .build();
        /*
        THIS JUST CHECKS FOR TEST USER
         */
        if(authenticationRequest.getUsername().equals("test") && authenticationRequest.getPassword().equals("test")){
            return ResponseEntity.ok().build();
        }else{
            return ResponseEntity.badRequest().build();
        }
    }

}
