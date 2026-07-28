package com.thinhbqt.enotes_api_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thinhbqt.enotes_api_service.dto.CategoryDto;
import com.thinhbqt.enotes_api_service.dto.CategoryResponse;
import com.thinhbqt.enotes_api_service.service.CategoryService;
import com.thinhbqt.enotes_api_service.util.CommonUtil;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;




@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/save-category")
    public ResponseEntity<?> saveCategory(@Valid @RequestBody CategoryDto category) {
        Boolean isSaved = categoryService.saveCategory(category);
        if(isSaved){
            // return new ResponseEntity<>("susscess save", HttpStatus.CREATED);
            return CommonUtil.createBuildResponseMessage( HttpStatus.CREATED, "success save");
        }
        else{
            // return new ResponseEntity<>("not saved", HttpStatus.INTERNAL_SERVER_ERROR);
            return CommonUtil.createErrorResponseMessage("failed to save category", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/categories")
    public ResponseEntity<?> getAllCategory() {
        List<CategoryDto> categories = categoryService.getAllCategory();
        if(CollectionUtils.isEmpty(categories)){
            return ResponseEntity.noContent().build();
        }
        else{
            // return new ResponseEntity<>(categories, HttpStatus.OK);
            return CommonUtil.createBuildResponse(categories, HttpStatus.OK);
        }
    }
    @GetMapping("/active-categories")
    public ResponseEntity<?> getIsActiveCategory() {
        List<CategoryResponse> categories = categoryService.getAllIsActiveCategory();
        if(CollectionUtils.isEmpty(categories)){
            return ResponseEntity.noContent().build();
        }
        else{
            // return new ResponseEntity<>(categories, HttpStatus.OK);
            return CommonUtil.createBuildResponse(categories, HttpStatus.OK);

        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Integer id){
        // try {
        //     CategoryDto categoryDto = categoryService.getById(id);
        // if(ObjectUtils.isEmpty(categoryDto)){
        //     return new ResponseEntity<>("not found category", HttpStatus.NOT_FOUND);
        // }
        // else{
        //     return new ResponseEntity<>(categoryDto, HttpStatus.OK);
        // }
        // }
        // catch (ResourceNotFoundException e) {
        //     return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        // }
        // catch (Exception e) {
        //     return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        // }

        CategoryDto categoryDto = categoryService.getById(id);
        if(ObjectUtils.isEmpty(categoryDto)){
            return CommonUtil.createErrorResponseMessage("category not found ", HttpStatus.NOT_FOUND);
        }
        else
            return CommonUtil.createBuildResponse(categoryDto, HttpStatus.OK);
        
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Integer id) throws Exception{
        Boolean deletedCategory = categoryService.deleteById(id);
        if(deletedCategory){
            return CommonUtil.createBuildResponse(deletedCategory, HttpStatus.OK);
        }
        else{
            // return new ResponseEntity<>("not found category", HttpStatus.INTERNAL_SERVER_ERROR);
            return CommonUtil.createErrorResponseMessage("not found category", HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }
    
    
    
    
}
