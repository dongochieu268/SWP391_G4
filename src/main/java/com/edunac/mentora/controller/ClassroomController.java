package com.edunac.mentora.controller;
import com.edunac.mentora.service.ClassroomService;
import com.edunac.mentora.domain.Classroom;

import com.edunac.mentora.domain.User;
import com.edunac.mentora.repository.LearningPathRepository;
import com.edunac.mentora.repository.SubjectRepository;
import com.edunac.mentora.service.SemesterService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/teacher/classrooms")
public class ClassroomController {

    @Autowired
    private ClassroomService classroomService;

    @Autowired
    private SemesterService semesterService;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private LearningPathRepository learningPathRepository;

    // DANH SÁCH LỚP HỌC
    @GetMapping
    public String listClassrooms(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        List<Classroom> classrooms = classroomService.getClassroomsByTeacher(user.getId());
        model.addAttribute("classrooms", classrooms);
        model.addAttribute("user", user);
        return "teacher/index";
    }

    // TRANG TẠO LỚP HỌC (UC25)
    @GetMapping("/create")
    public String createPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        model.addAttribute("user", user);
        model.addAttribute("semesters", semesterService.findAll());
        model.addAttribute("subjects", subjectRepository.findByStatusOrderByNameAsc("ACTIVE"));
        model.addAttribute("learningPaths", learningPathRepository.findAll());
        return "teacher/classroom-create";
    }

    // XỬ LÝ TẠO LỚP HỌC (UC25)
    @PostMapping("/create")
    public String createClassroom(
            @RequestParam String name,
            @RequestParam Integer subjectId,
            @RequestParam Integer learningPathId,
            @RequestParam Integer semesterId,
            HttpSession session,
            Model model
    ) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        Classroom created = classroomService.createClassroom(name, subjectId, learningPathId, semesterId, user.getId());

        model.addAttribute("newClassroom", created);
        model.addAttribute("user", user);
        return "teacher/classroom-create-success";
    }
}
