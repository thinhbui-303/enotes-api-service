package com.thinhbqt.enotes_api_service.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.thinhbqt.enotes_api_service.dto.FavoriteNoteDto;
import com.thinhbqt.enotes_api_service.dto.NoteDto;
import com.thinhbqt.enotes_api_service.dto.NoteResponse;
import com.thinhbqt.enotes_api_service.entity.FileDetails;
import com.thinhbqt.enotes_api_service.service.NoteService;
import com.thinhbqt.enotes_api_service.util.CommonUtil;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("api/v1/note")
public class NoteController {
    @Autowired
    NoteService noteService;

    @PreAuthorize("hasAnyRole('USER')")
    @PostMapping("/save-note")
    public ResponseEntity<?> saveNote(@RequestParam String notes, @RequestParam(defaultValue = "") MultipartFile file)
            throws Exception {
        Boolean saved = noteService.saveNote(notes, file);
        if (saved) {
            return CommonUtil.createBuildResponseMessage(HttpStatus.OK, "Save successful");
        } else {
            return CommonUtil.createErrorResponseMessage("Something wrong!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/notes")
    public ResponseEntity<?> getAllNote() {
        List<NoteDto> notes = noteService.getAllNote();
        if (notes.isEmpty()) {
            ResponseEntity.noContent().build();
        }
        return CommonUtil.createBuildResponse(notes, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/download/{id}")
    public ResponseEntity<?> downloadFile(@PathVariable Integer id) throws Exception {
        FileDetails fileDetails = noteService.getFileDetails(id);
        byte[] data = noteService.downloadFile(fileDetails);

        HttpHeaders header = new HttpHeaders();
        String setContentString = CommonUtil.setContentType(fileDetails.getOriginalFileName());
        MediaType mediaType = MediaType.parseMediaType(setContentString);
        header.setContentType(mediaType);
        header.setContentDispositionFormData("attachment", fileDetails.getOriginalFileName());

        return CommonUtil.createBuildResponse(data, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/user-notes")
    public ResponseEntity<?> getAllNotePaginationByUser(@RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        NoteResponse noteResponse = noteService.getAllNotePagination(pageNo, pageSize);
        return CommonUtil.createBuildResponse(noteResponse, HttpStatus.OK);
    }
    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/deleteNote/{id}")
    public ResponseEntity<?> softDeleteNote(@PathVariable Integer id) {
        noteService.softDeleteNote(id);

        return CommonUtil.createBuildResponseMessage(HttpStatus.OK, "Delete success!");
    }
    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/restoreNote/{id}")
    public ResponseEntity<?> restoreNote(@PathVariable Integer id) {
        noteService.restoreNote(id);

        return CommonUtil.createBuildResponseMessage(HttpStatus.OK, "Restored success!");
    }
    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/recycleBin")
    public ResponseEntity<?> getNoteFromRecycleBin(@RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        NoteResponse noteResponse = noteService.getNoteFromBinPagination(pageNo, pageSize);
        return CommonUtil.createBuildResponse(noteResponse, HttpStatus.OK);
    }
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @DeleteMapping("/deletePermanentNote/{id}")
    public ResponseEntity<?> hardDeleteNote(@PathVariable Integer id) {
        noteService.hardDeleteNote(id);

        return CommonUtil.createBuildResponseMessage(HttpStatus.OK, "Delete permanent success!");
    }

    @GetMapping("/saveFavoriteNote/{id}")
    public ResponseEntity<?> saveFavoriteNote(@PathVariable Integer noteId) {
        noteService.saveFavoriteNote(noteId);

        return CommonUtil.createBuildResponseMessage(HttpStatus.CREATED, "save fav note success!");
    }

    @DeleteMapping("/deleteFavoriteNote/{id}")
    public ResponseEntity<?> deleteFavoriteNote(@PathVariable Integer favNoteId) {
        noteService.deleteFavoriteNote(favNoteId);

        return CommonUtil.createBuildResponseMessage(HttpStatus.OK, "Delete faveNote success!");
    }

    @GetMapping("/getFavoriteNote")
    public ResponseEntity<?> getFavoriteNote() {
        List<FavoriteNoteDto> favoriteNoteDtos = noteService.getFavoriteNote();
        if (ObjectUtils.isEmpty(favoriteNoteDtos)) {
            return ResponseEntity.noContent().build();
        }
        return CommonUtil.createBuildResponse(favoriteNoteDtos, HttpStatus.OK);
    }

    @GetMapping("/copyNote/{id}")
    public ResponseEntity<?> copyNote(@PathVariable Integer id) {
        Boolean copied = noteService.copyNote(id);
        if (copied) {
            return CommonUtil.createBuildResponseMessage(HttpStatus.CREATED, "Copied");
        }
        return CommonUtil.createErrorResponseMessage("fail to copy!", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
