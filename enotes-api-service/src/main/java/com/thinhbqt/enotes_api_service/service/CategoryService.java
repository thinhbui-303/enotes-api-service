package com.thinhbqt.enotes_api_service.service;

import java.util.List;

import com.thinhbqt.enotes_api_service.dto.CategoryDto;
import com.thinhbqt.enotes_api_service.dto.CategoryResponse;


public interface CategoryService {

    public Boolean saveCategory(CategoryDto categoryDto);

    public List<CategoryDto> getAllCategory();

    public List<CategoryResponse> getAllIsActiveCategory();
}