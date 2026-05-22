package com.edunac.mentora.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "classroom_members",
        uniqueConstraints = @UniqueConstraint(columnNames = {"classroom_id", "user_id"}))
@Getter
@Setter
public class ClassroomMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "classroom_id", nullable = false)
    private Integer classroomId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "role_in_class", nullable = false, length = 20)
    private String roleInClass = "STUDENT"; // STUDENT | TEACHER | TA

    @Column(nullable = false, length = 20)
    private String status = "ACTIVE"; // ACTIVE | BANNED

    @CreationTimestamp
    @Column(name = "joined_at")
    private LocalDateTime joinedAt;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;
}
