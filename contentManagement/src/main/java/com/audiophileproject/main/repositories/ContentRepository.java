package com.audiophileproject.main.repositories;

import com.audiophileproject.main.models.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {
    List<Content> findAllByUserId(String userId);

    Optional<Content> findByIdAndUserId(Long id, String userId);
}
