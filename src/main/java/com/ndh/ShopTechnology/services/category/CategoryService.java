package com.ndh.ShopTechnology.services.category;

import com.ndh.ShopTechnology.dto.request.category.CreateCategoryRequest;
import com.ndh.ShopTechnology.entities.product.CategoryEntity;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CategoryService {

    CategoryEntity createCategory(CreateCategoryRequest req);
    List<CategoryEntity> getAllCategories();
}
