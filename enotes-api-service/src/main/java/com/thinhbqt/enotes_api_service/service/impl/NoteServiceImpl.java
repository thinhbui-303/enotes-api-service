package com.thinhbqt.enotes_api_service.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thinhbqt.enotes_api_service.dto.FavoriteNoteDto;
import com.thinhbqt.enotes_api_service.dto.NoteDto;
import com.thinhbqt.enotes_api_service.dto.NoteDto.CategoryDto;
import com.thinhbqt.enotes_api_service.dto.NoteDto.FileDetailsDto;
import com.thinhbqt.enotes_api_service.dto.NoteResponse;
import com.thinhbqt.enotes_api_service.entity.FavoriteNote;
import com.thinhbqt.enotes_api_service.entity.FileDetails;
import com.thinhbqt.enotes_api_service.entity.Note;
import com.thinhbqt.enotes_api_service.entity.User;
import com.thinhbqt.enotes_api_service.exception.ResourceNotFoundException;
import com.thinhbqt.enotes_api_service.repository.CategoryRepository;
import com.thinhbqt.enotes_api_service.repository.FavoriteNoteRepository;
import com.thinhbqt.enotes_api_service.repository.FileDetailsRepository;
import com.thinhbqt.enotes_api_service.repository.NoteRepository;
import com.thinhbqt.enotes_api_service.service.NoteService;
import com.thinhbqt.enotes_api_service.util.CommonUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {
    private final NoteRepository noteRepository;

    private final CategoryRepository categoryRepository;

    private final ModelMapper mapper;

    private final FavoriteNoteRepository favoriteNoteRepository;

    private final FileDetailsRepository fileDetailsRepository;

    @Value("${file.upload.path}")
    private String uploadPath;

    private void checkExistCategory(CategoryDto categoryDto) {
        categoryRepository.findById(categoryDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("can not found category"));
    }

    @Override
    public Boolean saveNote(String notes, MultipartFile file) throws Exception {
        log.info("NoteServiceImpl: Execution start: saveNote method by ", CommonUtil.getLoggedInUser().getEmail());
        ObjectMapper ob = new ObjectMapper();
        NoteDto noteDto = ob.readValue(notes, NoteDto.class);

        noteDto.setIsDeleted(false);
        noteDto.setDeletedOn(null);

        FileDetails fileDetails = saveFileDetails(file);

        if (!ObjectUtils.isEmpty(noteDto.getId())) {

            updateNote(noteDto, file);
            log.info("Execution success: update Note done!");
        }
        checkExistCategory(noteDto.getCategoryDto());

        Note note = mapper.map(noteDto, Note.class);

        if (!ObjectUtils.isEmpty(fileDetails)) {
            note.setFileDetails(fileDetails);
        } else {
            if (ObjectUtils.isEmpty(noteDto.getId()))
                note.setFileDetails(null);

        }
        Note saveNote = noteRepository.save(note);
        if (!ObjectUtils.isEmpty(saveNote)) {
            log.info("Execution success: save Note done!");
            return true;
        }
        log.info("Execution end: saveNote method!");
        return false;

    }

    private void updateNote(NoteDto noteDto, MultipartFile file) {
        Note check = noteRepository.findById(noteDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Id note not found!"));
        if (ObjectUtils.isEmpty(file)) {
            noteDto.setFileDetailsDto(mapper.map(check.getFileDetails(), FileDetailsDto.class));
        }
    }

    private FileDetails saveFileDetails(MultipartFile file) throws Exception {
        if (!file.isEmpty()) {
            FileDetails fileDtls = new FileDetails();
            fileDtls.setOriginalFileName(file.getOriginalFilename());
            fileDtls.setDisplayFileName(getDisplayName(file.getOriginalFilename()));

            String randomString = UUID.randomUUID().toString();
            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            List<String> extensionAllow = Arrays.asList("pdf", "xlsx", "png", "docx", "jpg");
            if (!extensionAllow.contains(extension.toLowerCase())) {
                throw new IllegalArgumentException("invalid file format! upload only pdf, jpg, png, docx, xlsx");
            }
            String uploadfileName = randomString + "." + extension;
            fileDtls.setUploadFileName(uploadfileName);
            fileDtls.setFileSize(file.getSize());
            File saveFile = new File(uploadPath);
            if (!saveFile.exists()) {
                saveFile.mkdir();
            }
            String storePath = uploadPath.concat(uploadfileName);
            fileDtls.setPath(storePath);
            long upload = Files.copy(file.getInputStream(), Paths.get(storePath));
            if (upload != 0) {
                FileDetails saveFileDtls = fileDetailsRepository.save(fileDtls);
                return saveFileDtls;
            }
        }
        return null;
    }

    private String getDisplayName(String originalFileName) {
        String extension = FilenameUtils.getExtension(originalFileName);
        String fileName = FilenameUtils.removeExtension(originalFileName);
        if (fileName.length() > 8) {
            fileName = fileName.substring(0, 7);
        }
        fileName = fileName + "." + extension;
        return fileName;
    }

    @Override
    public List<NoteDto> getAllNote() {
        return noteRepository.findAll()
                .stream()
                .map(note -> mapper.map(note, NoteDto.class))
                .toList();
    }

    @Override
    public FileDetails getFileDetails(Integer id) {
        return fileDetailsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found id file"));
    }

    @Override
    public byte[] downloadFile(FileDetails fileDetails) throws Exception {
        log.info("NoteServiceImpl: Execution start: downloadFile method by ", CommonUtil.getLoggedInUser().getEmail());
        InputStream inputStream = new FileInputStream(fileDetails.getPath());
        byte[] data = StreamUtils.copyToByteArray(inputStream);
        inputStream.close();
        log.info("Execution end: downloadFile method done! ");
        return data;
    }

    @Override
    public NoteResponse getAllNotePagination(Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        Page<Note> notes = noteRepository.findByCreatedByAndIsDeletedFalse(CommonUtil.getLoggedInUser().getId(),
                pageable);

        List<NoteDto> noteDtos = notes.get().map(note -> mapper.map(note, NoteDto.class)).toList();
        NoteResponse noteResponse = NoteResponse.builder().pageNo(pageNo).pageSize(pageSize)
                .notes(noteDtos)
                .totalElements(notes.getTotalElements()).totalPages(notes.getTotalPages())
                .isFirst(notes.isFirst()).isLast(notes.isLast()).build();
        return noteResponse;
    }

    @Override
    public void softDeleteNote(Integer id) {
        Note note = noteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Not found id note!"));
        note.setIsDeleted(true);
        note.setDeletedOn(LocalDateTime.now());
        noteRepository.save(note);
    }

    @Override
    public void restoreNote(Integer id) {
        Note note = noteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Not found id note!"));
        note.setIsDeleted(false);
        note.setDeletedOn(null);
        noteRepository.save(note);
    }

    @Override
    public NoteResponse getNoteFromBinPagination(Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        Page<Note> notes = noteRepository.findByCreatedByAndIsDeletedTrue(CommonUtil.getLoggedInUser().getId(),
                pageable);

        List<NoteDto> noteDto = notes.get().map(note -> mapper.map(note, NoteDto.class)).toList();

        NoteResponse noteResponse = NoteResponse.builder().notes(noteDto)
                .totalElements(notes.getTotalElements())
                .totalPages(notes.getTotalPages())
                .pageNo(pageNo).pageSize(pageSize)
                .isFirst(notes.isFirst())
                .isLast(notes.isLast()).build();

        return noteResponse;
    }

    @Override
    public void hardDeleteNote(Integer id) {
        log.info("NoteServiceImpl: Execution start: hardDeleteNote method by ", CommonUtil.getLoggedInUser().getEmail());
        Note note = noteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Not found id note!"));
        noteRepository.delete(note);
        noteRepository.save(note);
        log.info("Execution end: hardDeleteNote method! ");
    }

    @Override
    public void saveFavoriteNote(Integer noteId) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("not found note id!"));
        FavoriteNote favoriteNote = FavoriteNote.builder().userId(CommonUtil.getLoggedInUser().getId()).note(note)
                .build();
        favoriteNoteRepository.save(favoriteNote);
    }

    @Override
    public void deleteFavoriteNote(Integer favoriteNoteId) {
        FavoriteNote favoriteNote = favoriteNoteRepository.findById(favoriteNoteId)
                .orElseThrow(() -> new ResourceNotFoundException("id not found!"));
        favoriteNoteRepository.delete(favoriteNote);
    }

    @Override
    public List<FavoriteNoteDto> getFavoriteNote() {
        log.info("NoteServiceImpl: Execution start: getFavoriteNote method by ", CommonUtil.getLoggedInUser().getEmail());
        return favoriteNoteRepository.findByUserId(CommonUtil.getLoggedInUser().getId())
                .stream().map(favNote -> mapper.map(favNote, FavoriteNoteDto.class)).toList();
    }

    @Override
    public Boolean copyNote(Integer id) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("id not found!"));
        Note copNote = Note.builder().title(note.getTitle()).category(note.getCategory())
                .description(note.getDescription()).isDeleted(false).fileDetails(null).build();
        Note saveCopyNote = noteRepository.save(copNote);
        if (ObjectUtils.isEmpty(saveCopyNote)) {
            return false;
        }
        return true;
    }

    @Override
    public NoteResponse getNotesByUserSearch(Integer pageNo, Integer pageSize, String keyword) {
        log.info("NoteServiceImpl: Execution start: getNotesByUserSearch method ");
        // 1. Lấy thông tin User hiện tại từ Security Context
        User loggedInUser = CommonUtil.getLoggedInUser();
        Integer userId = loggedInUser.getId();

        Pageable pageable = PageRequest.of(pageNo, pageSize);

        Page<Note> notes = noteRepository.searchNotes(keyword, userId, pageable);

        List<NoteDto> notesDtoList = notes.getContent()
                .stream()
                .map(note -> mapper.map(note, NoteDto.class))
                .toList();

        NoteResponse noteResponse = NoteResponse.builder().notes(notesDtoList)
                .totalElements(notes.getTotalElements())
                .totalPages(notes.getTotalPages())
                .pageNo(pageNo).pageSize(pageSize)
                .isFirst(notes.isFirst())
                .isLast(notes.isLast()).build();
        log.info("Execution end: getNotesByUserSearch method!");
        return noteResponse;
    }

}
