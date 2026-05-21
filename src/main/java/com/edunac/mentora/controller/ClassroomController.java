package com.edunac.mentora.controller;

import com.edunac.mentora.domain.Classroom;
import com.edunac.mentora.domain.User;
import com.edunac.mentora.service.ClassroomService;
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

    // DANH SÁCH LỚP HỌC
    @GetMapping
    public String listClassrooms(HttpSession session, Model model) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        List<Classroom> classrooms = classroomService.getClassroomsByTeacher(user.getId());
        model.addAttribute("classrooms", classrooms);
        model.addAttribute("user", user);

        return "teacher/classroom-list";
    }

    // TRANG TẠO LỚP HỌC (UC25)
    @GetMapping("/create")
    public String createPage(HttpSession session, Model model) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);

        return "teacher/classroom-create";
    }

    // XỬ LÝ TẠO LỚP HỌC (UC25)
    @PostMapping("/create")
    public String createClassroom(
            @RequestParam String subject,
            @RequestParam String path,
            @RequestParam String semester,
            HttpSession session,
            Model model
    ) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        Classroom created = classroomService.createClassroom(subject, path, semester, user.getId());

        // Sau khi tạo xong, chuyển sang trang danh sách và hiển thị thông báo
        model.addAttribute("successMessage", "Tạo lớp học thành công!");
        model.addAttribute("newClassroom", created);
        model.addAttribute("user", user);

        return "teacher/classroom-create-success";
    }
}
