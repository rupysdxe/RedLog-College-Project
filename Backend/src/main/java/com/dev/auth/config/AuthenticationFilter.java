package com.dev.auth.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author Rupesh Dangi
 * @date: 2024/09/08 22/21
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class AuthenticationFilter extends OncePerRequestFilter {
    private final List<AuthenticationFilterStrategy> authenticationStrategies;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException
    {

        log.info("Request received for path {} and method {}", request.getRequestURI(), request.getMethod());
        for (AuthenticationFilterStrategy strategy : authenticationStrategies)
        {
            if (strategy.supports(request)) {
                log.info("Strategy {} supports the request", strategy.getClass().getName());
                strategy.authenticate(request, response, filterChain);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

}
