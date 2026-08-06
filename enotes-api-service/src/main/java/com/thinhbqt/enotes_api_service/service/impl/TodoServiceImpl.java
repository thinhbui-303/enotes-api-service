package com.thinhbqt.enotes_api_service.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.thinhbqt.enotes_api_service.dto.TodoDto;
import com.thinhbqt.enotes_api_service.entity.Todo;
import com.thinhbqt.enotes_api_service.exception.ResourceNotFoundException;
import com.thinhbqt.enotes_api_service.repository.TodoRepository;
import com.thinhbqt.enotes_api_service.service.TodoService;
import com.thinhbqt.enotes_api_service.util.CommonUtil;
import com.thinhbqt.enotes_api_service.util.Validation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;

    private final ModelMapper mapper;

    private final Validation validation;

    @Override
    public Boolean saveTodo(TodoDto todoDto) {
        log.info("TodoServiceImpl: Execution start: saveTodo method!");
        validation.todoValidation(todoDto);
        Todo todo = mapper.map(todoDto, Todo.class);
        todo.setStatusId(todoDto.getStatus().getId());
        Todo saveTodo = todoRepository.save(todo);
        if (ObjectUtils.isEmpty(saveTodo)) {
            log.info("Execution fail: save todo fail!");
            return false;
        }
        log.info("Execution success: saveTodo method done!");

        log.info("Execution end: saveTodo method!");

        return true;
    }

    @Override
    public TodoDto getTodoById(Integer id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Id not found!"));
        return mapper.map(todo, TodoDto.class);
    }

    @Override
    public List<TodoDto> getTodoByUser() {
        log.info("TodoServiceImpl: Execution start: getTodoByUser method by!", CommonUtil.getLoggedInUser().getEmail());

        List<Todo> todos = todoRepository.findByCreatedBy(CommonUtil.getLoggedInUser().getId());
        return todos.stream().map(todo -> mapper.map(todos, TodoDto.class)).toList();
    }

}
