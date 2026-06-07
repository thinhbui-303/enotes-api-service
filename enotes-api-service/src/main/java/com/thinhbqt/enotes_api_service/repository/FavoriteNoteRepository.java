package com.thinhbqt.enotes_api_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thinhbqt.enotes_api_service.entity.FavoriteNote;
import java.util.List;


public interface FavoriteNoteRepository extends JpaRepository<FavoriteNote, Integer>{
    List<FavoriteNote> findByUid(Integer uid);

}
