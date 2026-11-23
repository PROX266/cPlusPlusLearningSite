package org.example.cpluspluslearningsite.repository;

import org.example.cpluspluslearningsite.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
}