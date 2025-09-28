package com.ndh.ShopTechnology.services.user.impl;

import com.ndh.ShopTechnology.config.TokenProvider;
import com.ndh.ShopTechnology.constant.MessageConstant;
import com.ndh.ShopTechnology.constant.RoleConstant;
import com.ndh.ShopTechnology.constant.SystemConstant;
import com.ndh.ShopTechnology.dto.request.auth.LoginRequest;
import com.ndh.ShopTechnology.dto.request.auth.RegisterUserRequest;
import com.ndh.ShopTechnology.dto.response.user.LoginResponse;
import com.ndh.ShopTechnology.dto.response.user.UserResponse;
import com.ndh.ShopTechnology.entities.user.UserEntity;
import com.ndh.ShopTechnology.entities.user.UserRoleEntity;
import com.ndh.ShopTechnology.exception.AuthenticationFailedException;
import com.ndh.ShopTechnology.exception.NotFoundEntityException;
import com.ndh.ShopTechnology.repository.UserRepository;
import com.ndh.ShopTechnology.repository.UserRoleRepository;
import com.ndh.ShopTechnology.services.user.UserAuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserAuthServiceImpl implements UserAuthService {

    private final UserRepository        userRepository;
    private final UserRoleRepository    roleRepository;

    private final PasswordEncoder       passwordEncoder;

    private final AuthenticationManager authenticationManager;
    private final TokenProvider         jwtTokenUtil;

    public UserAuthServiceImpl(UserRepository userRepository, UserRoleRepository roleRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, TokenProvider jwtTokenUtil) {
        this.userRepository         = userRepository;
        this.roleRepository         = roleRepository;
        this.passwordEncoder        = passwordEncoder;
        this.authenticationManager  = authenticationManager;
        this.jwtTokenUtil           = jwtTokenUtil;
    }



    @Override
    public UserResponse registerUser(RegisterUserRequest request) {

        String username = Optional.ofNullable(request.getUsername())
                .map(String::trim)
                .map(String::toLowerCase)
                .orElseThrow(() -> new IllegalArgumentException(MessageConstant.USERNAME_REQUIRED));

        String phone = Optional.ofNullable(request.getPhoneNumber())
                .map(String::trim)
                .orElseThrow(() -> new IllegalArgumentException(MessageConstant.PHONE_REQUIRED));

        if (userRepository.existsByUsername(username) || userRepository.existsByPhoneNumber(phone)) {
            throw new AuthenticationFailedException(MessageConstant.USER_ALREADY_EXISTS);
        }

        String roleCode = Optional.ofNullable(request.getRoleCode())
                .map(String::trim)
                .orElse(RoleConstant.ROLE_CUSTOMER);

        /*Check password(Nếu cần)*/
        String password = Optional.ofNullable(request.getPassword())
                .map(String::trim)
                .orElseThrow(() -> new IllegalArgumentException(MessageConstant.PASSWORD_REQUIRED));


        UserRoleEntity role = roleRepository.getUserRoleEntityByCode(roleCode);
        if (role == null) {
            throw new NotFoundEntityException(MessageConstant.ROLE_NOT_FOUND);
        }

        UserEntity user = UserEntity.builder()
                .username(username)
                .password(passwordEncoder.encode(request.getPassword().trim()))
                .firstName(Optional.ofNullable(request.getFirstName()).map(String::trim).orElse(null))
                .lastName(Optional.ofNullable(request.getLastName()).map(String::trim).orElse(null))
                .email(Optional.ofNullable(request.getEmail()).map(String::trim).orElse(null))
                .phoneNumber(phone)
                .status(SystemConstant.ACTIVE_STATUS)
                .role(role)
                .build();

        return UserResponse.fromEntity(userRepository.save(user));
    }


    @Override
    public LoginResponse login(LoginRequest request) {
        String login = Optional.ofNullable(request.getLogin())
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .orElseThrow(() -> new AuthenticationFailedException(MessageConstant.AUTH_FAILED));

        UserEntity user = (isPhoneNumber(login)
                ? userRepository.findOneByPhoneNumber(login)
                : userRepository.findOneByUsername(login))
                .orElseThrow(() -> new AuthenticationFailedException(MessageConstant.AUTH_FAILED));


        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AuthenticationFailedException(MessageConstant.AUTH_FAILED);
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(login, request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenUtil.generateToken(authentication);

        user.doBuildAuths();
        user.doBuildRoles();

        return LoginResponse.builder()
                .userInfo(UserResponse.fromEntity(user))
                .token(token)
                .build();
    }



    private boolean isPhoneNumber(String login) {
        return login.matches("\\d{10,15}");
    }

}
