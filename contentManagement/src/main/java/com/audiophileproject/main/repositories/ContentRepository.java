package com.audiophileproject.main.repositories;

import com.audiophileproject.main.models.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {
    List<Content> findAllByUserId(String userId);
    Optional<Content> findByIdAndUserId(Long id, String userId);
    @Query("SELECT DISTINCT c.tags FROM Content c WHERE c.userId = :userId AND c.tags IS NOT EMPTY")
    Set<String> findDistinctTagsByUserId(@Param("userId") String userId);
    long deleteByIdAndUserId(Long id, String userId);
}
