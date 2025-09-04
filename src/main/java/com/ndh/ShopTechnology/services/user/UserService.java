package com.ndh.ShopTechnology.services.user;

import com.ndh.ShopTechnology.dto.request.PaginationRequest;
import com.ndh.ShopTechnology.dto.request.user.CreateUserRequest;
import com.ndh.ShopTechnology.dto.response.ResultPagination;
import com.ndh.ShopTechnology.dto.request.user.ModUserInfoRequest;
import com.ndh.ShopTechnology.dto.response.user.UserResponse;
import com.ndh.ShopTechnology.entities.user.UserEntity;


public interface UserService {
    ResultPagination getAllUsers(PaginationRequest request);

    UserEntity getUserEntity();

    UserResponse getUserInfo(Long id);
    UserResponse getUserInfo();

    UserResponse modUserInfo(ModUserInfoRequest ent);

    UserResponse modProfileInfo(ModUserInfoRequest ent);

    UserResponse createUser(CreateUserRequest request);
}
