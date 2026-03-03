package com.example.demo;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;

import com.example.demo.model.Todo;
import com.example.demo.service.TodoService;

@Controller
@RequestMapping("/todo")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping
    public String list(Model model, HttpSession session) {
        List<Todo> todoList = todoService.findAll();
        String currentUser = (String) session.getAttribute("currentUser");
        model.addAttribute("todos", todoList);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("currentUserCode", ownerNameToCode(currentUser));
        return "todo/list";
    }

    @PostMapping("/select-user")
    public String selectUser(@RequestParam(value = "currentUser", required = false) String currentUser,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        if (currentUser == null || currentUser.isBlank()) {
            redirectAttributes.addFlashAttribute("errorMessage", "\u3060\u308c\u304c\u3064\u304b\u3046\u304b\u3048\u3089\u3093\u3067\u306d");
            return "redirect:/todo/select-user";
        }
        session.setAttribute("currentUser", currentUser);
        return "redirect:/todo/new";
    }

    @GetMapping("/select-user")
    public String selectUserPage(HttpSession session) {
        session.removeAttribute("currentUser");
        return "todo/select-user";
    }

    @PostMapping("/change-user")
    public String changeUser(HttpSession session) {
        session.removeAttribute("currentUser");
        return "redirect:/todo";
    }

    @GetMapping("/new")
    public String newTodo(@RequestParam(value = "owner", required = false) String owner,
                          HttpSession session,
                          Model model) {
        if (owner != null && !owner.isBlank()) {
            String mappedOwner = ownerCodeToName(owner);
            if (mappedOwner != null) {
                session.setAttribute("currentUser", mappedOwner);
            }
        }
        String currentUser = (String) session.getAttribute("currentUser");
        if (currentUser == null || currentUser.isBlank()) {
            return "redirect:/todo";
        }
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("owner", ownerNameToCode(currentUser));
        return "todo/form";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        Todo todo = todoService.findById(id);
        if (todo == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "ToDo\u304c\u898b\u3064\u304b\u308a\u307e\u305b\u3093");
            return "redirect:/todo";
        }
        model.addAttribute("todo", todo);
        return "todo/edit";
    }

    @PostMapping("/confirm")
    public String confirm(@RequestParam("title") String title, Model model) {
        model.addAttribute("title", title);
        return "todo/confirm";
    }

    @PostMapping("/complete")
    public String complete(@RequestParam("title") String title, HttpSession session) {
        String currentUser = (String) session.getAttribute("currentUser");
        if (currentUser == null || currentUser.isBlank()) {
            return "redirect:/todo";
        }
        todoService.createTodo(title, currentUser);
        return "redirect:/todo";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable("id") Long id,
                         @RequestParam("title") String title,
                         RedirectAttributes redirectAttributes) {
        int updatedRows = todoService.update(id, title);
        if (updatedRows > 0) {
            redirectAttributes.addFlashAttribute("successMessage", "\u66f4\u65b0\u304c\u5b8c\u4e86\u3057\u307e\u3057\u305f");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "\u66f4\u65b0\u306b\u5931\u6557\u3057\u307e\u3057\u305f");
        }
        return "redirect:/todo";
    }

    @PostMapping("/{id}/toggle")
    public String toggle(@PathVariable("id") Long id) {
        todoService.toggleCompleted(id);
        return "redirect:/todo";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        int deletedRows = todoService.deleteById(id);
        if (deletedRows > 0) {
            redirectAttributes.addFlashAttribute("successMessage", "ToDo\u3092\u524a\u9664\u3057\u307e\u3057\u305f");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "\u524a\u9664\u306b\u5931\u6557\u3057\u307e\u3057\u305f");
        }
        return "redirect:/todo";
    }

    private String ownerCodeToName(String ownerCode) {
        return switch (ownerCode) {
            case "mom", "\u6bcd" -> "\u6bcd";
            case "dad", "\u7236" -> "\u7236";
            case "me", "\u50d5" -> "\u50d5";
            case "sis", "\u59c9" -> "\u59c9";
            default -> null;
        };
    }

    private String ownerNameToCode(String ownerName) {
        if (ownerName == null || ownerName.isBlank()) {
            return null;
        }
        return switch (ownerName) {
            case "mom", "\u6bcd" -> "mom";
            case "dad", "\u7236" -> "dad";
            case "me", "\u50d5" -> "me";
            case "sis", "\u59c9" -> "sis";
            default -> null;
        };
    }
}
