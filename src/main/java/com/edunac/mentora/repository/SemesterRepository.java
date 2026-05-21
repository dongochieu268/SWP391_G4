package com.edunac.mentora.repository;

import com.edunac.mentora.domain.Semester;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SemesterRepository extends JpaRepository<Semester, Integer> {

    List<Semester> findAllByOrderByStartDateDesc();
}
