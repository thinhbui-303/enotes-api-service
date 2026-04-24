package com.thinhbqt.enotes_api_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thinhbqt.enotes_api_service.dto.CategoryDto;
import com.thinhbqt.enotes_api_service.dto.CategoryResponse;
import com.thinhbqt.enotes_api_service.entity.Category;
import com.thinhbqt.enotes_api_service.service.CategoryService;
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

    @PostMapping("/save-category")
    public ResponseEntity<?> saveCategory(@RequestBody CategoryDto category) {
        Boolean isSaved = categoryService.saveCategory(category);
        if(isSaved){
            return new ResponseEntity<>("susscess save", HttpStatus.CREATED);
        }
        else{
            return new ResponseEntity<>("not saved", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/categories")
    public ResponseEntity<?> getAllCategory() {
        List<CategoryDto> categories = categoryService.getAllCategory();
        if(CollectionUtils.isEmpty(categories)){
            return ResponseEntity.noContent().build();
        }
        else{
            return new ResponseEntity<>(categories, HttpStatus.OK);
        }
    }
    @GetMapping("/active-categories")
    public ResponseEntity<?> getIsActiveCategory() {
        List<CategoryResponse> categories = categoryService.getAllIsActiveCategory();
        if(CollectionUtils.isEmpty(categories)){
            return ResponseEntity.noContent().build();
        }
        else{
            return new ResponseEntity<>(categories, HttpStatus.OK);
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Integer id) {
        CategoryDto categoryDto = categoryService.getById(id);
        if(ObjectUtils.isEmpty(categoryDto)){
            return new ResponseEntity<>("not found category", HttpStatus.NOT_FOUND);
        }
        else{
            return new ResponseEntity<>(categoryDto, HttpStatus.OK);
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Integer id){
        Boolean deletedCategory = categoryService.deleteById(id);
        if(deletedCategory){
            return new ResponseEntity<>("deleted successfully", HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("not found category", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    
    
    
}
