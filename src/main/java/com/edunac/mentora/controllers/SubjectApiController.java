package com.edunac.mentora.controllers;

import com.edunac.mentora.models.Subject;
import com.edunac.mentora.services.SubjectService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class SubjectApiController {

    private final SubjectService subjectService;

    public SubjectApiController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping({"/api/admin/subjects", "/api/lecturer/subjects"})
    public List<Subject> getAllSubjects() {
        return subjectService.getAllSubjects();
    }

    @GetMapping({"/api/admin/subjects/{id}", "/api/lecturer/subjects/{id}"})
    public Subject getSubject(@PathVariable Integer id) {
        return subjectService.findById(id);
    }

    @PostMapping({"/api/admin/subjects", "/api/lecturer/subjects"})
    public Subject createSubject(@Valid @RequestBody Subject subject) {
        return subjectService.create(subject);
    }

    @PutMapping({"/api/admin/subjects/{id}", "/api/lecturer/subjects/{id}"})
    public Subject updateSubject(@PathVariable Integer id, @Valid @RequestBody Subject subject) {
        return subjectService.update(id, subject);
    }

    @PatchMapping({
            "/api/admin/subjects/{id}/publish",
            "/api/lecturer/subjects/{id}/publish"
    })
    public Subject publishSubject(@PathVariable Integer id) {
        return subjectService.publish(id);
    }

    @PatchMapping({
            "/api/admin/subjects/{id}/unpublish",
            "/api/lecturer/subjects/{id}/unpublish"
    })
    public Subject unpublishSubject(@PathVariable Integer id) {
        return subjectService.unpublish(id);
    }

    @DeleteMapping({"/api/admin/subjects/{id}", "/api/lecturer/subjects/{id}"})
    public Map<String, String> deleteSubject(@PathVariable Integer id) {
        subjectService.delete(id);
        return Map.of("message", "Deleted successfully");
    }
}
