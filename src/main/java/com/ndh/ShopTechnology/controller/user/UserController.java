package com.ndh.ShopTechnology.controller.user;

import com.ndh.ShopTechnology.def.DefRes;
import com.ndh.ShopTechnology.dto.request.user.ModUserInfoRequest;
import com.ndh.ShopTechnology.dto.response.APIResponse;
import com.ndh.ShopTechnology.dto.response.user.UserResponse;
import com.ndh.ShopTechnology.services.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/users")
public class UserController {

    private final UserService userService;



    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;

    }

    @Operation(summary = "Get user profile information", description = "Retrieve the profile information of the user")
    @GetMapping("/profile")
    public ResponseEntity<APIResponse> getUserInfo() {
        UserResponse ent = userService.getProfile();
        if (ent == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(APIResponse.doResponse(
                            DefRes.STAT_CODE, DefRes.STATUS_NOT_FOUND,
                            DefRes.RES_DES, "User information not found"
                    ));
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(APIResponse.doResponse(
                        DefRes.STAT_CODE, DefRes.STATUS_SUCCESS,
                        DefRes.RES_DES, "User information retrieved successfully",
                        DefRes.RES_DATA, userService.getProfile()
                ));
    }

    @Operation(summary = "Update user profile", description = "Modify the profile information of the current user.")
    @PutMapping("/profile")
    public ResponseEntity<APIResponse> modProfileInfo(@RequestBody ModUserInfoRequest request) {
        UserResponse updatedUser = userService.modProfileInfo(request);

        if (updatedUser == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(APIResponse.doResponse(
                            DefRes.STAT_CODE, DefRes.STATUS_BAD_REQUEST,
                            DefRes.RES_DES, "Failed to update user profile"
                    ));
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(APIResponse.doResponse(
                        DefRes.STAT_CODE, DefRes.STATUS_SUCCESS,
                        DefRes.RES_DES, "User profile updated successfully",
                        DefRes.RES_DATA, updatedUser
                ));
    }


}