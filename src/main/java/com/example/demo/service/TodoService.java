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

    public Todo findById(Long id) {
        return todoMapper.findById(id);
    }

    public int createTodo(String title) {
        Todo todo = Todo.builder()
                .title(title)
                .completed(false)
                .build();
        return todoMapper.insert(todo);
    }

    public int update(Long id, String title) {
        Todo existing = todoMapper.findById(id);
        if (existing == null) {
            return 0;
        }
        Todo todo = Todo.builder()
                .id(id)
                .title(title)
                .completed(existing.getCompleted())
                .build();
        return todoMapper.update(todo);
    }

    public int toggleCompleted(Long id) {
        Todo existing = todoMapper.findById(id);
        if (existing == null) {
            return 0;
        }
        Todo todo = Todo.builder()
                .id(existing.getId())
                .title(existing.getTitle())
                .completed(!Boolean.TRUE.equals(existing.getCompleted()))
                .build();
        return todoMapper.update(todo);
    }

    public int deleteById(Long id) {
        return todoMapper.deleteById(id);
    }
}
