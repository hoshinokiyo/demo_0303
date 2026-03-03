package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping
    public String adminTop(HttpSession session, RedirectAttributes redirectAttributes) {
        if (!isMom(session)) {
            redirectAttributes.addFlashAttribute("errorMessage", "\u5165\u308c\u306a\u3044\u3088\uff01");
            return "redirect:/todo";
        }
        seedAdminFlagsIfMissing(session);
        return "admin/admin";
    }

    @GetMapping("/approve")
    public String approve(HttpSession session, RedirectAttributes redirectAttributes) {
        if (!isMom(session)) {
            redirectAttributes.addFlashAttribute("errorMessage", "\u5165\u308c\u306a\u3044\u3088\uff01");
            return "redirect:/todo";
        }
        seedAdminFlagsIfMissing(session);
        boolean adminPasswordSet = getBoolean(session, "adminPasswordSet");
        boolean adminAuthed = getBoolean(session, "adminAuthed");
        if (!adminPasswordSet || !adminAuthed) {
            redirectAttributes.addFlashAttribute("errorMessage", "\u5165\u308c\u306a\u3044\u3088\uff01");
            return "redirect:/admin";
        }
        return "admin/approve";
    }

    @GetMapping("/password")
    public String passwordStub(HttpSession session, RedirectAttributes redirectAttributes, Model model) {
        if (!isMom(session)) {
            redirectAttributes.addFlashAttribute("errorMessage", "\u5165\u308c\u306a\u3044\u3088\uff01");
            return "redirect:/todo";
        }
        seedAdminFlagsIfMissing(session);
        model.addAttribute("passwordSet", getBoolean(session, "adminPasswordSet"));
        return "admin/password";
    }

    private boolean isMom(HttpSession session) {
        Object currentUser = session.getAttribute("currentUser");
        if (currentUser == null) {
            return false;
        }
        String user = currentUser.toString();
        return "\u6bcd".equals(user) || "mom".equals(user);
    }

    private void seedAdminFlagsIfMissing(HttpSession session) {
        if (session.getAttribute("adminPasswordSet") == null) {
            session.setAttribute("adminPasswordSet", Boolean.FALSE);
        }
        if (session.getAttribute("adminAuthed") == null) {
            session.setAttribute("adminAuthed", Boolean.FALSE);
        }
    }

    private boolean getBoolean(HttpSession session, String key) {
        Object value = session.getAttribute(key);
        return value instanceof Boolean && (Boolean) value;
    }
}
