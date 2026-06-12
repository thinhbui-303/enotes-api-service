package com.thinhbqt.enotes_api_service.service.impl;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.thinhbqt.enotes_api_service.dto.CategoryDto;
import com.thinhbqt.enotes_api_service.dto.CategoryResponse;
import com.thinhbqt.enotes_api_service.entity.Category;
import com.thinhbqt.enotes_api_service.exception.ExistedDataException;
import com.thinhbqt.enotes_api_service.exception.ResourceNotFoundException;
import com.thinhbqt.enotes_api_service.repository.CategoryRepository;
import com.thinhbqt.enotes_api_service.service.CategoryService;
import com.thinhbqt.enotes_api_service.util.Validation;

@Service
public class CategoryServiceImpl implements CategoryService {
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private ModelMapper mapper;

    @Autowired
    private Validation validation;
    @Override
    public Boolean saveCategory(CategoryDto categoryDto) {
        validation.categoryValidation(categoryDto);

        Category category = mapper.map(categoryDto, Category.class);
        Boolean existedName = categoryRepository.existsByName(categoryDto.getName()); 
        
        if(existedName){
            throw new ExistedDataException("Category name has already existed");
        }
        if (ObjectUtils.isEmpty(category.getId())) {

            category.setIsDeleted(false);
            // category.setCreatedOn(new Date());
            // category.setCreatedBy(1);            
        }
        else{
            updateCategory(category);
        }
        categoryRepository.save(category);
        return true;
    }

    private void updateCategory(Category cate) {
        Optional<Category> find = categoryRepository.findById(cate.getId());
        if (find.isPresent()) {
            Category category = find.get();
            cate.setIsDeleted(category.getIsDeleted());
            cate.setCreatedBy(category.getCreatedBy());
            cate.setCreatedOn(category.getCreatedOn());
            // cate.setUpdatedBy(1);
            // cate.setUpdatedOn(new Date());
        }
        
    }
 
    @Override
    public List<CategoryDto> getAllCategory() {
        List<Category> categories = categoryRepository.findByIsDeletedFalse();
        return categories.stream()
                .map(cate -> mapper.map(cate, CategoryDto.class)).toList();
    }

    @Override
    public List<CategoryResponse> getAllIsActiveCategory() {
        List<Category> categories = categoryRepository.findByIsActiveTrueAndIsDeletedFalse();
        return categories.stream().map(cate -> mapper.map(cate, CategoryResponse.class)).toList();
    }

    @Override
    public CategoryDto getById(Integer id){
        Category category = categoryRepository.findByIdAndIsDeletedFalse(id)
        .orElseThrow( () -> new ResourceNotFoundException("Category not found with id: " + id));
        if (ObjectUtils.isEmpty(category)) {
            return null;
        } else {
            return mapper.map(category, CategoryDto.class);
        }
    }

    @Override
    public Boolean deleteById(Integer id) {
        Category category = categoryRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Category not found with id:"+ id));

        if (ObjectUtils.isEmpty(category)) {
            return false;
        } else {
            category.setIsDeleted(true);
            categoryRepository.save(category);
            return true;
        }
    }
}
