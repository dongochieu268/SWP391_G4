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

    private String subject;
    @Column(name = "class_path") // đổi từ "path" → "class_path"
    private String path;

    private String semester;

    @Column(name = "invite_code")
    private String inviteCode;

    @Column(name = "teacher_id")
    private Integer teacherId;

    private String status;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
