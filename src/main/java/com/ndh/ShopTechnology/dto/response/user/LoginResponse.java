package com.ndh.ShopTechnology.dto.response.user;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private UserResponse userInfo;
    private String token;
}
