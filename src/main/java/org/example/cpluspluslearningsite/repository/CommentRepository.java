package org.example.cpluspluslearningsite.repository;

import org.example.cpluspluslearningsite.entity.Comment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // Загружаем последние 15 комментариев вместе с автором и уроком
    @EntityGraph(attributePaths = {"user", "lesson"})
    List<Comment> findTop15ByOrderByCreatedAtDesc();
}