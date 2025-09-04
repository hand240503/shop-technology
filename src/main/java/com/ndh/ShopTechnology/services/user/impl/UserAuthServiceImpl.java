package com.ndh.ShopTechnology.services.user.impl;

import com.ndh.ShopTechnology.config.TokenProvider;
import com.ndh.ShopTechnology.constant.RoleConstant;
import com.ndh.ShopTechnology.constant.SystemConstant;
import com.ndh.ShopTechnology.dto.request.auth.LoginRequest;
import com.ndh.ShopTechnology.dto.request.auth.RegisterUserRequest;
import com.ndh.ShopTechnology.dto.response.user.LoginResponse;
import com.ndh.ShopTechnology.dto.response.user.UserResponse;
import com.ndh.ShopTechnology.entities.user.UserEntity;
import com.ndh.ShopTechnology.entities.user.UserRoleEntity;
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

        String username = request.getUsername().toLowerCase().trim();
        String phone = request.getPhoneNumber().trim();

        if (userRepository.existsByUsername(username) || userRepository.existsByPhoneNumber(phone))
            return null;


        String roleCode = Optional.ofNullable(request.getRoleCode())
                .orElse(RoleConstant.ROLE_CUSTOMER)
                .trim();

        UserRoleEntity role = roleRepository.getUserRoleEntityByCode(roleCode);

        UserEntity user = UserEntity.builder()
                .username(username)
                .password(passwordEncoder.encode(request.getPassword().trim()))
                .firstName(request.getFirstName().trim())
                .lastName(request.getLastName().trim())
                .email(request.getEmail().trim())
                .phoneNumber(phone)
                .status(SystemConstant.ACTIVE_STATUS)
                .build();

        if (role != null)
            user.setRole(role);


        return UserResponse.fromEntity(userRepository.save(user));
    }

    @Override
    public LoginResponse login(LoginRequest request) throws Exception {
        String login = Optional.ofNullable(request.getLogin())
                .map(String::trim)
                .orElse("");
        if (login.isEmpty()) return null;

        Optional<UserEntity> userOpt = isPhoneNumber(login)
                ? userRepository.findOneByPhoneNumber(login)
                : userRepository.findOneByUsername(login);

        UserEntity user = userOpt.orElse(null);
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return null;
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
