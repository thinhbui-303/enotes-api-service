package com.thinhbqt.enotes_api_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thinhbqt.enotes_api_service.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    
}
