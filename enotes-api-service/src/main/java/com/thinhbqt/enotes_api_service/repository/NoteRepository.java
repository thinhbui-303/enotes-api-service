package com.thinhbqt.enotes_api_service.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.thinhbqt.enotes_api_service.entity.Note;

public interface NoteRepository extends JpaRepository<Note, Integer>{
    Page<Note> findByCreatedBy(Integer uid , Pageable pageable);

    Page<Note> findByCreatedByAndIsDeletedTrue(Integer uid, Pageable pageable);

    Page<Note> findByCreatedByAndIsDeletedFalse(Integer uid, Pageable pageable);

    List<Note> findByIsDeletedTrueAndDeletedOnBefore(LocalDateTime cutOffDay);

    @Query("select n from Note n where (Lower(n.title) like lower(concat('%',:keyword,'%')) "
			+ "or lower(n.description) like lower(concat('%',:keyword,'%')) "
			+ "or lower(n.category.name) like lower(concat('%',:keyword,'%'))) "
			+ "and n.isDeleted=false "
			+ "and n.createdBy=:userId")
	Page<Note> searchNotes(@Param("keyword") String keyword,@Param("userId")Integer userId,Pageable pageable);
	
}
