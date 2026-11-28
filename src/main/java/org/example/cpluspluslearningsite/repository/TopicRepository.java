package org.example.cpluspluslearningsite.repository;

import org.example.cpluspluslearningsite.entity.Topic;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TopicRepository extends JpaRepository<Topic, Long> {

    @EntityGraph(attributePaths = {"lessons"})
    List<Topic> findAllByOrderByOrderIndexAsc();
}