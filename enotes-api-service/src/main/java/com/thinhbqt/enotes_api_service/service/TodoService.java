package com.thinhbqt.enotes_api_service.service;

import java.util.List;

import com.thinhbqt.enotes_api_service.dto.TodoDto;

public interface TodoService {
    public Boolean saveTodo(TodoDto todoDto);

    public TodoDto getTodoById(Integer id);
    
    public List<TodoDto> getTodoByUser();
}
