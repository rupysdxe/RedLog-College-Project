package com.dev.auth.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.dev.utils.Constants.TOKEN_ENDPOINT;

/**
 * @author Rupesh Dangi
 * @date: 2024/09/08 21/12
 */
@Component
@Slf4j
public class TokenEndpointStrategy implements AuthenticationFilterStrategy {
    @Override
    public boolean supports(HttpServletRequest request) {
        return request.getRequestURI().equals(TOKEN_ENDPOINT);
    }
    @Override
    public void authenticate(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        filterChain.doFilter(request, response);
    }
}
