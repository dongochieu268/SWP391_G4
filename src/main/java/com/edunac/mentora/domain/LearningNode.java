package com.edunac.mentora.domain;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "learning_nodes", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"learning_path_id", "node_order"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LearningNode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "learning_path_id", nullable = false)
    private LearningPath learningPath;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(length = 2000)
    private String description;

    @Column(name = "node_order", nullable = false, precision = 18, scale = 9)
    private BigDecimal nodeOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prerequisite_node_id")
    private LearningNode prerequisiteNode;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}