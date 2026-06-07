package com.thinhbqt.enotes_api_service.dto;

import com.thinhbqt.enotes_api_service.entity.Note;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FavoriteNoteDto {

    private Integer id;

    private Note note;

    private Integer userId;
}
