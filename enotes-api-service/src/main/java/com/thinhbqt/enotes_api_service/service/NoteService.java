package com.thinhbqt.enotes_api_service.service;

import java.util.List;

import com.thinhbqt.enotes_api_service.dto.NoteDto;

public interface NoteService {
    public Boolean saveNote(NoteDto noteDto);

    public List<NoteDto> getAllNote();

    
}
