package com.thinhbqt.enotes_api_service.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thinhbqt.enotes_api_service.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer>{
    List<Category> findByIsActiveTrueAndIsDeletedFalse();

    List<Category> findByIsDeletedFalse();

    Category findByIdAndIsDeletedFalse(Integer id);
}
