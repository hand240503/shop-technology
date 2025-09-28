package com.ndh.ShopTechnology.services.user;

import com.ndh.ShopTechnology.dto.request.auth.LoginRequest;
import com.ndh.ShopTechnology.dto.request.auth.RegisterUserRequest;
import com.ndh.ShopTechnology.dto.response.user.LoginResponse;
import com.ndh.ShopTechnology.dto.response.user.UserResponse;
import com.ndh.ShopTechnology.entities.user.UserEntity;

public interface UserAuthService {


    UserResponse registerUser(RegisterUserRequest request) ;

    LoginResponse login(LoginRequest request) throws Exception;

}
