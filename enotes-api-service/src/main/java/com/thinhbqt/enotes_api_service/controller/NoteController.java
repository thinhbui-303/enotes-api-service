package com.thinhbqt.enotes_api_service.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.thinhbqt.enotes_api_service.dto.NoteDto;
import com.thinhbqt.enotes_api_service.service.NoteService;
import com.thinhbqt.enotes_api_service.util.CommonUtil;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("api/v1/note")
public class NoteController {
    @Autowired
    NoteService noteService;

    @PostMapping("/save-note")
    public ResponseEntity<?> saveNote(@RequestParam String notes, @RequestParam(defaultValue = "") MultipartFile file ) throws Exception{
        Boolean saved = noteService.saveNote(notes, file);
        if(saved){
            return CommonUtil.createBuildResponseMessage( HttpStatus.OK, "Save successful");
        }
        else{
            return CommonUtil.createErrorResponseMessage("Something wrong!",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/notes")
    public ResponseEntity<?> getAllNote() {
        List<NoteDto> notes =  noteService.getAllNote();
        if(notes.isEmpty()){
            ResponseEntity.noContent().build();
        }
        return CommonUtil.createBuildResponse(notes, HttpStatus.OK);
    }
    
}
