package org.example.cpluspluslearningsite.repository;

import org.example.cpluspluslearningsite.entity.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    List<Exercise> findByLessonIdOrderByOrderIndexAsc(Long lessonId);
    List<Exercise> findByLessonIsNull(); // Задачи из общего Задачника
}