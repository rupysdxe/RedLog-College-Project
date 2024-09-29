package com.dev.auth.config;

import com.dev.auth.exceptions.UnAuthorizedException;
import com.dev.auth.jwt.JWTService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Rupesh Dangi
 * @date: 2024/09/08 21/06
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenAuthenticationStrategy implements AuthenticationFilterStrategy
{
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private final JWTService jwtService;
    private final List<String> supportedEndpoints = Collections.singletonList("/api");
    @Override
    public boolean supports(HttpServletRequest request) {
        return supportedEndpoints.stream().anyMatch(request.getRequestURI()::startsWith);
    }
    @Override
    public void authenticate(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);
        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_PREFIX)) {
            String jwt = authorizationHeader.substring(BEARER_PREFIX.length());
            String username = jwtService.getUsernameFromToken(jwt);
            Collection<GrantedAuthority> authorities = jwtService.extractAuthoritiesFromToken(jwt);
            if (username != null && !username.isEmpty() && SecurityContextHolder.getContext().getAuthentication() == null) {
                if (jwtService.validateToken(jwt)) {
                    UsernamePasswordAuthenticationToken authentication = new
                            UsernamePasswordAuthenticationToken(username, null, authorities);
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.info("Token is valid and username: {} is set in Security Context.", username);
                    filterChain.doFilter(request, response);
                } else {
                    log.error("Token is not valid.");
                    throw new UnAuthorizedException("Token not valid.");
                }
            }
        } else {
            log.info("Authorization Header not found.");
            filterChain.doFilter(request, response);
        }
    }

}

