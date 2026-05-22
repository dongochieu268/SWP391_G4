package com.edunac.mentora.repository;
import com.edunac.mentora.domain.Classroom;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClassroomRepository extends JpaRepository<Classroom, Integer> {

    List<Classroom> findByTeacherId(Integer teacherId);
}
