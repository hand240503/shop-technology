package com.ndh.ShopTechnology.entities.user;

import com.ndh.ShopTechnology.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "user_auth")
public class UserAuthRightEntity extends BaseEntity {

    public static final String COL_STATUS          = "status";
    public static final String COL_AUTH_RIGHTS     = "aut_right";
    public static final String COL_USER_ID         = "user_id";
    public static final String COL_USER_ROLE_ID    = "user_role_id";
    public static final String COL_TYPE            = "type";

    @Column(name = COL_STATUS, nullable = true)
    private Integer status;

    @Column(name = COL_AUTH_RIGHTS, nullable = true)
    private String authRights;
    @Column(name = COL_TYPE, nullable = true)
    private Integer type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = COL_USER_ID)
    private UserEntity user;



}
