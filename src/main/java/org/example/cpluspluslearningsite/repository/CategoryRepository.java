package org.example.cpluspluslearningsite.repository;

import org.example.cpluspluslearningsite.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}