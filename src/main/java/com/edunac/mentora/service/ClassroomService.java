package com.edunac.mentora.service;
import com.edunac.mentora.repository.ClassroomRepository;
import com.edunac.mentora.domain.Classroom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ClassroomService {

    @Autowired
    private ClassroomRepository classroomRepository;

    // UC25 - TẠO LỚP HỌC
    public Classroom createClassroom(String name, Integer subjectId, Integer learningPathId,
                                     Integer semesterId, Integer teacherId) {
        Classroom classroom = new Classroom();
        classroom.setName(name);
        classroom.setSubjectId(subjectId);
        classroom.setLearningPathId(learningPathId);
        classroom.setSemesterId(semesterId);
        classroom.setTeacherId(teacherId);
        classroom.setCreatedBy(teacherId);
        classroom.setStatus("OPEN");

        String inviteCode = UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
        classroom.setInviteCode(inviteCode);

        return classroomRepository.save(classroom);
    }

    // Lấy danh sách lớp của giáo viên
    public List<Classroom> getClassroomsByTeacher(Integer teacherId) {

        return classroomRepository.findByTeacherId(teacherId);
    }
}
