package com.ndh.ShopTechnology.services.user.impl;

import com.ndh.ShopTechnology.constant.MessageConstant;
import com.ndh.ShopTechnology.constant.RoleConstant;
import com.ndh.ShopTechnology.constant.SystemConstant;
import com.ndh.ShopTechnology.def.DefAPIAuth;
import com.ndh.ShopTechnology.dto.request.PaginationRequest;
import com.ndh.ShopTechnology.dto.request.user.CreateUserRequest;
import com.ndh.ShopTechnology.dto.request.user.ModUserInfoRequest;
import com.ndh.ShopTechnology.dto.response.ResultPagination;
import com.ndh.ShopTechnology.dto.response.user.UserResponse;
import com.ndh.ShopTechnology.entities.user.UserEntity;
import com.ndh.ShopTechnology.entities.user.UserRoleEntity;
import com.ndh.ShopTechnology.repository.UserRepository;
import com.ndh.ShopTechnology.repository.UserRoleRepository;
import com.ndh.ShopTechnology.services.common.APIAuthService;
import com.ndh.ShopTechnology.services.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserRightServiceImpl userRightServiceImpl;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleRepository roleRepository;

    /* ================== Helpers ================== */

    private UserEntity getCurrentUserEntity() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            return null;
        }

        return userRepository.findOneByUsername(auth.getName())
                .map(user -> {
                    user.doBuildAuths();
                    user.doBuildRoles();
                    return user;
                })
                .orElse(null);
    }


    private UserEntity loadUser(Long id) {
        return (id == null) ? getCurrentUserEntity() : userRepository.findById(id).orElse(null);
    }

    private UserResponse toResponse(UserEntity user) {
        if (user == null) return null;
        user.doBuildAuths();
        user.doBuildRoles();
        return UserResponse.fromEntity(user);
    }

    private UserEntity applyModFields(UserEntity existingUser, ModUserInfoRequest req) {
        if (req == null || existingUser == null) return existingUser;

        if (req.getFirstName()      != null) existingUser.setFirstName(req.getFirstName());
        if (req.getLastName()       != null) existingUser.setLastName(req.getLastName());
        if (req.getEmail()          != null) existingUser.setEmail(req.getEmail());
        if (req.getPhoneNumber()    != null) existingUser.setPhoneNumber(req.getPhoneNumber());
        if (req.getAvatar()         != null) existingUser.setAvatar(req.getAvatar());
        if (req.getStatus()         != null) existingUser.setStatus(req.getStatus());
        if (req.getType()           != null) existingUser.setType(req.getType());
        if (req.getInfo_01()        != null) existingUser.setInfo_01(req.getInfo_01());
        if (req.getInfo_02()        != null) existingUser.setInfo_02(req.getInfo_02());
        if (req.getInfo_03()        != null) existingUser.setInfo_03(req.getInfo_03());
        if (req.getInfo_04()        != null) existingUser.setInfo_04(req.getInfo_04());

        if (StringUtils.hasText(req.getPassword())) {
            existingUser.setPassword(passwordEncoder.encode(req.getPassword()));
        }

        return existingUser;
    }

    /* ================== Services ================== */

    @Override
    public ResultPagination getAllUsers(PaginationRequest request) {
        int page = (request.getPage() == null) ? 0  : request.getPage();
        int size = (request.getSize() == null) ? 10 : request.getSize();

        Pageable            pageable    =   PageRequest.of(page, size);
        Page<UserEntity>    userPage    =   userRepository.findAll(pageable);

        List<UserResponse>  responses   =   UserResponse.fromListEntity(userPage.getContent());
        return responses.isEmpty()
                ? new ResultPagination(responses, 0, 0, 0)
                : new ResultPagination(responses, (int) userPage.getTotalElements(), page, size);
    }

    @Override
    public UserEntity getCurrentUser() {
        return getCurrentUserEntity();
    }

    @Override
    public UserResponse getUserInfo(Long id) {
        UserEntity ent = getCurrentUser();

        boolean hasPermission = APIAuthService.checkUserPermissions(
                ent,
                DefAPIAuth.WORK_GET,
                DefAPIAuth.R_AUT_USER_GET
        );

        if (!hasPermission) {
            throw new AccessDeniedException(MessageConstant.NO_PERMISSION_ACTION);
        }
        return toResponse(loadUser(id));
    }

    @Override
    public UserResponse getProfile() {
        return getUserInfo(null);
    }

    @Override
    public UserResponse modUserInfo(ModUserInfoRequest req) {
        if (req == null || req.getId() == null) {
            return null;
        }

        UserEntity currentUser = getCurrentUser();

        boolean hasPermission = APIAuthService.checkUserPermissions(
                currentUser,
                DefAPIAuth.WORK_MOD,
                DefAPIAuth.R_AUT_USER_MOD
        );

        if (!hasPermission) {
            throw new AccessDeniedException(MessageConstant.NO_PERMISSION_ACTION);
        }

        UserEntity target = loadUser(req.getId());
        if (target == null) return null;

        return modifyUserInfo(req, target);
    }



    @Override
    public UserResponse modProfileInfo(ModUserInfoRequest req) {
        UserEntity current = getCurrentUserEntity();
        if (current == null) return null;
        return modifyUserInfo(req, current);
    }

    private UserResponse modifyUserInfo(ModUserInfoRequest req, UserEntity user) {
        if (user == null) return null;

        UserEntity toSave = applyModFields(user, req);

        if (req.getRole() != null) {
            roleRepository.findById(req.getRole()).ifPresent(toSave::setRole);
        }

        UserEntity saved = userRepository.save(toSave);

        if (req.getRights() != null) {
            userRightServiceImpl.manageUserRights(DefAPIAuth.MODE_MOD, saved, req.getRights(), saved.getManId());
        }

        return toResponse(saved);
    }


    @Override
    public UserResponse createUser(CreateUserRequest request) {
        if (request == null) return null;

        UserEntity  current     =   getCurrentUserEntity();
        Long        manId       =   (request.getManId()         != null)    ?   request.getManId()                          : (current != null ? current.getId() : null);
        String      username    =   (request.getUsername()      != null)    ?   request.getUsername().toLowerCase().trim()  : null;
        String      phone       =   (request.getPhoneNumber()   != null)    ?   request.getPhoneNumber().trim()             : null;

        if (userRepository.existsByUsername(username) || userRepository.existsByPhoneNumber(phone)) {
            return null;
        }

        UserEntity ent = UserEntity.builder()
                .username(username)
                .password(passwordEncoder.encode(request.getPassword().trim()))
                .firstName(request.getFirstName()       != null ? request.getFirstName().trim() : null)
                .lastName(request.getLastName()         != null ? request.getLastName().trim()  : null)
                .email(request.getEmail()               != null ? request.getEmail().trim()     : null)
                .phoneNumber(phone)
                .status(SystemConstant.ACTIVE_STATUS)
                .manId(manId)
                .build();

        Long roleId = (request.getRole() != null) ? request.getRole() : RoleConstant.ROLE_EMPLOYEE_ID;
        roleRepository.findById(roleId).ifPresent(ent::setRole);

        UserEntity saved = userRepository.save(ent);

        if (request.getRights() != null) {
            userRightServiceImpl.manageUserRights(DefAPIAuth.MODE_NEW, saved, request.getRights(), saved.getManId());
        }

        return toResponse(saved);
    }
}
