package com.thinhbqt.enotes_api_service.dto;

import java.time.LocalDateTime;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NoteDto {
    private Integer id ; 

    private String title;

    private String description;

    private CategoryDto categoryDto;

    private Integer createdBy;

    private Date createdOn;

    private Integer updatedBy;

    private Date updatedOn;

    private Boolean isDeleted;

    private LocalDateTime deletedOn;
    
    private FileDetailsDto fileDetailsDto;
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CategoryDto {
        private Integer id;
        private String name;    
    }
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FileDetailsDto {
   
    private Integer id; 

    private String originalFileName;

    private  String displayFileName;

  
    }
}
