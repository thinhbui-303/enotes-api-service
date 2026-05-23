package com.thinhbqt.enotes_api_service.service.impl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thinhbqt.enotes_api_service.dto.NoteDto;
import com.thinhbqt.enotes_api_service.dto.NoteDto.CategoryDto;
import com.thinhbqt.enotes_api_service.entity.FileDetails;
import com.thinhbqt.enotes_api_service.entity.Note;
import com.thinhbqt.enotes_api_service.exception.ResourceNotFoundException;
import com.thinhbqt.enotes_api_service.repository.CategoryRepository;
import com.thinhbqt.enotes_api_service.repository.FileDetailsRepository;
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

    @Autowired
    private FileDetailsRepository fileDetailsRepository;

    @Value("${file.upload.path}")
    private String uploadPath;
    private void checkExistCategory(CategoryDto categoryDto){
        categoryRepository.findById(categoryDto.getId())
        .orElseThrow(() -> new ResourceNotFoundException("can not found category"));
    }
    @Override
    public Boolean saveNote(String notes , MultipartFile file)throws Exception{

        ObjectMapper ob = new ObjectMapper();
        NoteDto noteDto = ob.readValue(notes, NoteDto.class);

        FileDetails fileDetails = saveFileDetails(file);
        checkExistCategory(noteDto.getCategoryDto());

        Note note = mapper.map(noteDto, Note.class);

        if(!ObjectUtils.isEmpty(fileDetails)){
            note.setFileDetails(fileDetails);
        }
        else{
            if(ObjectUtils.isEmpty(noteDto.getId()))
                note.setFileDetails(null);
            
        }
        Note saveNote =  noteRepository.save(note);
        if(!ObjectUtils.isEmpty(saveNote)){
            
            return true;
        }
        else return false;

    }
    private FileDetails saveFileDetails(MultipartFile file)throws Exception{
        if(!file.isEmpty()){
            FileDetails fileDtls = new FileDetails();
            fileDtls.setOriginalFileName(file.getOriginalFilename());
            fileDtls.setDisplayFileName(getDisplayName(file.getOriginalFilename()));
            
            String randomString = UUID.randomUUID().toString();
            String extension = FilenameUtils.getExtension(file.getOriginalFilename()) ;
            List<String> extensionAllow = Arrays.asList("pdf", "xlsx", "png", "docx", "jpg");
            if(!extensionAllow.contains(extension.toLowerCase())){
                throw new IllegalArgumentException("invalid file format! upload only pdf, jpg, png, docx, xlsx");
            }
            String uploadfileName = randomString + "." + extension;
            fileDtls.setUploadFileName(uploadfileName);
            fileDtls.setFileSize(file.getSize());
            File saveFile = new File(uploadPath);
            if(!saveFile.exists()){
                saveFile.mkdir();
            }
            String storePath = uploadPath.concat(uploadfileName);
            fileDtls.setPath(storePath);
            long upload = Files.copy(file.getInputStream(), Paths.get(storePath));
            if(upload != 0){
                FileDetails saveFileDtls = fileDetailsRepository.save(fileDtls);
                return saveFileDtls;
            }
        }
        return null; 
    }
    private String getDisplayName(String originalFileName){
        String extension = FilenameUtils.getExtension(originalFileName);
        String fileName = FilenameUtils.removeExtension(originalFileName);
        if(fileName.length()> 8){
            fileName = fileName.substring(0, 7);
        }
        fileName = fileName + "." + extension;
        return fileName;
    }
    @Override
    public List<NoteDto> getAllNote(){
        return noteRepository.findAll()
        .stream()
        .map(note -> mapper.map(note, NoteDto.class))
        .toList();
    }
    
}
