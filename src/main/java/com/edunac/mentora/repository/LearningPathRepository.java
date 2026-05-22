package com.edunac.mentora.repository;
import com.edunac.mentora.domain.LearningPath;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LearningPathRepository extends JpaRepository<LearningPath, Integer> {

    List<LearningPath> findBySubjectIdOrderByNameAsc(Integer subjectId);

    List<LearningPath> findByCreatedByOrderByCreatedAtDesc(Integer createdBy);
}
