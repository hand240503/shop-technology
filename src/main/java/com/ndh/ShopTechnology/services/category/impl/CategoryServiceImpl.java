package com.ndh.ShopTechnology.services.category.impl;

import com.ndh.ShopTechnology.constant.MessageConstant;
import com.ndh.ShopTechnology.def.DefAPIAuth;
import com.ndh.ShopTechnology.dto.request.category.CreateCategoryRequest;
import com.ndh.ShopTechnology.entities.product.CategoryEntity;
import com.ndh.ShopTechnology.entities.user.UserEntity;
import com.ndh.ShopTechnology.repository.CategoryRepository;
import com.ndh.ShopTechnology.services.category.CategoryService;
import com.ndh.ShopTechnology.services.common.APIAuthService;
import com.ndh.ShopTechnology.services.user.UserService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository    categoryRepository;
    private final UserService           userService;

    public CategoryServiceImpl(CategoryRepository categoryRepository, UserService userService) {
        this.categoryRepository     =   categoryRepository;
        this.userService            =   userService;
    }

    @Override
    public CategoryEntity createCategory(CreateCategoryRequest req) {
        UserEntity currentUser = userService.getCurrentUser();
        boolean hasPermission = APIAuthService.checkUserPermissions(
                currentUser,
                DefAPIAuth.WORK_NEW,
                DefAPIAuth.R_AUT_USER_NEW
        );
        if(!hasPermission){
            throw new AccessDeniedException(MessageConstant.NO_PERMISSION_ACTION);
        }
        CategoryEntity ent = CategoryEntity.builder()
                    .code(req.getCode())
                    .name(req.getName())
                    .status(req.getStatus())
                    .build();
        return categoryRepository.save(ent);
    }

    @Override
    public List<CategoryEntity> getAllCategories() {
        return categoryRepository.findAll();
    }
}
