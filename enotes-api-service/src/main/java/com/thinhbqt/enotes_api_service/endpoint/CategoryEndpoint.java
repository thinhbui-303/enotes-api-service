package com.thinhbqt.enotes_api_service.endpoint;

import static com.thinhbqt.enotes_api_service.util.Constants.*;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.thinhbqt.enotes_api_service.dto.CategoryDto;
import com.thinhbqt.enotes_api_service.util.ApiCommonResponses;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Category")
@ApiCommonResponses
@RequestMapping("/api/v1/category")
public interface CategoryEndpoint {

    @Operation(summary = "add and update category",  tags = {"Category"}, description = "admin save category")
    @PreAuthorize(ADMIN)
    @PostMapping("/save-category")
    public ResponseEntity<?> saveCategory(@Valid @RequestBody CategoryDto category);

    @Operation(summary = "get all category",  tags = {"Category"}, description = "admin get all category")
    @PreAuthorize(ADMIN)
    @GetMapping("/categories")
    public ResponseEntity<?> getAllCategory();

    @Operation(summary = "get all active category", tags = {"Category"}, description = "admin all active category")
    @PreAuthorize(ADMIN_AND_USER)
    @GetMapping("/active-categories")
    public ResponseEntity<?> getIsActiveCategory();

    @Operation(summary = "get all category by id",  tags = {"Category"}, description = "admin category by id")
    @PreAuthorize(ADMIN)
    @GetMapping("/getCategory/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Integer id);

    @Operation(summary = "remove category by id", tags = {"Category"}, description = "admin delete category by id")
    @PreAuthorize(ADMIN)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Integer id) throws Exception;
}
