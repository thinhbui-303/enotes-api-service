package com.thinhbqt.enotes_api_service.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.thinhbqt.enotes_api_service.dto.NoteDto;
import com.thinhbqt.enotes_api_service.dto.NoteDto.CategoryDto;
import com.thinhbqt.enotes_api_service.entity.Note;
import com.thinhbqt.enotes_api_service.exception.ResourceNotFoundException;
import com.thinhbqt.enotes_api_service.repository.CategoryRepository;
import com.thinhbqt.enotes_api_service.repository.NoteRepository;
import com.thinhbqt.enotes_api_service.service.NoteService;

@Service
public class NoteServiceImpl implements NoteService{
    @Autowired
    private NoteRepository noteRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper mapper;

    private void checkExistCategory(CategoryDto categoryDto){
        categoryRepository.findById(categoryDto.getId())
        .orElseThrow(() -> new ResourceNotFoundException("can not found category"));
    }
    @Override
    public Boolean saveNote(NoteDto noteDto){
        checkExistCategory(noteDto.getCategoryDto());

        Note note = mapper.map(noteDto, Note.class);

        if(!ObjectUtils.isEmpty(note)){
            
            noteRepository.save(note);
            return true;
        }
        else return false;

    }

    @Override
    public List<NoteDto> getAllNote(){
        return noteRepository.findAll()
        .stream()
        .map(note -> mapper.map(note, NoteDto.class))
        .toList();
    }
    
}
