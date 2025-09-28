package com.ndh.ShopTechnology.dto.response.user;

import com.ndh.ShopTechnology.entities.user.UserEntity;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {

    private Long id;

    private String username;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private String avatar;

    private Integer status;

    private Integer type;

    private String info_01;

    private String info_02;

    private String info_03;

    private String info_04;

    private Set<Integer> rights;

    private Set<Long> roles;

    private String roleName;


    public static UserResponse fromEntity(UserEntity user) {
        if (user == null) return null;


        if (user.getId() == null) return null;

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .avatar(user.getAvatar())
                .status(user.getStatus())
                .type(user.getType())
                .info_01(user.getInfo_01())
                .info_02(user.getInfo_02())
                .info_03(user.getInfo_03())
                .info_04(user.getInfo_04())
                .rights(user.getO_Rights() != null ? user.getO_Rights() : new HashSet<>())
                .roles(user.getO_Roles() != null ? user.getO_Roles() : new HashSet<>())
                .roleName(user.getRole() != null ? user.getRole().getName() : null)
                .build();
    }

    public static List<UserResponse> fromListEntity(List<UserEntity> users) {
        if (users == null || users.isEmpty()) return null;

        List<UserResponse> userResponses = new ArrayList<>();
        for (UserEntity user : users) {
            UserResponse response = fromEntity(user);
            if (response != null) {
                userResponses.add(response);
            }
        }
        return userResponses;
    }


}
