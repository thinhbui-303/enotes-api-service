package com.thinhbqt.enotes_api_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thinhbqt.enotes_api_service.entity.FileDetails;

public interface FileDetailsRepository extends JpaRepository<FileDetails,Integer>{
    
}
