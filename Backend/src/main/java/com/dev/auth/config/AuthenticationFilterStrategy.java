package com.dev.auth.config;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Rupesh Dangi
 * @date: 2024/09/08 21/05
 */
public interface AuthenticationFilterStrategy
{
    boolean supports(HttpServletRequest request);
    void authenticate(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException;
}
