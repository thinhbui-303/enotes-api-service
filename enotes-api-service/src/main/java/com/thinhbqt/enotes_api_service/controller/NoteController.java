package com.thinhbqt.enotes_api_service.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.thinhbqt.enotes_api_service.dto.FavoriteNoteDto;
import com.thinhbqt.enotes_api_service.dto.NoteDto;
import com.thinhbqt.enotes_api_service.dto.NoteResponse;
import com.thinhbqt.enotes_api_service.endpoint.NoteEndpoint;
import com.thinhbqt.enotes_api_service.entity.FileDetails;
import com.thinhbqt.enotes_api_service.service.NoteService;
import com.thinhbqt.enotes_api_service.util.CommonUtil;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
public class NoteController implements NoteEndpoint {
    @Autowired
    NoteService noteService;

    @Override
    public ResponseEntity<?> saveNote(String notes, MultipartFile file)
            throws Exception {
        Boolean saved = noteService.saveNote(notes, file);
        if (saved) {
            return CommonUtil.createBuildResponseMessage(HttpStatus.OK, "Save successful");
        } else {
            return CommonUtil.createErrorResponseMessage("Something wrong!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> getAllNote() {
        List<NoteDto> notes = noteService.getAllNote();
        if (notes.isEmpty()) {
            ResponseEntity.noContent().build();
        }
        return CommonUtil.createBuildResponse(notes, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> downloadFile(Integer id) throws Exception {
        FileDetails fileDetails = noteService.getFileDetails(id);
        byte[] data = noteService.downloadFile(fileDetails);

        HttpHeaders header = new HttpHeaders();
        String setContentString = CommonUtil.setContentType(fileDetails.getOriginalFileName());
        MediaType mediaType = MediaType.parseMediaType(setContentString);
        header.setContentType(mediaType);
        header.setContentDispositionFormData("attachment", fileDetails.getOriginalFileName());

        return CommonUtil.createBuildResponse(data, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getAllNotePaginationByUser(Integer pageNo,
            Integer pageSize) {
        NoteResponse noteResponse = noteService.getAllNotePagination(pageNo, pageSize);
        return CommonUtil.createBuildResponse(noteResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> softDeleteNote(Integer id) {
        noteService.softDeleteNote(id);

        return CommonUtil.createBuildResponseMessage(HttpStatus.OK, "Delete success!");
    }

    @Override
    public ResponseEntity<?> restoreNote(Integer id) {
        noteService.restoreNote(id);

        return CommonUtil.createBuildResponseMessage(HttpStatus.OK, "Restored success!");
    }

    @Override
    public ResponseEntity<?> getNoteFromRecycleBin(Integer pageNo,
            Integer pageSize) {
        NoteResponse noteResponse = noteService.getNoteFromBinPagination(pageNo, pageSize);
        return CommonUtil.createBuildResponse(noteResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> hardDeleteNote(Integer id) {
        noteService.hardDeleteNote(id);

        return CommonUtil.createBuildResponseMessage(HttpStatus.OK, "Delete permanent success!");
    }

    @Override
    public ResponseEntity<?> saveFavoriteNote(Integer noteId) {
        noteService.saveFavoriteNote(noteId);

        return CommonUtil.createBuildResponseMessage(HttpStatus.CREATED, "save fav note success!");
    }

    @Override
    public ResponseEntity<?> deleteFavoriteNote(Integer favNoteId) {
        noteService.deleteFavoriteNote(favNoteId);

        return CommonUtil.createBuildResponseMessage(HttpStatus.OK, "Delete faveNote success!");
    }

    @Override
    public ResponseEntity<?> getFavoriteNote() {
        List<FavoriteNoteDto> favoriteNoteDtos = noteService.getFavoriteNote();
        if (ObjectUtils.isEmpty(favoriteNoteDtos)) {
            return ResponseEntity.noContent().build();
        }
        return CommonUtil.createBuildResponse(favoriteNoteDtos, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> copyNote(Integer id) {
        Boolean copied = noteService.copyNote(id);
        if (copied) {
            return CommonUtil.createBuildResponseMessage(HttpStatus.CREATED, "Copied");
        }
        return CommonUtil.createErrorResponseMessage("fail to copy!", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<?> searchNote(Integer pageNo, Integer pageSize, String key) {

        NoteResponse notesResponse = noteService.getNotesByUserSearch(pageNo, pageSize, key);
        return new ResponseEntity<>(notesResponse, HttpStatus.OK);
    }
}
