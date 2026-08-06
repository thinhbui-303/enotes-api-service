package com.thinhbqt.enotes_api_service.schedular;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.thinhbqt.enotes_api_service.entity.Note;
import com.thinhbqt.enotes_api_service.repository.NoteRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NoteSchedular {
    
    private final NoteRepository noteRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void deleteNotesFromRecycleBin(){
        LocalDateTime cutOffDay = LocalDateTime.now().minusDays(7);
        List<Note> oldNotes = noteRepository.findByIsDeletedTrueAndDeletedOnBefore(cutOffDay); 
        noteRepository.deleteAll(oldNotes);
    }
}
