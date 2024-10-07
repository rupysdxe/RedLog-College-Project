package com.dev.auth.controller;

import lombok.Data;

/**
 * @author Rupesh Dangi
 * @date: 2024/10/06 22/26
 */
@Data
public class UpdatePasswordDto {
    String oldPassword;
    String newPassword;
}
