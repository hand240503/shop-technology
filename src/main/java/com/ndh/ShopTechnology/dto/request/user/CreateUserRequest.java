package com.ndh.ShopTechnology.dto.request.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateUserRequest {

    @NotBlank(message = "Username cannot be blank")
    private String username;

    @NotBlank(message = "Password cannot be blank")
    private String password;

    @NotBlank(message = "First name cannot be blank")
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    private String lastName;

    @NotBlank(message = "Email cannot be blank")
    private String email;

    @NotBlank(message = "Phone number cannot be blank")
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

    private Long manId;
}
