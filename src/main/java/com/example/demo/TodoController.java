package com.example.demo;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/todo")
public class TodoController {

    @GetMapping
    public String list(Model model) {
        List<TodoItem> todoList = List.of(
                new TodoItem(1L, "Learn Spring Boot", "TODO"),
                new TodoItem(2L, "Build todo list page", "DOING"),
                new TodoItem(3L, "Implement controller", "DONE")
        );
        model.addAttribute("todoList", todoList);
        return "todo/list";
    }

    public record TodoItem(Long id, String title, String status) {
    }
}