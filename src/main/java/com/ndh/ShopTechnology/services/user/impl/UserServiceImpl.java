package com.ndh.ShopTechnology.services.user.impl;

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
import com.ndh.ShopTechnology.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserRightServiceImpl userRightServiceImpl;

    private final PasswordEncoder passwordEncoder;
    private final UserRoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserRightServiceImpl userRightServiceImpl, PasswordEncoder passwordEncoder, UserRoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.userRightServiceImpl = userRightServiceImpl;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }


    @Override
    public ResultPagination getAllUsers(PaginationRequest request) {

        if (request.getPage() == null)
            request.setPage(0);
        if (request.getSize() == null)
            request.setSize(10);

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<UserEntity> userLst = userRepository.findAll(pageable);
        ResultPagination rs = null;

        List<UserResponse> userResponses = UserResponse.fromListEntity(userLst.getContent());
        if (userResponses.isEmpty())
            rs = new ResultPagination(userResponses, 0, 0, 0);
        else
            rs = new ResultPagination(userResponses, (int) userLst.getTotalElements(), request.getPage(), request.getSize());

        return rs;
    }


    @Override
    public UserEntity getUserEntity() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        String username = authentication.getName();
        Optional<UserEntity> userEntity = userRepository.findOneByUsername(username);
        if (userEntity.isEmpty())
            return null;
        UserEntity ent = userEntity.get();

        ent.doBuildAuths();
        ent.doBuildRoles();

        return ent;
    }

    @Override
    public UserResponse getUserInfo(Long id) {
        Optional<UserEntity> ent = userRepository.findById(id);
        UserResponse res = null;
        if (ent.isPresent()) {
            res = UserResponse.fromEntity(ent.get());

            ent.get().doBuildAuths();
            ent.get().doBuildRoles();
        }
        return res;
    }


    @Override
    public UserResponse getUserInfo() {
        return UserResponse.fromEntity(getUserEntity());
    }

    @Override
    public UserResponse modUserInfo(ModUserInfoRequest ent) {
        if (ent.getId() == null) return null;

        Optional<UserEntity> opt = userRepository.findById(ent.getId());
        if (opt.isEmpty()) return null;
        UserEntity user = opt.get();
        return modifyUserInfo(ent, user);
    }

    @Override
    public UserResponse modProfileInfo(ModUserInfoRequest ent) {
        UserEntity user = getUserEntity();
        if (user == null) return null;
        return modifyUserInfo(ent, user);
    }

    private UserResponse modifyUserInfo(ModUserInfoRequest ent, UserEntity user) {

        UserEntity updatedUser = toUserEnt(user, ent);

        if (ent.getRole() != null) {
            Optional<UserRoleEntity> role = roleRepository.findById(ent.getRole());
            role.ifPresent(updatedUser::setRole);
        }

        updatedUser.setId(user.getId());
        UserEntity userEntity = userRepository.save(updatedUser);

        if (ent.getRights() != null) {
            userRightServiceImpl.manageUserRights(DefAPIAuth.MODE_MOD, userEntity, ent.getRights(), userEntity.getManId());
        }

        userEntity.doBuildRoles();
        userEntity.doBuildAuths();

        return UserResponse.fromEntity(userEntity);
    }


    private UserEntity toUserEnt(UserEntity existingUser, ModUserInfoRequest ent) {
        return UserEntity.builder()
                .role(existingUser.getRole())
                .username(existingUser.getUsername())
                .firstName(ent.getFirstName()      != null ? ent.getFirstName()      : existingUser.getFirstName())
                .password(StringUtils.hasText(ent.getPassword())
                        ? passwordEncoder.encode(ent.getPassword())
                        : existingUser.getPassword())
                .lastName(ent.getLastName()        != null ? ent.getLastName()        : existingUser.getLastName())
                .email(ent.getEmail()              != null ? ent.getEmail()           : existingUser.getEmail())
                .phoneNumber(ent.getPhoneNumber()  != null ? ent.getPhoneNumber()     : existingUser.getPhoneNumber())
                .avatar(ent.getAvatar()            != null ? ent.getAvatar()          : existingUser.getAvatar())
                .status(ent.getStatus()            != null ? ent.getStatus()          : existingUser.getStatus())
                .type(ent.getType()                != null ? ent.getType()            : existingUser.getType())
                .info_01(ent.getInfo_01()          != null ? ent.getInfo_01()         : existingUser.getInfo_01())
                .info_02(ent.getInfo_02()          != null ? ent.getInfo_02()         : existingUser.getInfo_02())
                .info_03(ent.getInfo_03()          != null ? ent.getInfo_03()         : existingUser.getInfo_03())
                .info_04(ent.getInfo_04()          != null ? ent.getInfo_04()         : existingUser.getInfo_04())
                .manId(existingUser.getManId())
                .build();
    }



    @Override
    public UserResponse createUser(CreateUserRequest request) {

        UserEntity manUser = getUserEntity();
        Long manId = manUser.getId();
        if (request.getManId() != null)
            manId = request.getManId();

        String username = request.getUsername().toLowerCase().trim();
        String phoneNumber = request.getPhoneNumber().trim();

        if (userRepository.existsByUsername(username)) {
            return null;
        }

        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            return null;
        }


        UserEntity ent = UserEntity
                .builder()
                .username(username)
                .password(passwordEncoder.encode(request.getPassword().trim()))
                .firstName(request.getFirstName().trim())
                .lastName(request.getLastName().trim())
                .email(request.getEmail().trim())
                .phoneNumber(phoneNumber)
                .status(SystemConstant.ACTIVE_STATUS)
                .manId(manId)
                .build();


        Long roleId = request.getRole() != null ? request.getRole() : RoleConstant.ROLE_EMPLOYEE_ID;
        Optional<UserRoleEntity> roleOptional = roleRepository.findById(roleId);

        if (roleOptional.isPresent()) {
            ent.setRole(roleOptional.get());
        }
        ent = userRepository.save(ent);

        if (request.getRights() != null) {
            userRightServiceImpl.manageUserRights(DefAPIAuth.MODE_NEW, ent, request.getRights(), ent.getManId());
        }
        ent.doBuildAuths();
        ent.doBuildRoles();
        return UserResponse.fromEntity(ent);
    }

}
