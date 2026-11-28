package org.example.cpluspluslearningsite.repository;

import org.example.cpluspluslearningsite.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}