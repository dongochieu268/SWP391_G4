package com.edunac.mentora.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "classrooms")
@Getter
@Setter
public class Classroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "subject_id", nullable = false)
    private Integer subjectId;

    @Column(name = "learning_path_id", nullable = false)
    private Integer learningPathId;

    @Column(name = "teacher_id", nullable = false)
    private Integer teacherId;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(name = "semester_id", nullable = false)
    private Integer semesterId;

    @Column(nullable = false, length = 20)
    private String status = "OPEN";

    @Column(name = "invite_code", length = 100, unique = true)
    private String inviteCode;

    @Column(name = "created_by", nullable = false)
    private Integer createdBy;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
