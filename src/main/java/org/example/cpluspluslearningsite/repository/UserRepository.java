package org.example.cpluspluslearningsite.repository;

import org.example.cpluspluslearningsite.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    // Ищет по точному ID (если ввели число) ИЛИ по части имени без учета регистра (ILIKE/LOWER)
    @Query("SELECT u FROM User u WHERE " +
            "(:userId IS NOT NULL AND u.id = :userId) OR " +
            "(LOWER(u.username) LIKE LOWER(CONCAT('%', :query, '%'))) " +
            "ORDER BY u.createdAt DESC")
    List<User> searchByIdOrUsername(@Param("userId") Long userId, @Param("query") String query);
}