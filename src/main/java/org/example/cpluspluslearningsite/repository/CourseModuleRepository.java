package org.example.cpluspluslearningsite.repository;

import org.example.cpluspluslearningsite.entity.CourseModule;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CourseModuleRepository extends JpaRepository<CourseModule, Long> {

    @EntityGraph(attributePaths = {"lessons"})
    List<CourseModule> findAllByOrderByOrderIndexAsc();
}