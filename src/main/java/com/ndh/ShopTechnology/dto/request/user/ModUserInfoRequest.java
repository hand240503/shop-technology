package com.ndh.ShopTechnology.dto.request.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
public class ModUserInfoRequest {

    private Long id;

    private String firstName;

    private String password;

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

    private Long role;
}
