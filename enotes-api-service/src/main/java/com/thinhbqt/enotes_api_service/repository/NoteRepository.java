package com.thinhbqt.enotes_api_service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thinhbqt.enotes_api_service.entity.Note;

@Repository
public interface NoteRepository extends JpaRepository<Note, Integer>{
    Page<Note> findByCreatedBy(Integer uid , Pageable pageable);

    Page<Note> findByCreatedByAndIsDeletedTrue(Integer uid, Pageable pageable);

    Page<Note> findByCreatedByAndIsDeletedFalse(Integer uid, Pageable pageable);

}
