package com.edunac.mentora.repository;
import com.edunac.mentora.domain.LearningNode;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LearningNodeRepository extends JpaRepository<LearningNode, Integer> {

    List<LearningNode> findByLearningPathIdOrderByNodeOrderAsc(Integer learningPathId);
}