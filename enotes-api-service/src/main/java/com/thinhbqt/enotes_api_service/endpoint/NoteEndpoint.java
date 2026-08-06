package com.thinhbqt.enotes_api_service.endpoint;

import static com.thinhbqt.enotes_api_service.util.Constants.*;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.thinhbqt.enotes_api_service.dto.NoteRequest;
import com.thinhbqt.enotes_api_service.util.ApiCommonResponses;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Note", description = "All the Notes Operation APIs")
@ApiCommonResponses
@RequestMapping("api/v1/note")
public interface NoteEndpoint {
        @Operation(summary = "Save Notes", tags = { "Note", "User" }, description = "User Save Notes")
        @PreAuthorize(USER)
        @PostMapping(value = "/save-note", consumes = "multipart/form-data")
        public ResponseEntity<?> saveNote(
                        @RequestParam @Parameter(description = "Json String Notes", required = true, content = @Content(schema = @Schema(implementation = NoteRequest.class))) String notes,
                        @RequestParam(defaultValue = "") MultipartFile file)
                        throws Exception;

        @Operation(summary = "Get All Notes", tags = { "Note" }, description = "Get All Notes Admin")
        @PreAuthorize(ADMIN)
        @GetMapping("/notes")
        public ResponseEntity<?> getAllNote();

        @Operation(summary = "Download Upload File", tags = { "Note", "User" }, description = "Download file ")
        @PreAuthorize(USER)
        @GetMapping("/download/{id}")
        public ResponseEntity<?> downloadFile(@PathVariable Integer id) throws Exception;

        @Operation(summary = "Get All notes For User", tags = { "Note",
                        "User" }, description = "Get All notes For User")
        @PreAuthorize(USER)
        @GetMapping("/user-notes")
        public ResponseEntity<?> getAllNotePaginationByUser(
                        @RequestParam(value = "pageNo", defaultValue = DEFAULT_PAGE_NUMBER) Integer pageNo,
                        @RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize);

        @Operation(summary = "Delete Notes", tags = { "Note", "User" }, description = "Delete Notes By user")
        @PreAuthorize(USER)
        @GetMapping("/deleteNote/{id}")
        public ResponseEntity<?> softDeleteNote(@PathVariable Integer id);

        @Operation(summary = "Restore Delete Notes", tags = { "Note",
                        "User" }, description = "Restore Delete Notes from Recycle Bin")
        @PreAuthorize(USER)
        @GetMapping("/restoreNote/{id}")
        public ResponseEntity<?> restoreNote(@PathVariable Integer id);

        @Operation(summary = "Get Notes From Recycle Bin", tags = { "Note",
                        "User" }, description = "Get Notes From Recycle Bin")
        @PreAuthorize(USER)
        @GetMapping("/recycleBin")
        public ResponseEntity<?> getNoteFromRecycleBin(
                        @RequestParam(value = "pageNo", defaultValue = DEFAULT_PAGE_NUMBER) Integer pageNo,
                        @RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize);

        @Operation(summary = "Hard Delete Notes", tags = { "Note", "User" }, description = "Hard Delete Notes")
        @PreAuthorize(ADMIN_AND_USER)
        @DeleteMapping("/deletePermanentNote/{id}")
        public ResponseEntity<?> hardDeleteNote(@PathVariable Integer id);

        @Operation(summary = "Favorite Note", tags = { "Note", "User" }, description = "User favorite notes")
        @PreAuthorize(ADMIN_AND_USER)
        @GetMapping("/saveFavoriteNote/{id}")
        public ResponseEntity<?> saveFavoriteNote(@PathVariable Integer noteId);

        @Operation(summary = "Empty User Recycle Bin", tags = { "Note",
                        "User" }, description = "Empty User Recycle Bin")
        @PreAuthorize(ADMIN_AND_USER)
        @DeleteMapping("/deleteFavoriteNote/{id}")
        public ResponseEntity<?> deleteFavoriteNote(@PathVariable Integer favNoteId);

        @Operation(summary = "Get User Favorite Notes", tags = { "Note", "User" }, description = "User Favorite Notes")
        @PreAuthorize(ADMIN_AND_USER)
        @GetMapping("/getFavoriteNote")
        public ResponseEntity<?> getFavoriteNote();

        @Operation(summary = "Copy Notes", tags = { "Note", "User" }, description = "Copy Notes")
        @PreAuthorize(ADMIN_AND_USER)
        @GetMapping("/copyNote/{id}")
        public ResponseEntity<?> copyNote(@PathVariable Integer id);

        @Operation(summary = "Search Notes", tags = { "Note", "User" }, description = "User Search Notes")
        @PreAuthorize(ADMIN_AND_USER)
        @GetMapping("/search")
        public ResponseEntity<?> searchNote(
                        @RequestParam(name = "pageNo", defaultValue = DEFAULT_PAGE_NUMBER) Integer pageNo,
                        @RequestParam(name = "pageSize", defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
                        @RequestParam(name = "key", defaultValue = "") String key);
}
