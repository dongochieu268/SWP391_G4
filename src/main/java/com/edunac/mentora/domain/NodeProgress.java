package com.edunac.mentora.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "node_progress",
        uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "node_id", "classroom_id"}))
@Getter
@Setter
public class NodeProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "student_id", nullable = false)
    private Integer studentId;

    @Column(name = "node_id", nullable = false)
    private Integer nodeId;

    @Column(name = "classroom_id", nullable = false)
    private Integer classroomId;

    @Column(name = "is_completed", nullable = false)
    private Boolean isCompleted = false;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;
}
