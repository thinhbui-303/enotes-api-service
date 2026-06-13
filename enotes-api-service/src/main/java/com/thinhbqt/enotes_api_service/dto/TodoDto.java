package com.thinhbqt.enotes_api_service.dto;

import java.util.Date;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TodoDto {
    private Integer id ; 

    private String title;

    private StatusDto status;

    private Integer createdBy;

    private Date createdOn;

    private Integer updatedBy;

    private Date updatedOn;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StatusDto {
        private Integer id; 
        private String name;
    }
}
