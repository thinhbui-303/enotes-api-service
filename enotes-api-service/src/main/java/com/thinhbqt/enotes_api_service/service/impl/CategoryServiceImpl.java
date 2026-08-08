package com.thinhbqt.enotes_api_service.service.impl;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final ModelMapper mapper;

    private final Validation validation;

    @Override
    public Boolean saveCategory(CategoryDto categoryDto) {

        validation.categoryValidation(categoryDto);

        Category category = mapper.map(categoryDto, Category.class);
        Boolean existedName = categoryRepository.existsByName(categoryDto.getName());

        if (existedName) {
            throw new ExistedDataException("Category name has already existed");
        }
        if (ObjectUtils.isEmpty(category.getId())) {

            category.setIsDeleted(false);
            log.info("Execution success: add new Category");

        } else {
            updateCategory(category);
            log.info("Execution success: update Category done");
        }
        categoryRepository.save(category);

        // log.info("Execution end: saveCategory method!");

        return true;
    }

    private void updateCategory(Category cate) {
        Optional<Category> find = categoryRepository.findById(cate.getId());
        if (find.isPresent()) {
            Category category = find.get();
            cate.setIsDeleted(category.getIsDeleted());
            cate.setCreatedBy(category.getCreatedBy());
            cate.setCreatedOn(category.getCreatedOn());

        }

    }

    @Override
    @Cacheable(value = "allCategories")
    public List<CategoryDto> getAllCategory() {
        // log.info("CategoryServiceImpl: Execution Start: getAllCategory method!");
        List<Category> categories = categoryRepository.findByIsDeletedFalse();
        // log.info("Execution End: getAllCategory method!");
        return categories.stream()
                .map(cate -> mapper.map(cate, CategoryDto.class)).toList();
    }

    @Override
    @Cacheable(value = "allActiveCategories")
    public List<CategoryResponse> getAllIsActiveCategory() {
        // log.info("CategoryServiceImpl: Execution Start: getAllIsActiveCategory
        // method!");

        List<Category> categories = categoryRepository.findByIsActiveTrueAndIsDeletedFalse();
        // log.info("Execution End: getAllIsActiveCategory method!");
        return categories.stream().map(cate -> mapper.map(cate, CategoryResponse.class)).toList();
    }

    @Override
    @Cacheable(value = "categoryById", key = "#id")
    public CategoryDto getById(Integer id) {
        // log.info("CategoryServiceImpl: Execution Start: getById method in
        // category!");

        Category category = categoryRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        if (ObjectUtils.isEmpty(category)) {
            return null;
        } else {
            // log.info("Execution end: getById method in category!");
            return mapper.map(category, CategoryDto.class);
        }
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "getCategoryById", key = "#id"),
            @CacheEvict(value = { "allCategory", "activeCategory" }, allEntries = true) 
    })
    public Boolean deleteById(Integer id) {
        // log.info("CategoryServiceImpl: Execution Start: deleteById method in
        // category!");
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id:" + id));

        if (ObjectUtils.isEmpty(category)) {
            return false;
        } else {
            category.setIsDeleted(true);
            categoryRepository.save(category);
            // log.info("Execution end: deleteById method in category!");
            return true;
        }
    }
}
