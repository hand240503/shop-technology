package com.ndh.ShopTechnology.controller.auth;

import com.ndh.ShopTechnology.constant.MessageConstant;
import com.ndh.ShopTechnology.def.DefRes;
import com.ndh.ShopTechnology.dto.request.auth.LoginRequest;
import com.ndh.ShopTechnology.dto.request.auth.RegisterUserRequest;
import com.ndh.ShopTechnology.dto.response.APIResponse;
import com.ndh.ShopTechnology.dto.response.user.LoginResponse;
import com.ndh.ShopTechnology.dto.response.user.UserResponse;
import com.ndh.ShopTechnology.services.user.UserAuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/auth")
public class UserAuthController {

    private final UserAuthService userAuthService;

    @Autowired
    public UserAuthController(UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Valid RegisterUserRequest request) {
        UserResponse ent = userAuthService.registerUser(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(APIResponse.doResponse(
                        DefRes.RES_DES, MessageConstant.USER_REGISTER_SUCCESS,
                        DefRes.STAT_CODE, DefRes.STATUS_CREATED,
                        DefRes.RES_DATA, ent
                ));
    }



    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request) throws Exception {
        LoginResponse res = userAuthService.login(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(APIResponse.doResponse(
                        DefRes.RES_DES, MessageConstant.LOGIN_SUCCESS,
                        DefRes.STAT_CODE, DefRes.STATUS_SUCCESS,
                        DefRes.RES_DATA, res.getUserInfo(),
                        DefRes.USER_TOK, res.getToken()
                ));
    }

}
