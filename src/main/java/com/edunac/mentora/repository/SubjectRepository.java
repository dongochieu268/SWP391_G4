package com.edunac.mentora.repository;
import com.edunac.mentora.domain.Subject;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Integer> {

    List<Subject> findByStatusOrderByNameAsc(String status);
}
