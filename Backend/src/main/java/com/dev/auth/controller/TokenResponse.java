package com.dev.auth.controller;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * @author Rupesh Dangi
 * @date: 2024/09/08 21/33
 */
@Getter
@Setter
@Builder
public class TokenResponse {
    private String username;
    private String jwt;
    private Set<String> roles;
}
