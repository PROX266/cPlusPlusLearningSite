package org.example.cpluspluslearningsite.repository;

import org.example.cpluspluslearningsite.entity.Submission;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    // Загружаем последние 15 решений вместе с автором и уроком
    @EntityGraph(attributePaths = {"user", "lesson"})
    List<Submission> findTop15ByOrderBySubmittedAtDesc();
}