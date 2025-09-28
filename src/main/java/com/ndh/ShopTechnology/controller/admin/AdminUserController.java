package com.ndh.ShopTechnology.controller.admin;

import com.ndh.ShopTechnology.def.DefRes;
import com.ndh.ShopTechnology.dto.request.user.CreateUserRequest;
import com.ndh.ShopTechnology.dto.request.user.ModUserInfoRequest;
import com.ndh.ShopTechnology.dto.response.APIResponse;
import com.ndh.ShopTechnology.dto.response.user.UserResponse;
import com.ndh.ShopTechnology.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/admin/users")
public class AdminUserController {
    private final UserService userService;


    @Autowired
    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

//    @GetMapping("")
//    public ResponseEntity<APIResponse> getAllUser(
//            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
//            @RequestParam(value = "size", defaultValue = "10", required = false) int size) {
//
//        ResultPagination resultPagination = userService.getAllUsers(page, size);
//
//        if (resultPagination.getLst().isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(APIResponse.doResponse(
//                            DefRes.STAT_CODE, DefRes.STATUS_NOT_FOUND,
//                            DefRes.RES_DATA, "No users found."
//                    ));
//        }
//
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(APIResponse.doResponse(
//                        DefRes.STAT_CODE, DefRes.STATUS_SUCCESS,
//                        DefRes.RES_DATA, resultPagination
//                ));
//    }

//    @PostMapping("")
//    public ResponseEntity<APIResponse> getAllUser(@RequestBody PaginationRequest request) {
//
//        ResultPagination resultPagination = userService.getAllUsers(request);
//
//        if (resultPagination.getLst().isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(APIResponse.doResponse(
//                            DefRes.STAT_CODE, DefRes.STATUS_NOT_FOUND,
//                            DefRes.RES_DATA, "No users found."
//                    ));
//        }
//
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(APIResponse.doResponse(
//                        DefRes.STAT_CODE, DefRes.STATUS_SUCCESS,
//                        DefRes.RES_DATA, resultPagination
//                ));
//    }

    @PostMapping("")
    public ResponseEntity<APIResponse> createUser(@RequestBody CreateUserRequest request){

        UserResponse userResponse = userService.createUser(request);

        return ResponseEntity.status(HttpStatus.OK)
                .body(APIResponse.doResponse(
                        DefRes.STAT_CODE, DefRes.STATUS_SUCCESS,
                        DefRes.RES_DATA, userResponse
                ));
    }


    @GetMapping("/{id}")
    public ResponseEntity<APIResponse> getUserInfo(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(APIResponse.doResponse(
                        DefRes.STAT_CODE, DefRes.STATUS_SUCCESS,
                        DefRes.RES_DATA, userService.getUserInfo(id)
                ));
    }

    @PutMapping("")
    public ResponseEntity<APIResponse> modUerInfo(@RequestBody ModUserInfoRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(APIResponse.doResponse(
                        DefRes.STAT_CODE, DefRes.STATUS_SUCCESS,
                        DefRes.RES_DATA, userService.updateUserInfo(request)
                ));
    }
}
