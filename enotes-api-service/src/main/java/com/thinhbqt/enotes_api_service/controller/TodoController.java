package com.thinhbqt.enotes_api_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RestController;

import com.thinhbqt.enotes_api_service.dto.TodoDto;
import com.thinhbqt.enotes_api_service.endpoint.TodoEndpoint;
import com.thinhbqt.enotes_api_service.service.TodoService;
import com.thinhbqt.enotes_api_service.util.CommonUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TodoController implements TodoEndpoint {

    private final TodoService todoService;

    @Override
    public ResponseEntity<?> saveTodo(TodoDto todoDto) {
        todoService.saveTodo(todoDto);
        return CommonUtil.createBuildResponseMessage(HttpStatus.CREATED, "Save todo successful");
    }

    @Override
    public ResponseEntity<?> getById(Integer id) {
        TodoDto todoDto = todoService.getTodoById(id);
        return CommonUtil.createBuildResponse(todoDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getTodoByUser() {
        List<TodoDto> todos = todoService.getTodoByUser();
        if (ObjectUtils.isEmpty(todos)) {
            return ResponseEntity.noContent().build();
        }
        return CommonUtil.createBuildResponse(todos, HttpStatus.OK);
    }

}
