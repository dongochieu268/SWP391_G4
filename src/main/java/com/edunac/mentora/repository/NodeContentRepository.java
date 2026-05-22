package com.edunac.mentora.repository;
import com.edunac.mentora.domain.NodeContent;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NodeContentRepository extends JpaRepository<NodeContent, Integer> {

    List<NodeContent> findByLearningNodeIdOrderByDisplayOrderAsc(Integer nodeId);
}