package com.edunac.mentora.service;
import com.edunac.mentora.repository.LearningPathRepository;
import com.edunac.mentora.domain.LearningPath;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class LearningPathService {

    @Autowired
    private LearningPathRepository learningPathRepository;

    public List<LearningPath> getPathsByTeacher(Integer teacherId) {
        return learningPathRepository.findByCreatedByOrderByCreatedAtDesc(teacherId);
    }

    public Optional<LearningPath> findById(Integer id) {
        return learningPathRepository.findById(id);
    }

    @Transactional
    public LearningPath create(String name, String description, Integer subjectId, Integer teacherId) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Tên lộ trình không được để trống.");
        }
        if (subjectId == null) {
            throw new IllegalArgumentException("Vui lòng chọn môn học.");
        }

        LearningPath path = new LearningPath();
        path.setName(name.trim());
        path.setDescription(description);
        path.setSubjectId(subjectId);
        path.setCreatedBy(teacherId);
        return learningPathRepository.save(path);
    }
}
