package com.thinhbqt.enotes_api_service.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class NoteResponse {
    private List<NoteDto> notes ;
    private Integer pageNo;
    private Integer pageSize ; 
    private Integer totalPages;
    private Long totalElements;
    private Boolean isFirst;
    private Boolean isLast;
}
