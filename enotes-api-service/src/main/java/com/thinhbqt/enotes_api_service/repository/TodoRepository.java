package com.thinhbqt.enotes_api_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thinhbqt.enotes_api_service.entity.Todo;

public interface TodoRepository extends JpaRepository<Todo, Integer> {
    List<Todo> findByCreatedBy(Integer uid);
}
