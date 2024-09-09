package com.dev.auth.exceptions;

/**
 * @author Rupesh Dangi
 * @date: 2024/09/08 21/08
 */
public class UnAuthorizedException extends RuntimeException{

    public UnAuthorizedException(String message)
    {
        super(message);
    }
}

