package com.ndh.ShopTechnology.repository;

import com.ndh.ShopTechnology.entities.product.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
}
