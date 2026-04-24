package com.thinhbqt.enotes_api_service.service.impl;

import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.thinhbqt.enotes_api_service.dto.CategoryDto;
import com.thinhbqt.enotes_api_service.dto.CategoryResponse;
import com.thinhbqt.enotes_api_service.entity.Category;
import com.thinhbqt.enotes_api_service.repository.CategoryRepository;
import com.thinhbqt.enotes_api_service.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper mapper;
   

    @Override
    public Boolean saveCategory(CategoryDto categoryDto){
        Category category = mapper.map(categoryDto, Category.class);
        category.setIsDeleted(false);
        category.setCreatedOn(new Date());
        category.setCreatedBy(1);
        Category saveCategory = categoryRepository.save(category);
        if(!ObjectUtils.isEmpty(saveCategory)){
            return true;
        }
        return false;
    }

    @Override
    public List<CategoryDto> getAllCategory(){
        List<Category> categories = categoryRepository.findByIsDeletedFalse();
        return categories.stream()
        .map(cate -> mapper.map(cate,CategoryDto.class)).toList();
    }
    @Override
    public List<CategoryResponse> getAllIsActiveCategory(){
        List<Category> categories = categoryRepository.findByIsActiveTrueAndIsDeletedFalse();
        return categories.stream().map(cate -> mapper.map(cate, CategoryResponse.class)).toList();
    }
    @Override
    public CategoryDto getById(Integer id){
        Category category = categoryRepository.findByIdAndIsDeletedFalse(id);
        if(ObjectUtils.isEmpty(category)){
            return null;
        }
        else{
            return mapper.map(category,CategoryDto.class);
        }
    }
    @Override
    public Boolean deleteById(Integer id){
        Category category = categoryRepository.findById(id).get();

        if(ObjectUtils.isEmpty(category)){
            return false;
        }
        else{
            category.setIsDeleted(true);
            categoryRepository.save(category);
            return true;
        }
    }
}
