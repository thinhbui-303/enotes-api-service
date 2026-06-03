package com.thinhbqt.enotes_api_service.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.thinhbqt.enotes_api_service.dto.NoteDto;
import com.thinhbqt.enotes_api_service.entity.FileDetails;
import com.thinhbqt.enotes_api_service.service.NoteService;
import com.thinhbqt.enotes_api_service.util.CommonUtil;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



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
    
    @GetMapping("/download/{id}")
    public ResponseEntity<?> downloadFile(@PathVariable Integer id)throws Exception {
        FileDetails fileDetails = noteService.getFileDetails(id);
        byte[] data = noteService.downloadFile(fileDetails);

        HttpHeaders header = new HttpHeaders();
        String setContentString = CommonUtil.setContentType(fileDetails.getOriginalFileName());
        MediaType mediaType = MediaType.parseMediaType(setContentString);
        header.setContentType(mediaType);
        header.setContentDispositionFormData("attachment", fileDetails.getOriginalFileName());

        return new ResponseEntity<>(data,header,HttpStatus.OK);
    }
    
    
}
