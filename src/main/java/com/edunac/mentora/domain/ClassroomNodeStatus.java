package com.edunac.mentora.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "classroom_node_status",
        uniqueConstraints = @UniqueConstraint(columnNames = {"classroom_id", "node_id"}))
@Getter
@Setter
public class ClassroomNodeStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "classroom_id", nullable = false)
    private Integer classroomId;

    @Column(name = "node_id", nullable = false)
    private Integer nodeId;

    @Column(nullable = false, length = 20)
    private String status = "HIDDEN"; // HIDDEN | VISIBLE

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
