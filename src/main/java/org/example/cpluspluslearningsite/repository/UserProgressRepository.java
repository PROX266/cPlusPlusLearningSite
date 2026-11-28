package org.example.cpluspluslearningsite.repository;

import org.example.cpluspluslearningsite.entity.UserProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserProgressRepository extends JpaRepository<UserProgress, Long> {
    List<UserProgress> findByUserId(Long userId);
    boolean existsByUserIdAndLessonId(Long userId, Long lessonId);
}