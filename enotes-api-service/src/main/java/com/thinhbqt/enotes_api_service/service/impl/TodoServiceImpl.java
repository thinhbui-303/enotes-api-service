package com.thinhbqt.enotes_api_service.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.thinhbqt.enotes_api_service.dto.TodoDto;
import com.thinhbqt.enotes_api_service.entity.Todo;
import com.thinhbqt.enotes_api_service.exception.ResourceNotFoundException;
import com.thinhbqt.enotes_api_service.repository.TodoRepository;
import com.thinhbqt.enotes_api_service.service.TodoService;
import com.thinhbqt.enotes_api_service.util.Validation;

@Service
public class TodoServiceImpl implements TodoService {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private Validation validation;

    @Override
    public Boolean saveTodo(TodoDto todoDto) {
        validation.todoValidation(todoDto);
        Todo todo = mapper.map(todoDto, Todo.class);
        todo.setStatusId(todoDto.getStatus().getId());
        Todo saveTodo = todoRepository.save(todo);
        if (ObjectUtils.isEmpty(saveTodo)) {
            return false;
        }
        return true;
    }

    @Override
    public TodoDto getTodoById(Integer id) {
        Todo todo = todoRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Id not found!"));
        return mapper.map(todo, TodoDto.class);
    }
    @Override
    public List<TodoDto> getTodoByUser(){
        List<Todo> todos = todoRepository.findByCreatedBy(1);
        return todos.stream().map(todo -> mapper.map(todos, TodoDto.class)).toList();
    }

}
