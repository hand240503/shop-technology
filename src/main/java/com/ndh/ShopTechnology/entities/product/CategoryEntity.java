package com.ndh.ShopTechnology.entities.product;

import com.ndh.ShopTechnology.entities.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "category")
public class CategoryEntity extends BaseEntity {

    public static final String COL_CODE            = "code";
    public static final String COL_NAME            = "name";
    public static final String COL_STATUS          = "status";

    @Column(name = COL_CODE, nullable = true)
    private String code;

    @Column(name = COL_NAME, nullable = true)
    private String name;

    @Column(name = COL_STATUS, nullable = true)
    private Integer status;
}
