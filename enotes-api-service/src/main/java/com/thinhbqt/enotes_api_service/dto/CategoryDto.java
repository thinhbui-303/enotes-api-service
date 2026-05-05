package com.thinhbqt.enotes_api_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CategoryDto {
    private Integer id;

    @NotBlank
    @Size(min = 3, max = 100)
    private String name;

    @NotBlank
    @Size(min = 10, max = 1000)
    private String description;

    @NotNull
    private Boolean isActive;

    private Integer createdBy;

    private Date createdOn;

    private Integer updatedBy;

    private Date updatedOn;

}
