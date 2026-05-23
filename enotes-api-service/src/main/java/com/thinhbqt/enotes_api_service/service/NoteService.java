package com.thinhbqt.enotes_api_service.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.thinhbqt.enotes_api_service.dto.NoteDto;

public interface NoteService {
    public Boolean saveNote(String notes , MultipartFile file)throws Exception;

    public List<NoteDto> getAllNote();

    
}
