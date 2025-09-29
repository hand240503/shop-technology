package com.ndh.ShopTechnology.entities.user;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ndh.ShopTechnology.entities.BaseEntity;
import com.ndh.ShopTechnology.services.common.CommonService;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "users")
public class UserEntity extends BaseEntity {

    public static final String COL_USERNAME         =   "user_name";
    public static final String COL_PASSWORD         =   "password";
    public static final String COL_FIRST_NAME       =   "first_name";
    public static final String COL_LAST_NAME        =   "last_name";
    public static final String COL_EMAIL            =   "email";
    public static final String COL_PHONE_NUMBER     =   "telephone";
    public static final String COL_AVATAR           =   "avatar";
    public static final String COL_STATUS           =   "status";
    public static final String COL_TYPE             =   "type";
    public static final String COL_INFO_01          =   "info_01";
    public static final String COL_INFO_02          =   "info_02";
    public static final String COL_INFO_03          =   "info_03";
    public static final String COL_INFO_04          =   "info_04";
    public static final String COL_USER_ROLE_ID     =   "user_role_id";
    public static final String COL_MAN_ID           =   "man_id";

    @NotNull(message = "Username không được để trống")
    @Column(name = COL_USERNAME, nullable = false, unique = true)
    private String username;

    @NotNull(message = "Password không được để trống")
    @Column(name = COL_PASSWORD, nullable = false)
    private String password;

    @Column(name = COL_FIRST_NAME, nullable = true)
    private String firstName;

    @Column(name = COL_LAST_NAME, nullable = true)
    private String lastName;

    @Column(name = COL_EMAIL, nullable = true)
    private String email;

    @Column(name = COL_PHONE_NUMBER, nullable = true, unique = true)
    private String phoneNumber;

    @Column(name = COL_AVATAR, nullable = true)
    private String avatar;

    @Column(name = COL_STATUS, nullable = true)
    private Integer status;

    @Column(name = COL_TYPE, nullable = true)
    private Integer type;

    @Column(name = COL_INFO_01, nullable = true)
    private String info_01;

    @Column(name = COL_INFO_02, nullable = true)
    private String info_02;

    @Column(name = COL_INFO_03, nullable = true)
    private String info_03;

    @Column(name = COL_INFO_04, nullable = true)
    private String info_04;

    @Column(name = COL_MAN_ID, nullable = true)
    private Long manId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = COL_USER_ROLE_ID)
    @JsonIgnore
    private UserRoleEntity role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<UserAuthRightEntity> authRights = new HashSet<>();

    @Transient
    private HashSet<Integer> O_Rights ;

    @Transient
    private HashSet<Long> O_Roles ;


    public void doBuildRoles(){
        if (this.O_Roles != null) return;
        this.O_Roles = new HashSet<>();
        if (this.role != null) this.O_Roles.add(role.getId());

    }

    public void doBuildAuths(){
        if (this.O_Rights != null) return;

        this.O_Rights = new HashSet<>();
        if (this.authRights != null) {
            for (UserAuthRightEntity authRightEntity : authRights) {
                String rs = authRightEntity.getAuthRights();
                Set<Integer> hashSet = CommonService.convertToHashSet(rs);
                this.O_Rights.addAll(hashSet);
            }
        }
        if (this.role != null && this.role.getUserRights() != null) {
            String uRights = this.role.getUserRights().trim();
            if (uRights.isEmpty()) return;
            ObjectMapper objectMapper = new ObjectMapper();
            Set<Integer> roleRights = null;
            try {
                roleRights = new HashSet<>(Arrays.asList(objectMapper.readValue(uRights, Integer[].class)));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            this.O_Rights.addAll(roleRights);
        }
    }


    public boolean haveAnyRights(Integer... rights) {
        if (rights != null) {
            for (Integer r : rights)
                if (this.O_Rights.contains(r)) return true;
        }
        return false;
    }

    public boolean haveAnyRights(Set<Integer> rights) {
        if (rights != null) {
            for (Integer r : rights)
                if (this.O_Rights.contains(r)) return true;
        }
        return false;
    }

    public boolean haveRights(Integer... rights) {
        if (rights == null || rights.length == 0) return false;

        for (Integer r : rights) {
            if (!O_Rights.contains(r)) {
                return false;
            }
        }
        return true;
    }

    public boolean haveRights(Set<Integer> rights) {
        if (rights == null || rights.isEmpty()) return false;

        for (Integer r : rights) {
            if (!O_Rights.contains(r)) {
                return false;
            }
        }
        return true;
    }




}
