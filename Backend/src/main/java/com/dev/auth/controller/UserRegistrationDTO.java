package com.dev.auth.controller;

import com.dev.auth.Role;
import lombok.Data;

/**
 * @author Rupesh Dangi
 * @date: 2024/09/08 22/29
 */
@Data
public class UserRegistrationDTO {
    private String username;
    private String password;
    private Role role;
}
