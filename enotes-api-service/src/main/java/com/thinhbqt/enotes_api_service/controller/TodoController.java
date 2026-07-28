package com.thinhbqt.enotes_api_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thinhbqt.enotes_api_service.dto.TodoDto;
import com.thinhbqt.enotes_api_service.service.TodoService;
import com.thinhbqt.enotes_api_service.util.CommonUtil;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;



@RestController
@RequestMapping("api/v1/todo")
public class TodoController {
    @Autowired
    TodoService todoService;

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping("/saveTodo")
    public ResponseEntity<?> saveTodo(@RequestBody TodoDto todoDto) {
        todoService.saveTodo(todoDto);
        return CommonUtil.createBuildResponseMessage(HttpStatus.CREATED, "Save todo successful");
    }
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        TodoDto todoDto = todoService.getTodoById(id);
        return CommonUtil.createBuildResponse(todoDto, HttpStatus.OK);
    }
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/getTodoByUser")
    public ResponseEntity<?> getTodoByUser() {
        List<TodoDto> todos= todoService.getTodoByUser();
        if(ObjectUtils.isEmpty(todos)){
            return ResponseEntity.noContent().build();
        }
        return CommonUtil.createBuildResponse(todos, HttpStatus.OK);
    }
    
}
