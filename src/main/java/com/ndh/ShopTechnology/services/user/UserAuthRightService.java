package com.ndh.ShopTechnology.services.user;

import com.ndh.ShopTechnology.entities.user.UserAuthRightEntity;
import com.ndh.ShopTechnology.entities.user.UserEntity;

import java.util.Set;

public interface UserAuthRightService {

    UserAuthRightEntity manageUserRights(int mode,UserEntity userEntity, Set<Integer> rightsToAssign, Long manId);

}
