package com.dev.auth.jwt;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;

/**
 * @author Rupesh Dangi
 * @date: 2024/09/08 21/07
 */
public interface JWTService {
    String generateToken(UserDetails userDetails);
    String getUsernameFromToken(String token);
    boolean isTokenExpired(String token);
    boolean validateToken(String token);
    Set<GrantedAuthority> extractAuthoritiesFromToken(String token);
}
