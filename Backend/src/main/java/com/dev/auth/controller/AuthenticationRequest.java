package com.dev.auth.controller;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

/**
 * @author Rupesh Dangi
 * @date: 2024/09/08 21/33
 */
@Getter
@Setter
public class AuthenticationRequest {
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
}
