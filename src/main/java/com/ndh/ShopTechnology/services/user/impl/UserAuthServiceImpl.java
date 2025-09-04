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

    private final UserRepository userRepository;
    private final UserRoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;
    private final TokenProvider jwtTokenUtil;

    public UserAuthServiceImpl(UserRepository userRepository, UserRoleRepository roleRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, TokenProvider jwtTokenUtil) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
    }



    @Override
    public UserResponse registerUser(RegisterUserRequest request) {
        if (userRepository.existsByUsername(request.getUsername().toLowerCase().trim()))
            return null;

        if (userRepository.existsByPhoneNumber(request.getPhoneNumber().trim()))
            return null;

        UserRoleEntity role = null;
        if (request.getRoleCode() == null)
            request.setRoleCode(RoleConstant.ROLE_CUSTOMER);

        role = roleRepository.getUserRoleEntityByCode(request.getRoleCode().trim());
        UserEntity user = UserEntity
                .builder()
                .username(request.getUsername().toLowerCase().trim())
                .password(passwordEncoder.encode(request.getPassword().trim()))
                .firstName(request.getFirstName().trim())
                .lastName(request.getLastName().trim())
                .email(request.getEmail().trim())
                .phoneNumber(request.getPhoneNumber().trim())
                .status(SystemConstant.ACTIVE_STATUS)
                .build();
        if (role != null) user.setRole(role);

        return UserResponse.fromEntity(userRepository.save(user));
    }

    @Override
    public LoginResponse login(LoginRequest request) throws Exception {
        String uName = request.getLogin();
        if (uName == null || uName.trim().isEmpty()) return null;

        Optional<UserEntity> ent;
        LoginResponse loginResponse = new LoginResponse();
        String token = null;

        if (isPhoneNumber(uName))
            ent = userRepository.findOneByPhoneNumber(uName);
        else
            ent = userRepository.findOneByUsername(uName);

        if (ent.isEmpty()) return null;

        if (passwordEncoder.matches(request.getPassword(), ent.get().getPassword())) {

            final Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getLogin(), request.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            token = jwtTokenUtil.generateToken(authentication);

            ent.get().doBuildAuths();
            ent.get().doBuildRoles();

            UserResponse userResponse = UserResponse.fromEntity(ent.get());

            loginResponse.setUserInfo(userResponse);
            loginResponse.setToken(token);

            return loginResponse;
        }

        return null;
    }


    private boolean isPhoneNumber(String login) {
        return login.matches("\\d{10,15}");
    }

}
