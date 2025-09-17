package com.ndh.ShopTechnology.constant;

public class MessageConstant {

    // JWT / Authentication messages
    public static final String ERROR_GET_USERNAME      = "An error occurred during getting username from token";
    public static final String TOKEN_EXPIRED_LOG       = "The token is expired and not valid anymore";
    public static final String TOKEN_EXPIRED_RESPONSE  = "JWT expired";
    public static final String AUTH_FAILED             = "Authentication Failed. Username or Password not valid.";
    public static final String BEARER_MISSING          = "Couldn't find bearer string, will ignore the header";
    public static final String AUTH_SUCCESS_PREFIX     = "Authenticated user %s, setting security context";

    // User validation messages
    public static final String USERNAME_REQUIRED       = "Username is required";
    public static final String PHONE_REQUIRED          = "Phone number is required";
    public static final String PASSWORD_REQUIRED       = "Password is required";
    public static final String ROLE_NOT_FOUND          = "User role not found";
    public static final String USER_ALREADY_EXISTS     = "User already exists";
    public static final String VALIDATION_FAILED       = "Validation failed";

    // Success messages
    public static final String USER_REGISTER_SUCCESS   = "User registered successfully";
}
