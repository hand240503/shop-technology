package com.ndh.ShopTechnology.entities.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "role")
public class UserRoleEntity {
    public static final String COL_ID               = "id";
    public static final String COL_NAME             = "name";
    public static final String COL_CODE             = "code";
    public static final String COL_STATUS           = "status";
    public static final String COL_USER_RIGHTS      = "user_right";
    public static final String COL_INFO_01          = "info_01";
    public static final String COL_INFO_02          = "info_02";
    public static final String COL_CREATED_DATE     = "created_date";
    public static final String COL_MODIFIED_DATE    = "modified_date";
    public static final String COL_CREATED_BY       = "created_by";
    public static final String COL_MODIFIED_BY      = "modified_by";

    @Id
    @Column(name = COL_ID, unique = true, nullable = false)
    private Long id;

    @Column(name = COL_NAME, nullable = true)
    private String name;

    @Column(name = COL_CODE, nullable = true)
    private String code;

    @Column(name = COL_STATUS, nullable = true)
    private Integer status;

    @Column(name = COL_USER_RIGHTS, nullable = true)
    private String userRights;

    @Column(name = COL_INFO_01, nullable = true)
    private String info_01;

    @Column(name = COL_INFO_02, nullable = true)
    private String info_02;

    @Column(name = COL_CREATED_DATE)
    @CreatedDate
    private Date createdDate;

    @Column(name = COL_MODIFIED_DATE)
    @LastModifiedDate
    private Date modifiedDate;

    @Column(name = COL_CREATED_BY)
    @CreatedBy
    private String createdBy;

    @Column(name = COL_MODIFIED_BY)
    @LastModifiedBy
    private String modifiedBy;

}
