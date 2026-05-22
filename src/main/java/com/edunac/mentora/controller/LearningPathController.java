package com.edunac.mentora.controller;
import com.edunac.mentora.service.LearningPathService;
import com.edunac.mentora.domain.LearningPath;

import com.edunac.mentora.domain.User;
import com.edunac.mentora.repository.SubjectRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/teacher/learning-paths")
public class LearningPathController {

    @Autowired
    private LearningPathService learningPathService;

    @Autowired
    private SubjectRepository subjectRepository;

    // Danh sách lộ trình của giáo viên
    @GetMapping
    public String list(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        List<LearningPath> paths = learningPathService.getPathsByTeacher(user.getId());
        model.addAttribute("paths", paths);
        model.addAttribute("user", user);
        return "teacher/learning-path-list";
    }

    // Form tạo lộ trình mới
    @GetMapping("/create")
    public String createPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        model.addAttribute("subjects", subjectRepository.findByStatusOrderByNameAsc("ACTIVE"));
        model.addAttribute("user", user);
        return "teacher/learning-path-create";
    }

    // Xử lý tạo lộ trình
    @PostMapping("/create")
    public String create(
            @RequestParam String name,
            @RequestParam(required = false) String description,
            @RequestParam Integer subjectId,
            HttpSession session,
            Model model
    ) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        try {
            LearningPath path = learningPathService.create(name, description, subjectId, user.getId());
            // Sau khi tạo xong → chuyển thẳng vào trang quản lý node
            return "redirect:/teacher/learning-path/" + path.getId() + "/nodes";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("subjects", subjectRepository.findByStatusOrderByNameAsc("ACTIVE"));
            model.addAttribute("user", user);
            return "teacher/learning-path-create";
        }
    }
}
