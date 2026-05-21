package com.edunac.mentora.service;

import com.edunac.mentora.domain.Classroom;
import com.edunac.mentora.repository.ClassroomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ClassroomService {

    @Autowired
    private ClassroomRepository classroomRepository;

    // UC25 - TẠO LỚP HỌC
    public Classroom createClassroom(String subject, String path, String semester, Integer teacherId) {

        Classroom classroom = new Classroom();

        classroom.setSubject(subject);
        classroom.setPath(path);
        classroom.setSemester(semester);
        classroom.setTeacherId(teacherId);
        classroom.setStatus("ACTIVE");

        // Sinh invite code ngắn (8 ký tự)
        String inviteCode = UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
        classroom.setInviteCode(inviteCode);

        return classroomRepository.save(classroom);
    }

    // Lấy danh sách lớp của giáo viên
    public List<Classroom> getClassroomsByTeacher(Integer teacherId) {

        return classroomRepository.findByTeacherId(teacherId);
    }
}
