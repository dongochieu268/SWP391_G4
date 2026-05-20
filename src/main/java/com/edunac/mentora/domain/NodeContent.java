package com.edunac.mentora.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "node_contents")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NodeContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "node_id", nullable = false)
    private LearningNode learningNode;

    @Column(name = "content_type", nullable = false, length = 20)
    private String contentType; // TEXT, VIDEO, FILE, LINK

    @Column(length = 200)
    private String title;

    @Column(name = "content_url", length = 1000)
    private String contentUrl;

    @Column(name = "content_text", columnDefinition = "NVARCHAR(MAX)")
    private String contentText;

    @Column(name = "display_order")
    private Integer displayOrder = 1;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}