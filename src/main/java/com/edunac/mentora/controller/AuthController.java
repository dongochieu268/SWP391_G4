package com.edunac.mentora.controller;

import com.edunac.mentora.domain.User;
import com.edunac.mentora.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private AuthService authService;

    // LOGIN PAGE
    @GetMapping("/login")
    public String loginPage() {

        return "auth/authentication-login";
    }

    // REGISTER PAGE
    @GetMapping("/register")
    public String registerPage() {

        return "auth/authentication-register";
    }

    // HANDLE REGISTER
    @PostMapping("/register")
    public String register(
            @RequestParam String fullName,
            @RequestParam String email,
            @RequestParam String password
    ) {

        authService.register(fullName, email, password);

        return "redirect:/login";
    }

    // HANDLE LOGIN
    @PostMapping("/login")
    public String login(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session
    ) {

        User user = authService.login(email, password);

        // LOGIN FAIL
        if (user == null) {

            return "redirect:/login";
        }

        // SAVE SESSION
        session.setAttribute("user", user);

        // ROLE REDIRECT
        if (user.getRoleId() == 1) {

            return "admin/index";
        }

        if (user.getRoleId() == 2) {

            return "teacher/index";
        }

        return "student/index";
    }

    // LOGOUT
    @GetMapping("/logout")
    public String logout(HttpSession session) {

        session.invalidate();

        return "redirect:/login";
    }
}