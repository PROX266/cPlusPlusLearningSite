package org.example.cpluspluslearningsite.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lesson_contents")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LessonContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    @Column(name = "content_type", nullable = false, length = 20)
    private String contentType; // TEXT, CODE, NOTE

    @Column(nullable = false, columnDefinition = "TEXT")
    private String body;

    @Column(name = "order_index", nullable = false)
    private Integer orderIndex;
}