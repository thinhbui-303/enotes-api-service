package com.thinhbqt.enotes_api_service.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thinhbqt.enotes_api_service.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer>{
    List<Category> findByIsActiveTrueAndIsDeletedFalse();

    List<Category> findByIsDeletedFalse();

    Optional<Category> findByIdAndIsDeletedFalse(Integer id);
}
