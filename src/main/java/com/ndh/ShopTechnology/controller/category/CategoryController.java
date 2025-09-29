package com.ndh.ShopTechnology.controller.category;

import com.ndh.ShopTechnology.constant.MessageConstant;
import com.ndh.ShopTechnology.def.DefRes;
import com.ndh.ShopTechnology.dto.request.category.CreateCategoryRequest;
import com.ndh.ShopTechnology.dto.response.APIResponse;
import com.ndh.ShopTechnology.entities.product.CategoryEntity;
import com.ndh.ShopTechnology.services.category.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody CreateCategoryRequest req) {
        CategoryEntity ent = categoryService.createCategory(req);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(APIResponse.doResponse(
                        DefRes.RES_DES, MessageConstant.CATEGORY_CREATE_SUCCESS,
                        DefRes.STAT_CODE, DefRes.STATUS_CREATED,
                        DefRes.RES_DATA, ent
                ));
    }

    @GetMapping
    public ResponseEntity<?> getAllCategories() {
        List<CategoryEntity> categories = categoryService.getAllCategories();

        return ResponseEntity
                .ok(APIResponse.doResponse(
                        DefRes.RES_DES, MessageConstant.CATEGORY_LIST_SUCCESS,
                        DefRes.STAT_CODE, DefRes.STATUS_SUCCESS,
                        DefRes.RES_DATA, categories
                ));
    }
}
