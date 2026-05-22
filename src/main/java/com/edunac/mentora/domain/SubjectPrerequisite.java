package com.edunac.mentora.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "subject_prerequisites")
@Getter
@Setter
@IdClass(SubjectPrerequisite.SubjectPrerequisiteId.class)
public class SubjectPrerequisite {

    @Id
    @Column(name = "subject_id")
    private Integer subjectId;

    @Id
    @Column(name = "prerequisite_subject_id")
    private Integer prerequisiteSubjectId;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public static class SubjectPrerequisiteId implements Serializable {
        private Integer subjectId;
        private Integer prerequisiteSubjectId;
    }
}
