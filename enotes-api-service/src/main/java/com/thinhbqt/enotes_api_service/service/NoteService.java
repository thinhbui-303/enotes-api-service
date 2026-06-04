package com.thinhbqt.enotes_api_service.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.thinhbqt.enotes_api_service.dto.NoteDto;
import com.thinhbqt.enotes_api_service.dto.NoteResponse;
import com.thinhbqt.enotes_api_service.entity.FileDetails;

public interface NoteService {
    public Boolean saveNote(String notes , MultipartFile file)throws Exception;

    public List<NoteDto> getAllNote();

    public FileDetails getFileDetails(Integer id);
    
    public byte[] downloadFile(FileDetails fileDetails)throws Exception;

    public NoteResponse getAllNotePagination(Integer uid , Integer pageNo, Integer pageSize);
}
