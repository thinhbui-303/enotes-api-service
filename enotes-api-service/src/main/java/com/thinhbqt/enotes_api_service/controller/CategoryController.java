package com.thinhbqt.enotes_api_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RestController;

import com.thinhbqt.enotes_api_service.dto.CategoryDto;
import com.thinhbqt.enotes_api_service.dto.CategoryResponse;
import com.thinhbqt.enotes_api_service.endpoint.CategoryEndpoint;
import com.thinhbqt.enotes_api_service.service.CategoryService;
import com.thinhbqt.enotes_api_service.util.CommonUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CategoryController implements CategoryEndpoint {

    private final CategoryService categoryService;

    @Override
    public ResponseEntity<?> saveCategory(CategoryDto category) {
        boolean isSaved = categoryService.saveCategory(category);
        if (isSaved) {
            return CommonUtil.createBuildResponseMessage(HttpStatus.CREATED, "success save");
        } else {
            return CommonUtil.createErrorResponseMessage("failed to save category", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> getAllCategory() {
        List<CategoryDto> categories = categoryService.getAllCategory();
        if (CollectionUtils.isEmpty(categories)) {
            return ResponseEntity.noContent().build();
        } else {
            return CommonUtil.createBuildResponse(categories, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<?> getIsActiveCategory() {
        List<CategoryResponse> categories = categoryService.getAllIsActiveCategory();
        if (CollectionUtils.isEmpty(categories)) {
            return ResponseEntity.noContent().build();
        } else {
            return CommonUtil.createBuildResponse(categories, HttpStatus.OK);

        }
    }

    @Override
    public ResponseEntity<?> getCategoryById(Integer id) {

        CategoryDto categoryDto = categoryService.getById(id);
        if (ObjectUtils.isEmpty(categoryDto)) {
            return CommonUtil.createErrorResponseMessage("category not found ", HttpStatus.NOT_FOUND);
        } else
            return CommonUtil.createBuildResponse(categoryDto, HttpStatus.OK);

    }

    @Override
    public ResponseEntity<?> deleteCategory(Integer id) throws Exception {
        boolean deletedCategory = categoryService.deleteById(id);
        if (deletedCategory) {
            return CommonUtil.createBuildResponse(deletedCategory, HttpStatus.OK);
        } else {
            return CommonUtil.createErrorResponseMessage("not found category", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
