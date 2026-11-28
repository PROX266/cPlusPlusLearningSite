package org.example.cpluspluslearningsite.repository;

import org.example.cpluspluslearningsite.entity.Submission;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    @EntityGraph(attributePaths = {"user", "exercise"})
    List<Submission> findTop15ByOrderBySubmittedAtDesc();
}