package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.mapper.TodoMapper;
import com.example.demo.model.Todo;

@Service
public class TodoService {

    private final TodoMapper todoMapper;

    public TodoService(TodoMapper todoMapper) {
        this.todoMapper = todoMapper;
    }

    public List<Todo> findAll() {
        return todoMapper.findAll();
    }

    public int createTodo(String title) {
        Todo todo = Todo.builder()
                .title(title)
                .completed(false)
                .build();
        return todoMapper.insert(todo);
    }
}
