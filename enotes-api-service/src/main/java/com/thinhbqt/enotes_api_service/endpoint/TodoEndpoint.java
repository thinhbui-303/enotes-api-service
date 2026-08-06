package com.thinhbqt.enotes_api_service.endpoint;
import static com.thinhbqt.enotes_api_service.util.Constants.*;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.thinhbqt.enotes_api_service.dto.TodoDto;
import com.thinhbqt.enotes_api_service.util.ApiCommonResponses;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Todo", description = "All the Todo Operation APIs")
@ApiCommonResponses
@RequestMapping("api/v1/todo")
public interface TodoEndpoint {
    @Operation(summary = "Save Todo", tags = { "Todo" }, description = "Save Todo")
    @PreAuthorize(ADMIN_AND_USER)
    @PostMapping("/saveTodo")
    public ResponseEntity<?> saveTodo(@RequestBody TodoDto todoDto);

    @Operation(summary = "Get Todo", tags = { "Todo" }, description = "Get Todo")
    @PreAuthorize(ADMIN_AND_USER)
    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id);

    @Operation(summary = "Get All Todo By User", tags = { "Todo" }, description = "Get All Todo By User")
    @PreAuthorize(ADMIN_AND_USER)
    @GetMapping("/user-todo")
    public ResponseEntity<?> getTodoByUser();
}
